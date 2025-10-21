package com.kpro.common.communication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CopyHeaderInterceptor implements ClientHttpRequestInterceptor {
    private final List<String> blacklistedHeaders = new ArrayList<>();

    public CopyHeaderInterceptor(List<String> blacklistedHeaders) {
        blacklistedHeaders.forEach(s -> {
            if (s != null) this.blacklistedHeaders.add(s);
        });
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest servletRequest = servletRequestAttributes.getRequest();
            Enumeration<String> headerNames = servletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = servletRequest.getHeader(key);
                if (!request.getHeaders().containsKey(key) && !this.isKeyBlacklisted(key)) {
                    request.getHeaders().add(key, value);
                }
            }
        }
        return execution.execute(request, body);
    }

    private boolean isKeyBlacklisted(@NotNull String key) {
        return this.blacklistedHeaders.stream().anyMatch(s -> key.toLowerCase().startsWith(s));
    }

    public List<String> getBlacklistedHeaders() {
        return blacklistedHeaders;
    }
}
