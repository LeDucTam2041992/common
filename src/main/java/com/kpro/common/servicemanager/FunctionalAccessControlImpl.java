package com.kpro.common.servicemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpro.common.dto.base.BaseApiResponse;
import com.kpro.common.exception.ErrorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FunctionalAccessControlImpl implements FunctionalAccessControl {
    private static final Logger log = LoggerFactory.getLogger(FunctionalAccessControlImpl.class);
    private final RestTemplate interRestTemplate;
    private final String urlCheckPermission;
    private final ObjectMapper mapper = new ObjectMapper();

    public FunctionalAccessControlImpl(RestTemplate interRestTemplate, String urlCheckPermission) {
        this.interRestTemplate = interRestTemplate;
        this.urlCheckPermission = urlCheckPermission;
    }

    @Override
    public boolean doCheckPermission(String username, String serviceName, String permission) {
        log.info("{} check permission {} - {} - {}", getClass().getSimpleName(), username, serviceName, permission);
        if (username == null || serviceName == null || permission == null) return false;
        try {
            String urlCheck = UriComponentsBuilder.fromHttpUrl(urlCheckPermission).encode().toUriString();
            Map<String, String> params = new HashMap<>();
            params.put("permission", String.join("_", serviceName, permission));
            params.put("username", username);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            var checkPermissionResponse = interRestTemplate.exchange(urlCheck, HttpMethod.GET, httpEntity, BaseApiResponse.class, params);
            return (boolean) Objects.requireNonNull(checkPermissionResponse.getBody()).getData();
        } catch (RestClientResponseException clientResponseException) {
            if (clientResponseException.getStatusCode() == HttpStatus.BAD_REQUEST) {
                try {
                    ErrorObject errorObject = mapper.readValue(clientResponseException.getResponseBodyAsByteArray(), ErrorObject.class);
                    log.error("{} call check permission error {}", getClass().getSimpleName(), errorObject);
                } catch (IOException exception) {
                    log.error("{} read objectError error {}", getClass().getSimpleName(), exception);
                }
            }
            log.error("{} call service manager error: http status [{}] - detail [{}]", getClass().getSimpleName(), clientResponseException.getStatusCode(), clientResponseException.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("{} check permission server error {}", getClass().getSimpleName(), e);
        }
        return false;
    }
}
