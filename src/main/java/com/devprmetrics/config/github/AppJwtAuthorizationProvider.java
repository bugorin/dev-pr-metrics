package com.devprmetrics.config.github;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.kohsuke.github.authorization.AuthorizationProvider;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;

class AppJwtAuthorizationProvider implements AuthorizationProvider {

    private final long appId;
    private final RSAPrivateKey privateKey;
    private final Duration skew = Duration.ofSeconds(30);
    private final Duration ttl = Duration.ofMinutes(9); // GitHub exige <=10 min

    private String bearer;
    private Instant validUntil = Instant.MIN;

    AppJwtAuthorizationProvider(long appId, PrivateKey privateKey) {
        this.appId = appId;
        this.privateKey = (RSAPrivateKey) privateKey;
    }

    @Override
    public synchronized String getEncodedAuthorization() {
        Instant now = Instant.now();
        if (bearer == null || now.isAfter(validUntil)) {
            Instant issuedAt = now.minus(skew);
            Instant expiresAt = now.plus(ttl);

            String jwt = JWT.create()
                    .withIssuer(String.valueOf(appId))
                    .withIssuedAt(java.util.Date.from(issuedAt))
                    .withExpiresAt(java.util.Date.from(expiresAt))
                    .sign(Algorithm.RSA256(null, privateKey));

            bearer = String.format("Bearer %s", jwt);
            validUntil = expiresAt.minus(Duration.ofMinutes(1));
        }
        return bearer;
    }
}
