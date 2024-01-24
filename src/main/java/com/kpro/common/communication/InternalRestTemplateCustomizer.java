package com.kpro.common.communication;

import org.springframework.web.client.RestTemplate;

public interface InternalRestTemplateCustomizer {
    void customize(RestTemplate restTemplate);
}
