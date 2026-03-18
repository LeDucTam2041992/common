package com.kpro.common.sercurity.filter;

import com.kpro.common.sercurity.config.PublicPathConfigProperties;
import com.kpro.common.sercurity.utils.TokenManager;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final TokenManager tokenManager;
  private final Set<AntPathRequestMatcher> excludedMatchers;

  public JwtAuthenticationFilter(
      TokenManager tokenManager, PublicPathConfigProperties configProperties) {
    this.tokenManager = tokenManager;
    this.excludedMatchers =
        configProperties.getPublicPaths().stream()
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toSet());
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwt = getJwtFromRequest(request);
    if (jwt == null || notFilter(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      JWTClaimsSet claimsSet = tokenManager.validateInternalJwt(jwt);
      String principal = claimsSet.getSubject();
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(principal, null, null);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
      response.setHeader("X-Error-Detail", e.getMessage());

      // Set Content Type and Write Body
//      response.setContentType("application/json");
//      response.setCharacterEncoding("UTF-8");
    }
  }

  private boolean notFilter(HttpServletRequest request) {
    return excludedMatchers.stream().anyMatch(matcher -> matcher.matches(request));
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    var token = request.getHeader("X-Internal-Token");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring("Bearer ".length());
    }
    return null;
  }
}
