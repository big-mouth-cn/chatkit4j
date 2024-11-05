package com.bxm.copilot.autoconfigure;

import com.bxm.copilot.agent.AgentFactory;
import com.bxm.copilot.controller.ChatController;
import com.bxm.copilot.utils.LocalFileSystemPromptFetcher;
import com.bxm.copilot.utils.SystemPromptFetcher;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author huxiao
 * @date 2024/9/12
 * @since 1.0.0
 */
public class Chatkit4jAutoConfiguration {

    @Bean
    public AgentFactory agentFactory() {
        return new AgentFactory();
    }

    @Bean
    public ChatController copilotController(OpenAiService openAiService, AgentFactory agentFactory, SystemPromptFetcher systemPromptFetcher) {
        return new ChatController(openAiService, agentFactory, systemPromptFetcher);
    }

    @Bean
    @ConditionalOnMissingBean
    public SystemPromptFetcher systemPromptFetcher() {
        return new LocalFileSystemPromptFetcher();
    }
}
