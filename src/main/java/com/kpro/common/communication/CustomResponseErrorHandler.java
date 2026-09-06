package com.kpro.common.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpro.common.exception.BaseException;
import com.kpro.common.exception.KproCommonErrorCode;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

public class CustomResponseErrorHandler implements ClientHttpRequestInterceptor {

  private static final Logger log = LoggerFactory.getLogger(CustomResponseErrorHandler.class);

  private static ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    if (!"true".equalsIgnoreCase(request.getHeaders().getFirst("handle-exception"))) {
      return execution.execute(request, body);
    }

    try {
      return execution.execute(request, body);
    } catch (HttpStatusCodeException ex) {
      if (ex.getStatusCode() == HttpStatus.BAD_REQUEST
          || ex.getStatusCode() == HttpStatus.FORBIDDEN
          || ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        try {
          throw objectMapper.readValue(ex.getResponseBodyAsString(), BaseException.class);
        } catch (JsonProcessingException ex2) {
          throw new BaseException(KproCommonErrorCode.INTERNAL_ERROR);
        }
      }
      throw ex;
    } catch (ResourceAccessException ex3) {
      log.error(
          "[{}} call internal api [{}] error [{}]",
          getClass().getSimpleName(),
          request.getURI().getPath(),
          ex3.getMessage());
      Throwable cause = ex3.getCause();
      if (cause instanceof SocketTimeoutException) {
        throw new RestInternalTimeoutException();
      }
      throw ex3;
    } catch (Exception ex4) {
      log.error(
          "[{}} call internal api [{}] error [{}]",
          getClass().getSimpleName(),
          request.getURI().getPath(),
          ex4.getMessage());
      throw ex4;
    }
  }
}
