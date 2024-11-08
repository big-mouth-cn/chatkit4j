package io.github.bigmouthcn.chatkit4j.autoconfigure;

import io.github.bigmouthcn.chatkit4j.WebConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Optional;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@EnableConfigurationProperties(WebConfig.class)
public class Chatkit4jMvcConfigurer implements WebMvcConfigurer {

    private final WebConfig config;

    public Chatkit4jMvcConfigurer(WebConfig config) {
        this.config = config;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getAsyncTaskCorePoolSize());
        executor.setMaxPoolSize(config.getAsyncTaskMaxPoolSize());
        executor.setQueueCapacity(config.getAsyncTaskQueueCapacity());
        executor.initialize();

        configurer.setTaskExecutor(executor);
        configurer.setDefaultTimeout(Optional.ofNullable(config.getAsyncRequestTimeout()).orElse(Duration.ofMinutes(5)).toMillis());
    }
}
