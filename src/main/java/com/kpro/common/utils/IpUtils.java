package com.kpro.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
public class IpUtils {

  private static final String[] HEADERS_TO_TRY = {
    "X-Forwarded-For",
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "X-Real_IP",
  };

  public static String getClientIpAddressByIndex(HttpServletRequest request, int index) {
    for (String header : HEADERS_TO_TRY) {
      String headerString = request.getHeader(header);
      if (StringUtils.isNotBlank(headerString) && !"unknown".equalsIgnoreCase(headerString)) {
        String[] ips = headerString.split(",");
        if (index >= 0 && index < ips.length) {
          return StringUtils.trim(ips[index]);
        } else {
          return StringUtils.trim(ips[0]);
        }
      }
    }
    return StringUtils.trim(request.getRemoteAddr());
  }

  public static String getFirstClientIpAddress(HttpServletRequest request) {
    return getClientIpAddressByIndex(request, 0);
  }

  public static String getClientIpAddress() {
    ServletRequestAttributes servletRequestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletRequestAttributes == null) {
      return null;
    }
    return getFirstClientIpAddress(servletRequestAttributes.getRequest());
  }
}
