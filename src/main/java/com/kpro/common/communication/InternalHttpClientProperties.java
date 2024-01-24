package com.kpro.common.communication;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties("kpro.http.inter.client")
public class InternalHttpClientProperties {
    private String agent;
    private boolean defaultUserAgentDisable = false;
    private boolean verifyCertificateHostnames = false;

    //maxConnTotal, maxConnPerRoute, connTimeToLive by default value http client builder
    private int maxConnTotal = 0;
    private int maxConnPerRoute = 0;
    private long connTimeToLive = -1L;

    private boolean evictExpiredConnections;
    private boolean evictIdleConnections;
    private long maxIdleTime;
    private boolean redirectHandlingDisable = false;
    private boolean contentCompressionDisable = false;
    private boolean automaticRetriesDisable = false;
    private boolean useSystemProperties = true;
    private boolean propagateHeaders = true;
    private List<String> blacklistedHeader = Arrays.asList("cookie", "x-forwarded-", "Forwarded", "accept", "host", "x-xsrf-token");

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
}
