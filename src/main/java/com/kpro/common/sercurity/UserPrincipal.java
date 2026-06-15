package com.kpro.common.sercurity;

import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.Objects;
import lombok.Data;

@Data
public class UserPrincipal {
  private String userId;
  private String username;

  public static UserPrincipal from(JWTClaimsSet claimsSet) throws ParseException {
    Objects.requireNonNull(claimsSet, "JWTClaimsSet must not be null");
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setUserId(claimsSet.getSubject());
    userPrincipal.setUsername(claimsSet.getStringClaim("username"));
    return userPrincipal;
  }
}
