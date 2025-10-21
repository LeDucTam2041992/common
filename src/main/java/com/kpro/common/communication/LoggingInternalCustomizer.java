package com.kpro.common.communication;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class LoggingInternalCustomizer implements InternalRestTemplateCustomizer {
  private final ClientHttpRequestFactory internalHttpRequestFactory;

  public LoggingInternalCustomizer(ClientHttpRequestFactory internalHttpRequestFactory) {
    this.internalHttpRequestFactory = internalHttpRequestFactory;
  }

  @Override
  public void customize(RestTemplate restTemplate) {
    restTemplate.setRequestFactory(
        new BufferingClientHttpRequestFactory(this.internalHttpRequestFactory));
  }
}
