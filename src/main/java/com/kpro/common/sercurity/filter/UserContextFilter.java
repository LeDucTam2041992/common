package com.kpro.common.sercurity.filter;

import com.kpro.common.context.UserContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserContextFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    try {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      String username = httpRequest.getHeader("x-user-context");
      String locale = httpRequest.getHeader("x-locale-context");

      if (username != null) {
        UserContext.setUser(username);
      }

      List<String> locales;
      if (locale != null) {
        locales = Arrays.asList(locale.split(","));
      } else {
        locales = List.of("vi");
      }

      UserContext.setLanguages(locales);

      chain.doFilter(request, response);
    } finally {
      UserContext.clearAll();
    }
  }
}
