package com.devprmetrics.config;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubConfig {

    @Bean
    public PrivateKey gitHubAppPrivateKey(@Value("${github.private-key-path}") String privateKeyPath) {
        try {
            String pem = Files.readString(Path.of(privateKeyPath));
            try (PEMParser parser = new PEMParser(new StringReader(pem))) {
                Object object = parser.readObject();
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                if (object instanceof PEMKeyPair keyPair) {
                    return converter.getPrivateKey(keyPair.getPrivateKeyInfo());
                }
                if (object instanceof PrivateKeyInfo privateKeyInfo) {
                    return converter.getPrivateKey(privateKeyInfo);
                }
                throw new IllegalArgumentException("Formato de chave privada PEM não suportado.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler a chave privada em: " + privateKeyPath, e);
        }
    }
}
