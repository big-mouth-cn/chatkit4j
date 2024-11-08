package io.github.bigmouthcn.chatkit4j.autoconfigure;

import io.github.bigmouthcn.chatkit4j.agent.AgentFactory;
import io.github.bigmouthcn.chatkit4j.controller.ChatController;
import io.github.bigmouthcn.chatkit4j.utils.LocalFileSystemPromptFetcher;
import io.github.bigmouthcn.chatkit4j.utils.SystemPromptFetcher;
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
    public ChatController chatController(OpenAiService openAiService, AgentFactory agentFactory, SystemPromptFetcher systemPromptFetcher) {
        return new ChatController(openAiService, agentFactory, systemPromptFetcher);
    }

    @Bean
    @ConditionalOnMissingBean
    public SystemPromptFetcher systemPromptFetcher() {
        return new LocalFileSystemPromptFetcher();
    }
}
