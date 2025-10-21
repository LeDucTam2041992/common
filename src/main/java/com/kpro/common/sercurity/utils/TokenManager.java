package com.kpro.common.sercurity.utils;

import com.kpro.common.exception.KproCommonErrorCode;
import com.kpro.common.sercurity.exception.AuthenticationJwtException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TokenManager {

  private static final Logger log = LoggerFactory.getLogger(TokenManager.class);
  private final JWSProvider jwsProvider;
  private final JWTClaimsSet.Builder exactMatchClaimsBuilder = new JWTClaimsSet.Builder();
  private final Set<String> requiredClaims = Set.of();
  private final RandomValueStringGenerator strGenerator;

  public TokenManager(JWSProvider jwsProvider) {
    this.jwsProvider = jwsProvider;
    this.strGenerator = new RandomValueStringGenerator(200);
  }

  public JWTClaimsSet validateInternalJwt(String jwt) {
    SignedJWT signedJWT;
    JWTClaimsSet jwtClaimsSet;
    try {
      signedJWT = SignedJWT.parse(jwt);
      JWSVerifier verifier = jwsProvider.getVerifier();
      if (!signedJWT.verify(verifier)) {
        throw new AuthenticationJwtException(
            KproCommonErrorCode.TOKEN_INVALID, "Token sign invalid");
      }
      jwtClaimsSet = signedJWT.getJWTClaimsSet();
      DefaultJWTClaimsVerifier<SecurityContext> claimsVerifier =
          new DefaultJWTClaimsVerifier<>(exactMatchClaimsBuilder.build(), requiredClaims);
      claimsVerifier.verify(jwtClaimsSet, null);
    } catch (ParseException e) {
      log.error("Parse token error [{}]", e.getMessage());
      throw new AuthenticationJwtException(KproCommonErrorCode.TOKEN_INVALID);
    } catch (JOSEException e) {
      log.error("Verify sign token error [{}]", e.getMessage());
      throw new AuthenticationJwtException(KproCommonErrorCode.TOKEN_INVALID);
    } catch (BadJWTException e) {
      log.error("Claims verify error [{}]", e.getMessage());
      if (StringUtils.equalsIgnoreCase("Expired JWT", e.getMessage())) {
        throw new AuthenticationJwtException(KproCommonErrorCode.TOKEN_EXPIRED);
      }
      throw new AuthenticationJwtException(KproCommonErrorCode.TOKEN_INVALID);
    }
    return jwtClaimsSet;
  }

  public String generateInternalJwt(String sub, Map<String, Object> claims)
      throws JOSEException {
    long currentTime = System.currentTimeMillis();
    var jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
    jwtClaimsSetBuilder
        .subject(sub)
        .claim("data", strGenerator.generate())
        .expirationTime(new Date(currentTime + jwsProvider.getExpiredInSeconds() * 1000))
        .issueTime(new Date(currentTime));
    claims.forEach(jwtClaimsSetBuilder::claim);

    SignedJWT signedJWT =
        new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.parse(jwsProvider.getAlgorithm())).build(),
            jwtClaimsSetBuilder.build());

    JWSSigner signer = jwsProvider.getSigner();
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }

  public String getUsernameFromAuth() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (String) authentication.getPrincipal();
  }
}
