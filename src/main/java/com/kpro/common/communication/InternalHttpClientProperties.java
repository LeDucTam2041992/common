package com.kpro.common.communication;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kpro.http.inter.client")
public class InternalHttpClientProperties {
  private String agent;
  private boolean defaultUserAgentDisable = false;
  private boolean verifyCertificateHostnames = false;

  private int maxConnTotal = 100;
  private int maxConnPerRoute = 20;
  private long connTimeToLive = 15;

  private long connectTimeout = 10;
  private int socketTimeOut = 15;

  private long responseTimeout = 45;
  private long connectionRequestTimeout = 5;

  private boolean evictExpiredConnections = true;
  private boolean evictIdleConnections = true;
  private long maxIdleTime = 60;
  private boolean redirectHandlingDisable = false;
  private boolean contentCompressionDisable = false;
  private boolean automaticRetriesDisable = false;
  private boolean useSystemProperties = true;
  private boolean propagateHeaders = true;
  private List<String> blacklistedHeader =
      Arrays.asList("cookie", "x-forwarded-", "Forwarded", "accept", "host", "x-xsrf-token");

  public String getAgent() {
    return agent;
  }

  public void setAgent(String agent) {
    this.agent = agent;
  }

  public boolean isDefaultUserAgentDisable() {
    return defaultUserAgentDisable;
  }

  public void setDefaultUserAgentDisable(boolean defaultUserAgentDisable) {
    this.defaultUserAgentDisable = defaultUserAgentDisable;
  }

  public boolean isVerifyCertificateHostnames() {
    return verifyCertificateHostnames;
  }

  public void setVerifyCertificateHostnames(boolean verifyCertificateHostnames) {
    this.verifyCertificateHostnames = verifyCertificateHostnames;
  }

  public int getMaxConnTotal() {
    return maxConnTotal;
  }

  public void setMaxConnTotal(int maxConnTotal) {
    this.maxConnTotal = maxConnTotal;
  }

  public int getMaxConnPerRoute() {
    return maxConnPerRoute;
  }

  public void setMaxConnPerRoute(int maxConnPerRoute) {
    this.maxConnPerRoute = maxConnPerRoute;
  }

  public long getConnTimeToLive() {
    return connTimeToLive;
  }

  public void setConnTimeToLive(long connTimeToLive) {
    this.connTimeToLive = connTimeToLive;
  }

  public boolean isEvictExpiredConnections() {
    return evictExpiredConnections;
  }

  public void setEvictExpiredConnections(boolean evictExpiredConnections) {
    this.evictExpiredConnections = evictExpiredConnections;
  }

  public boolean isEvictIdleConnections() {
    return evictIdleConnections;
  }

  public void setEvictIdleConnections(boolean evictIdleConnections) {
    this.evictIdleConnections = evictIdleConnections;
  }

  public long getMaxIdleTime() {
    return maxIdleTime;
  }

  public void setMaxIdleTime(long maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
  }

  public boolean isRedirectHandlingDisable() {
    return redirectHandlingDisable;
  }

  public void setRedirectHandlingDisable(boolean redirectHandlingDisable) {
    this.redirectHandlingDisable = redirectHandlingDisable;
  }

  public boolean isContentCompressionDisable() {
    return contentCompressionDisable;
  }

  public void setContentCompressionDisable(boolean contentCompressionDisable) {
    this.contentCompressionDisable = contentCompressionDisable;
  }

  public boolean isAutomaticRetriesDisable() {
    return automaticRetriesDisable;
  }

  public void setAutomaticRetriesDisable(boolean automaticRetriesDisable) {
    this.automaticRetriesDisable = automaticRetriesDisable;
  }

  public boolean isUseSystemProperties() {
    return useSystemProperties;
  }

  public void setUseSystemProperties(boolean useSystemProperties) {
    this.useSystemProperties = useSystemProperties;
  }

  public boolean isPropagateHeaders() {
    return propagateHeaders;
  }

  public void setPropagateHeaders(boolean propagateHeaders) {
    this.propagateHeaders = propagateHeaders;
  }

  public List<String> getBlacklistedHeader() {
    return blacklistedHeader;
  }

  public void setBlacklistedHeader(List<String> blacklistedHeader) {
    this.blacklistedHeader = blacklistedHeader;
  }

  public long getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(long connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getSocketTimeOut() {
    return socketTimeOut;
  }

  public void setSocketTimeOut(int socketTimeOut) {
    this.socketTimeOut = socketTimeOut;
  }

  public long getResponseTimeout() {
    return responseTimeout;
  }

  public void setResponseTimeout(long responseTimeout) {
    this.responseTimeout = responseTimeout;
  }

  public long getConnectionRequestTimeout() {
    return connectionRequestTimeout;
  }

  public void setConnectionRequestTimeout(long connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }
}
