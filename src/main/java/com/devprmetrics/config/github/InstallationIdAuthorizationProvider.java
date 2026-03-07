package com.devprmetrics.config.github;

import org.kohsuke.github.*;
import org.kohsuke.github.authorization.AuthorizationProvider;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

class InstallationIdAuthorizationProvider implements AuthorizationProvider {

    private final long installationId;
    private final AuthorizationProvider appAuthorizationProvider;
    private final Duration refreshSkew;

    private String authorization;
    private Instant validUntil = Instant.MIN;

    InstallationIdAuthorizationProvider(long installationId,
                                        AuthorizationProvider appAuthorizationProvider,
                                        Duration refreshSkew) {
        this.installationId = installationId;
        this.appAuthorizationProvider = appAuthorizationProvider;
        this.refreshSkew = refreshSkew;
    }

    @Override
    public synchronized String getEncodedAuthorization() throws IOException {
        Instant now = Instant.now();
        if (authorization == null || now.isAfter(validUntil)) {
            authorization = String.format("token %s", refreshInstallationToken());
        }
        return authorization;
    }

    private String refreshInstallationToken() throws IOException {
        GitHub appClient = new GitHubBuilder()
                .withAuthorizationProvider(appAuthorizationProvider)
                .build();

        GHAppInstallation installation = appClient.getApp().getInstallationById(installationId);
        GHAppInstallationToken token = installation.createToken().create();

        validUntil = token.getExpiresAt().toInstant().minus(refreshSkew);
        return Objects.requireNonNull(token.getToken());
    }
}
