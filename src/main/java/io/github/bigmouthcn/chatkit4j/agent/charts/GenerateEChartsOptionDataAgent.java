package io.github.bigmouthcn.chatkit4j.agent.charts;

import io.github.bigmouthcn.chatkit4j.AgentConfig;
import io.github.bigmouthcn.chatkit4j.agent.AbstractChatAgent;
import com.google.common.collect.Lists;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 */
public class GenerateEChartsOptionDataAgent extends AbstractChatAgent<GenerateEChartsOptionDataRequest> {

    private final EChartsSystemPromptFetcher eChartsSystemPromptFetcher;
    private final AgentConfig.EChartsAgentConfig echartsAgentConfig;

    public GenerateEChartsOptionDataAgent(OpenAiService openAiService, EChartsSystemPromptFetcher eChartsSystemPromptFetcher, AgentConfig.EChartsAgentConfig echartsAgentConfig) {
        super(openAiService);
        this.eChartsSystemPromptFetcher = eChartsSystemPromptFetcher;
        this.echartsAgentConfig = echartsAgentConfig;
    }

    @Override
    public String getFunctionName() {
        return "generate-echarts-options-data";
    }

    @Override
    public String getFunctionDescription() {
        return "根据输入数据生成echarts图表的option参数值";
    }

    @Override
    protected ChatCompletionRequest createBasic() {
        ChatCompletionRequest.ChatCompletionRequestBuilder builder = ChatCompletionRequest.builder();
        if (Objects.nonNull(echartsAgentConfig.getMaxTokens())) {
            builder.maxTokens(echartsAgentConfig.getMaxTokens());
        }
        return builder
                .model(echartsAgentConfig.getModel())
                .temperature(echartsAgentConfig.getTemperature())
                .build();
    }

    @Override
    protected List<ChatMessage> createChatMessages(GenerateEChartsOptionDataRequest generateEChartsOptionDataRequest) {
        List<ChatMessage> messages = Lists.newArrayList();
        SystemMessage systemMessage = eChartsSystemPromptFetcher.fetch();
        if (systemMessage != null) {
            messages.add(systemMessage);
        }
        String data = generateEChartsOptionDataRequest.getData();
        messages.add(new UserMessage(data));
        return messages;
    }

    @Override
    protected Object createResponseOnCompletion(GenerateEChartsOptionDataRequest generateEChartsOptionDataRequest, AssistantMessage assistantMessage) {
        return new GenerateEChartsOptionDataResponse().setSuccess(true).setEchartsOption(assistantMessage.getTextContent());
    }

    @Override
    protected Object createResponseOnException(GenerateEChartsOptionDataRequest generateEChartsOptionDataRequest) {
        return new GenerateEChartsOptionDataResponse().setSuccess(false);
    }
}
