package com.kpro.common.sercurity.utils;

import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;

public interface JWSProvider {
    JWSVerifier getVerifier();
    JWSSigner getSigner();
    long getExpiredInSeconds();
    String getAlgorithm();
}
