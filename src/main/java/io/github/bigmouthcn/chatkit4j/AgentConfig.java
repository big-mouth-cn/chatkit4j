package io.github.bigmouthcn.chatkit4j;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

public class AgentConfig {

    @Data
    @ConfigurationProperties(prefix = "chatkit4j.agent.echarts")
    public static class EChartsAgentConfig {
        private boolean enable = false;
        private String model = "gpt-4o";
        private Double temperature = 0.1D;
        private Integer maxTokens;
    }
}
