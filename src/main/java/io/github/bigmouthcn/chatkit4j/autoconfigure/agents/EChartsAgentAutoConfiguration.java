package io.github.bigmouthcn.chatkit4j.autoconfigure.agents;

import io.github.bigmouthcn.chatkit4j.AgentConfig;
import io.github.bigmouthcn.chatkit4j.agent.charts.DefaultEChartsSystemPromptFetcher;
import io.github.bigmouthcn.chatkit4j.agent.charts.EChartsSystemPromptFetcher;
import io.github.bigmouthcn.chatkit4j.agent.charts.GenerateEChartsOptionDataAgent;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({
        AgentConfig.EChartsAgentConfig.class
})
public class EChartsAgentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EChartsSystemPromptFetcher eChartsSystemPromptFetcher() {
        return new DefaultEChartsSystemPromptFetcher();
    }

    @Bean
    @ConditionalOnProperty(prefix = "chatkit4j.agent.echarts", name = "enable", havingValue = "true")
    public GenerateEChartsOptionDataAgent generateEChartsOptionDataFunction(OpenAiService openAiService,
                                                                            EChartsSystemPromptFetcher eChartsSystemPromptFetcher,
                                                                            AgentConfig.EChartsAgentConfig eChartsAgentConfig) {
        return new GenerateEChartsOptionDataAgent(openAiService, eChartsSystemPromptFetcher, eChartsAgentConfig);
    }
}
