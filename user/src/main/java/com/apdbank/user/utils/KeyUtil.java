package com.apdbank.user.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;


@Component
public class KeyUtil {

    @Value("${spring.keys.access-private-token}")
    private String accessPrivateTokenPath;
    @Value("${spring.keys.access-public-token}")
    private String accessPublicTokenPath;

    @Value("${spring.keys.refresh-private-token}")
    private String refreshPrivateTokenPath;
    @Value("${spring.keys.refresh-public-token}")
    private String refreshPublicTokenPath;

    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;

    public KeyPair getAccessTokenKeyPair() {

        if (Objects.isNull(accessTokenKeyPair)) {
            accessTokenKeyPair = this.getKeyPair(accessPublicTokenPath, accessPrivateTokenPath);
        }

        return accessTokenKeyPair;
    }

    public KeyPair getRefreshTokenKeyPair() {

        if (Objects.isNull(refreshTokenKeyPair)) {
            refreshTokenKeyPair = this.getKeyPair(refreshPublicTokenPath, refreshPrivateTokenPath);
        }

        return refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {

        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {

            try {

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);

                return keyPair;

            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        }

        File directory = new File("keys");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }
}
