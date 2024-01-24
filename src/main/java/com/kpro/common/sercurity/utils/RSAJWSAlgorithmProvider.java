package com.kpro.common.sercurity.utils;

import com.kpro.common.sercurity.exception.AuthenticationJwtException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAJWSAlgorithmProvider implements JWSProvider {
    private static final Logger log = LoggerFactory.getLogger(RSAJWSAlgorithmProvider.class);
    private final JwtAlgorithmConfig algorithmConfig;

    public RSAJWSAlgorithmProvider(JwtAlgorithmConfig algorithmConfig) {
        this.algorithmConfig = algorithmConfig;
    }

    @Override
    public JWSVerifier getVerifier() {
        try {
            byte[] b = Base64.decodeBase64(algorithmConfig.getInternalPublicKey());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(spec);
            return new RSASSAVerifier((RSAPublicKey) publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("{} get jws verifier error {}", getClass().getSimpleName(), e);
            throw new AuthenticationJwtException();

        }
    }

    @Override
    public JWSSigner getSigner() {
        try {
            byte[] b = Base64.decodeBase64(algorithmConfig.getInternalPrivateKey());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(spec);
            return new RSASSASigner(privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("{} generate internal token error {}", getClass().getSimpleName(), e);
            throw new AuthenticationJwtException();
        }
    }

    @Override
    public long getExpiredInSeconds() {
        return algorithmConfig.getExpiredInSeconds();
    }

    @Override
    public String getAlgorithm() {
        return algorithmConfig.getAlgorithm();
    }
}
