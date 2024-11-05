package com.bxm.copilot.agent;

import com.theokanning.openai.completion.chat.AssistantMessage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 抽象的，实现了 Chat 一次性对话接口。
 *
 * @author huxiao
 * @date 2024-09-13
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractChatAgent<T extends AgentRequest> implements Agent<T> {

    private final OpenAiService openAiService;

    protected AbstractChatAgent(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    /**
     * 创建 Chat 请求的基本信息，比如设置 model、max_tokens、temperature 等属性。
     * @return 请求对象
     */
    protected abstract ChatCompletionRequest createBasic();

    /**
     * 创建 Chat 请求消息
     * @param t 入参对象
     * @return 消息列表
     * @throws Exception 如果发生异常，会调用 {@link  #createResponseOnException(AgentRequest)}
     */
    protected abstract List<ChatMessage> createChatMessages(T t) throws Exception;

    /**
     * 创建 Chat 请求成功后的响应
     * @param t 入参对象
     * @param assistantMessage Chat Assistant 返回的消息对象
     * @return 响应对象
     */
    protected abstract Object createResponseOnCompletion(T t, AssistantMessage assistantMessage);

    /**
     * 创建 Chat 请求失败后的响应
     * @param t 入参对象
     * @return 响应对象
     */
    protected abstract Object createResponseOnException(T t);

    @Override
    public Object apply(T t) {
        try {
            ChatCompletionRequest requestBody = createBasic();
            if (requestBody == null) {
                return createResponseOnException(t);
            }
            List<ChatMessage> chatMessages = createChatMessages(t);
            requestBody.setStream(false);
            requestBody.setMessages(chatMessages);
            ChatCompletionResult completion = openAiService.createChatCompletion(requestBody);
            AssistantMessage message = completion.getChoices().get(0).getMessage();
            String textContent = message.getTextContent();
            log.info("{}", textContent);
            return createResponseOnCompletion(t, message);
        } catch (Exception e) {
            log.error("chat error", e);
            return createResponseOnException(t);
        }
    }
}
