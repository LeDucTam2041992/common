package com.kpro.common.communication;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(InternalHttpClientProperties.class)
public class CommunicationConfig {

  private final InternalHttpClientProperties properties;

  public CommunicationConfig(InternalHttpClientProperties properties) {
    this.properties = properties;
  }

  @LoadBalanced
  @Bean
  public RestTemplate interRestTemplate(
      @Qualifier("internalHttpRequestFactory") ClientHttpRequestFactory internalHttpRequestFactory,
      @Qualifier("interClientRequestInterceptor")
          List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors,
      List<InternalRestTemplateCustomizer> internalRestTemplateCustomizers) {

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(internalHttpRequestFactory);
    restTemplate.getInterceptors().addAll(clientHttpRequestInterceptors);

    for (InternalRestTemplateCustomizer customizer : internalRestTemplateCustomizers) {
      customizer.customize(restTemplate);
    }
    return restTemplate;
  }

  @Bean
  @ConditionalOnMissingBean(name = "internalHttpRequestFactory")
  public ClientHttpRequestFactory internalHttpRequestFactory(
      InternalHttpClientProperties clientProperties) {
    HttpClientBuilder builder =
        HttpClients.custom()
            .disableAuthCaching()
            .disableConnectionState()
            .disableCookieManagement();
    if (clientProperties.isUseSystemProperties()) {
      builder.useSystemProperties();
    }

    PoolingHttpClientConnectionManagerBuilder poolingHttpClientConnectionManagerBuilder =
        PoolingHttpClientConnectionManagerBuilder.create();

    if (clientProperties.isVerifyCertificateHostnames()) {
      poolingHttpClientConnectionManagerBuilder.setSSLSocketFactory(
          SSLConnectionSocketFactoryBuilder.create()
              .setSslContext(SSLContexts.createSystemDefault())
              .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
              .build());
    }

    poolingHttpClientConnectionManagerBuilder.setMaxConnTotal(clientProperties.getMaxConnTotal());
    poolingHttpClientConnectionManagerBuilder.setMaxConnPerRoute(
        clientProperties.getMaxConnPerRoute());

    poolingHttpClientConnectionManagerBuilder.setDefaultConnectionConfig(
        ConnectionConfig.custom()
            .setConnectTimeout(10, TimeUnit.SECONDS)
            .setSocketTimeout(15, TimeUnit.SECONDS)
            .setTimeToLive(clientProperties.getConnTimeToLive(), TimeUnit.MINUTES)
            .build());

    builder.setConnectionManager(poolingHttpClientConnectionManagerBuilder.build());

    RequestConfig requestConfig =
        RequestConfig.custom()
            .setResponseTimeout(Timeout.ofMilliseconds(30_000))
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(5_000))
            .build();

    builder.setDefaultRequestConfig(requestConfig);

    if (clientProperties.getAgent() != null && clientProperties.getAgent().length() > 0) {
      builder.setUserAgent(clientProperties.getAgent());
    }

    if (clientProperties.isDefaultUserAgentDisable()) {
      builder.disableDefaultUserAgent();
    }

    if (clientProperties.isEvictIdleConnections()) {
      builder.evictIdleConnections(
          TimeValue.of(clientProperties.getMaxIdleTime(), TimeUnit.SECONDS));
    }

    if (clientProperties.isEvictExpiredConnections()) {
      builder.evictExpiredConnections();
    }

    if (clientProperties.isRedirectHandlingDisable()) {
      builder.disableRedirectHandling();
    }

    if (clientProperties.isContentCompressionDisable()) {
      builder.disableContentCompression();
    }

    if (clientProperties.isAutomaticRetriesDisable()) {
      builder.disableAutomaticRetries();
    }
    return new HttpComponentsClientHttpRequestFactory(builder.build());
  }

  @Bean
  InternalRestTemplateCustomizer internalLoggingCustomizer(
      @Qualifier("internalHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory) {
    return new LoggingInternalCustomizer(clientHttpRequestFactory);
  }

  @Bean
  @Order(100)
  @Qualifier("interClientRequestInterceptor")
  public CopyHeaderInterceptor copyHeaderInterceptor() {
    return new CopyHeaderInterceptor(this.properties.getBlacklistedHeader());
  }

  @Bean
  @Order(110)
  @Qualifier("interClientRequestInterceptor")
  public LoggingInterceptor loggingInterceptor() {
    return new LoggingInterceptor();
  }
}
