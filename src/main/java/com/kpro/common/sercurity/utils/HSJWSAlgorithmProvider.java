package com.kpro.common.sercurity.utils;

import com.kpro.common.sercurity.exception.AuthenticationJwtException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSJWSAlgorithmProvider implements JWSProvider {
    private static final Logger log = LoggerFactory.getLogger(HSJWSAlgorithmProvider.class);
    private final JwtAlgorithmConfig algorithmConfig;

    public HSJWSAlgorithmProvider(JwtAlgorithmConfig algorithmConfig) {
        this.algorithmConfig = algorithmConfig;
    }

    @Override
    public JWSVerifier getVerifier() {
        try {
            return new MACVerifier(algorithmConfig.getInternalKeySign());
        } catch (JOSEException e) {
            log.error("{} get jws verifier error {}", getClass().getSimpleName(), e);
            throw new AuthenticationJwtException();
        }
    }

    @Override
    public JWSSigner getSigner() {
        try {
            return new MACSigner(algorithmConfig.getInternalKeySign());
        } catch (KeyLengthException e) {
            log.error("{} get jws verifier error {}", getClass().getSimpleName(), e);
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
