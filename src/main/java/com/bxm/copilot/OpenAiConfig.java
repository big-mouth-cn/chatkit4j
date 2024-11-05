package com.bxm.copilot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author huxiao
 * @date 2024/10/16
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "chatkit4j.openai")
public class OpenAiConfig {

    public static final String BASE_URL = "https://api.aigateway.work/v1/";
    public static final String ACCESS_KEY = "sk-8hfa7zXO1Jw8CPofz";

    private String baseUrl = BASE_URL;
    private String accessKey = ACCESS_KEY;
    /**
     * API read timeout
     */
    private Duration timeout = Duration.ofMinutes(5);
}
