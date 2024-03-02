package com.kpro.common.sercurity.filter;

import com.kpro.common.sercurity.config.PublicPathConfigProperties;
import com.kpro.common.sercurity.utils.InternalTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final InternalTokenUtils tokenValidator;
    private final Set<AntPathRequestMatcher> excludedMatchers;

    public JwtAuthenticationFilter(InternalTokenUtils tokenValidator, PublicPathConfigProperties configProperties) {
        this.tokenValidator = tokenValidator;
        this.excludedMatchers = configProperties.getPaths().stream().map(AntPathRequestMatcher::new).collect(Collectors.toSet());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            String principal = tokenValidator.validateInternalJwt(jwt);

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(principal, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            log.error("error message", ex);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedMatchers.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        var internalToken = request.getHeader("internal-token").trim();
        log.info("{} internal token: {}", getClass().getSimpleName(), internalToken);
        return internalToken.split(" ")[1];
    }

}
