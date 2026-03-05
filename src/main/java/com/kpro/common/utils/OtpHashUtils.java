package com.kpro.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

@UtilityClass
public class OtpHashUtils {
  public static String hashOtp(String secret, String message) {
    HmacUtils hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);
    return hmac.hmacHex(message);
  }
}
