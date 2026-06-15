package com.kpro.common.dto.base;

import java.util.Collections;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiResponseFactory {
  public static final String SUCCESS_MESS = "SUCCESS";
  public static final String SUCCESS_CODE = "200";
  public static final String FAIL_MESS = "FAILED";
  public static final String FAIL_CODE = "400";

  public static <T> BaseApiResponse<T> success(T resultObject) {
    return toBaseApiResponse(resultObject, SUCCESS_MESS, SUCCESS_CODE);
  }

  private static <T> BaseApiResponse<T> toBaseApiResponse(
      T resultObject, String successMess, String successCode) {
    BaseApiResponse<T> result = new BaseApiResponse<>();
    ApiMessage message = new ApiMessage();
    message.setMessage(successMess);
    message.setCode(successCode);
    result.setData(resultObject);
    result.setMessages(Collections.singletonList(message));

    return result;
  }

  public static <T> BaseApiResponse<T> fail(T resultObject) {
    return toBaseApiResponse(resultObject, FAIL_MESS, FAIL_CODE);
  }
}
