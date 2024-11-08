package io.github.bigmouthcn.chatkit4j;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "chatkit4j.web")
public class WebConfig {

    private int asyncTaskCorePoolSize = 200;
    private int asyncTaskMaxPoolSize = 200;
    private int asyncTaskQueueCapacity = 1000;

    private Duration asyncRequestTimeout = Duration.ofMinutes(5);
}
