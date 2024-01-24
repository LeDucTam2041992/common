package com.kpro.common.sercurity.utils;

import com.kpro.common.sercurity.exception.AuthenticationJwtException;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.util.Date;

public class InternalTokenUtils {
    private static final Logger log = LoggerFactory.getLogger(InternalTokenUtils.class);
    private static final String TOKEN_INVALID = "AUT-10-002";
    private static final String TOKEN_EXPIRED = "AUTH-20-003";
    private static final String ADMIN = "tamld";
    private final JWSProvider jwsProvider;
    private RandomValueStringGenerator strGenerator;

    public InternalTokenUtils(JWSProvider jwsProvider) {
        this.jwsProvider = jwsProvider;
        this.strGenerator = new RandomValueStringGenerator(200);
    }

    public String validateInternalJwt(String jwt) throws ParseException {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(jwt);
            JWSVerifier verifier = jwsProvider.getVerifier();
            if (!signedJWT.verify(verifier)) {
                throw new AuthenticationJwtException(TOKEN_INVALID, "Token sign invalid");
            }
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expirationTime = claims.getExpirationTime();
            boolean expired = expirationTime != null && expirationTime.before(new Date());
            if (expired) throw new AuthenticationJwtException(TOKEN_EXPIRED);
        } catch (JOSEException | ParseException e) {
            log.error("{} parse token error {}", getClass().getSimpleName(), e);
            throw new AuthenticationJwtException(TOKEN_INVALID ,"Token invalid, can not create verifier");
        }
        log.info("{} sign jwt {}", getClass().getSimpleName(), signedJWT.getJWTClaimsSet().getSubject());
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public String generateInternalJwt(String username) throws JOSEException {
        long currentTime = System.currentTimeMillis();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.parse(jwsProvider.getAlgorithm())).build(),
                new JWTClaimsSet.Builder()
                        .subject(username)
                        .claim("data", strGenerator.generate())
                        .expirationTime(new Date(currentTime + jwsProvider.getExpiredInSeconds() * 1000))
                        .issueTime(new Date(currentTime))
                        .build());

        JWSSigner signer = jwsProvider.getSigner();
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public String getUsernameFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ADMIN;
        }
        return (String) authentication.getPrincipal();
    }
}
