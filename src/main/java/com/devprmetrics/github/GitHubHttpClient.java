package com.devprmetrics.github;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubHttpClient {

    private final RestClient gitHubApiClient;
    private final String organization;
    private final long appId;
    private final long installationId;
    private final PrivateKey privateKey;
    private String cachedInstallationToken;
    private Instant cachedInstallationTokenExpiresAt;

    public GitHubHttpClient(
            @Value("${github.org}") String organization,
            @Value("${github.app-id}") long appId,
            @Value("${github.installation-id}") long installationId,
            PrivateKey gitHubAppPrivateKey) {
        this.organization = organization;
        this.appId = appId;
        this.installationId = installationId;
        this.privateKey = gitHubAppPrivateKey;
        this.gitHubApiClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public String organization() {
        return organization;
    }

    public JsonNode get(GithubRequest request) {
        JsonNode body = gitHubApiClient.get()
                .uri(request.getUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + resolveInstallationToken())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(JsonNode.class);

        System.out.println(request.getUrl());
        System.out.println(body.toPrettyString());
        return body;
    }

    private synchronized String resolveInstallationToken() {
        Instant now = Instant.now();
        if (cachedInstallationToken != null
                && cachedInstallationTokenExpiresAt != null
                && now.isBefore(cachedInstallationTokenExpiresAt.minusSeconds(60))) {
            return cachedInstallationToken;
        }

        JsonNode response = gitHubApiClient.post()
                .uri("/app/installations/{installationId}/access_tokens", installationId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + createAppJwt())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(JsonNode.class);

        if (response == null || response.path("token").isMissingNode()) {
            throw new IllegalStateException("Falha ao obter installation token do GitHub App.");
        }

        cachedInstallationToken = response.path("token").asText();
        cachedInstallationTokenExpiresAt = Instant.parse(response.path("expires_at").asText());
        return cachedInstallationToken;
    }

    private String createAppJwt() {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(String.valueOf(appId))
                .withIssuedAt(java.util.Date.from(now.minusSeconds(30)))
                .withExpiresAt(java.util.Date.from(now.plusSeconds(9 * 60)))
                .sign(Algorithm.RSA256(null, (RSAPrivateKey) privateKey));
    }
}
