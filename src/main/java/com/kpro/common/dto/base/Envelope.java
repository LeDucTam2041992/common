package com.kpro.common.dto.base;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Envelope<T> {
  private final T data;
  private final Map<String, String> headers = new HashMap<>();

  public Envelope(T data) {
    this.data = data;
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public T getData() {
    return data;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public ResponseEntity<T> toResponseEntity() {
    HttpHeaders httpHeaders = new HttpHeaders();
    this.headers.forEach(httpHeaders::add);
    return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);
  }
}
