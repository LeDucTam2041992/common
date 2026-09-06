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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

  private static final Logger log = LoggerFactory.getLogger(CommunicationConfig.class);

  private final InternalHttpClientProperties properties;

  public CommunicationConfig(InternalHttpClientProperties properties) {
    this.properties = properties;
  }

  @LoadBalanced
  @Bean("interRestTemplate")
  @ConditionalOnProperty(
      name = "kpro.http.inter.load-balancer.enabled",
      havingValue = "true"
  )
  public RestTemplate interRestTemplate(
      @Qualifier("internalHttpRequestFactory") ClientHttpRequestFactory internalHttpRequestFactory,
      @Qualifier("interClientRequestInterceptor")
          List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors,
      @Qualifier("internalRestTemplateCustomizer")
          List<InternalRestTemplateCustomizer> internalRestTemplateCustomizers) {

    return getRestTemplate(internalHttpRequestFactory, clientHttpRequestInterceptors,
        internalRestTemplateCustomizers);
  }

  @Bean("interRestTemplate")
  @ConditionalOnProperty(
      name = "kpro.http.inter.load-balancer.enabled",
      havingValue = "false",
      matchIfMissing = true
  )
  public RestTemplate loadBalancedRestTemplate(
      @Qualifier("internalHttpRequestFactory") ClientHttpRequestFactory internalHttpRequestFactory,
      @Qualifier("interClientRequestInterceptor")
          List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors,
      @Qualifier("internalRestTemplateCustomizer")
          List<InternalRestTemplateCustomizer> internalRestTemplateCustomizers) {

    return getRestTemplate(internalHttpRequestFactory, clientHttpRequestInterceptors,
        internalRestTemplateCustomizers);
  }

  @NotNull
  private RestTemplate getRestTemplate(
      @Qualifier("internalHttpRequestFactory") ClientHttpRequestFactory internalHttpRequestFactory,
      @Qualifier("interClientRequestInterceptor") List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors,
      @Qualifier("internalRestTemplateCustomizer") List<InternalRestTemplateCustomizer> internalRestTemplateCustomizers) {
    if (clientHttpRequestInterceptors != null && !clientHttpRequestInterceptors.isEmpty()) {
      clientHttpRequestInterceptors.forEach(
          e ->
              log.info(
                  "Start config inter rest template with ClientHttpRequestInterceptor [{}]",
                  e.getClass().getSimpleName()));
    }

    if (internalRestTemplateCustomizers != null && !internalRestTemplateCustomizers.isEmpty()) {
      internalRestTemplateCustomizers.forEach(
          e ->
              log.info(
                  "Start config inter rest template with InternalRestTemplateCustomizer [{}]",
                  e.getClass().getSimpleName()));
    }

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
    HttpClientBuilder builder = HttpClients.custom();

    if (clientProperties.isUseSystemProperties()) {
      builder.useSystemProperties();
    }

    builder.disableAuthCaching().disableConnectionState().disableCookieManagement();

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
            // 8s time for handshake with server
            .setConnectTimeout(clientProperties.getConnectTimeout(), TimeUnit.SECONDS)

            // 5s the maximum period of inactivity between 2 consecutive data // packets (at tcp
            // level, not application)
            .setSocketTimeout(clientProperties.getSocketTimeOut(), TimeUnit.SECONDS)
            .setTimeToLive(clientProperties.getConnTimeToLive(), TimeUnit.MINUTES) // 5min
            // validate conn before use if idle > 2s
            .setValidateAfterInactivity(TimeValue.ofSeconds(2))
            .build());

    builder.setConnectionManager(poolingHttpClientConnectionManagerBuilder.build());

    RequestConfig requestConfig =
        RequestConfig.custom()
            // [50s] ghi đè [5s] của ConnectionConfig.setSocketTimeout khi gủi request
            // -> Không phải là tgian nhận byte đầu tiên từ khi gửi request thành công.
            // Bật DefaultManagedHttpClientConnection: DEBUG để check sẽ có 2 dòng log
            // http-outgoing-1 set socket timeout to 5 SECONDS
            // http-outgoing-1 set socket timeout to 50 SECONDS
            .setResponseTimeout(Timeout.ofSeconds(clientProperties.getResponseTimeout()))

            // 5s // time request pooling take connection
            .setConnectionRequestTimeout(
                Timeout.ofSeconds(clientProperties.getConnectionRequestTimeout()))
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
  @ConditionalOnProperty(
      name = "kpro.http.inter.client.logging.enable",
      havingValue = "true",
      matchIfMissing = false)
  @Qualifier("internalRestTemplateCustomizer")
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
  @ConditionalOnProperty(
      name = "kpro.http.inter.client.logging.enable",
      havingValue = "true",
      matchIfMissing = false)
  @Qualifier("interClientRequestInterceptor")
  public LoggingInterceptor loggingInterceptor() {
    return new LoggingInterceptor();
  }
}
