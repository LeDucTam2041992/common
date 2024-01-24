package com.kpro.common.exception;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(MessageResourceConfigProperties.class)
public class ErrorMessageLoader {
    private static final Logger log = LoggerFactory.getLogger(ErrorMessageLoader.class);
    private static Map<String, ErrorMessage> errorMessageMap;
    private final MessageResourceConfigProperties configProperties;

    public ErrorMessageLoader(MessageResourceConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @PostConstruct
    public synchronized void loadConfig() {
        log.info("-----load error config message------");
        Charset charset = StandardCharsets.UTF_8;
        try(var enResourceStream = new InputStreamReader(Objects.requireNonNull(ErrorMessageLoader.class.getResourceAsStream(configProperties.getEnResourceUrl())), charset);
            var vnResourceStream = new InputStreamReader(Objects.requireNonNull(ErrorMessageLoader.class.getResourceAsStream(configProperties.getViResourceUrl())), charset)) {
            Properties englishMessages = new Properties();
            englishMessages.load(enResourceStream);
            errorMessageMap = englishMessages.entrySet().stream().collect(
                    Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setEn(e.getValue().toString());
                                return errorMessage;
                            }
                    )
            );
            Properties vietnameseMessages = new Properties();
            vietnameseMessages.load(vnResourceStream);
            errorMessageMap.forEach((key, value) -> value.setVn(vietnameseMessages.getProperty(key)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ErrorMessage getMessage(String errorCode) {
        return errorMessageMap.get(errorCode) != null ? errorMessageMap.get(errorCode) : new ErrorMessage("Server is temporarily unavailable, please come back in a few minutes", "Server tạm thời không khả dụng, vui long quay lại sau ít phút");
    }
}
