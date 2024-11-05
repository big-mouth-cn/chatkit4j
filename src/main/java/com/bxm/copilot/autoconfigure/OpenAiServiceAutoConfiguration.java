package com.bxm.copilot.autoconfigure;

import com.bxm.copilot.OpenAiConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.theokanning.openai.function.FunctionDefinition;
import com.theokanning.openai.function.FunctionExecutorManager;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@EnableConfigurationProperties(OpenAiConfig.class)
public class OpenAiServiceAutoConfiguration {

    @Bean
    public OpenAiService openAiService(OpenAiConfig openAiConfig) {
        String baseUrl = openAiConfig.getBaseUrl();
        String accessKey = openAiConfig.getAccessKey();
        Duration timeout = openAiConfig.getTimeout();
        return new OpenAiService(accessKey, timeout, baseUrl);
    }

    public static ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
    ;

    public static FunctionExecutorManager createFunctionExecutorManager(List<FunctionDefinition> functionDefinitionList) {
        return new FunctionExecutorManager(MAPPER, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()), functionDefinitionList);
    }
}
