package io.github.bigmouthcn.chatkit4j.controller;

import io.github.bigmouthcn.chatkit4j.agent.AgentExecuteException;
import io.github.bigmouthcn.chatkit4j.agent.AgentFactory;
import io.github.bigmouthcn.chatkit4j.autoconfigure.OpenAiServiceAutoConfiguration;
import io.github.bigmouthcn.chatkit4j.utils.NamedThreadFactory;
import io.github.bigmouthcn.chatkit4j.utils.SystemPromptFetcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.theokanning.openai.assistants.run.ToolChoice;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.function.FunctionDefinition;
import com.theokanning.openai.function.FunctionExecutorManager;
import com.theokanning.openai.service.ChatMessageAccumulator;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@Slf4j
@RestController
public class ChatController {

    private final OpenAiService openAiService;
    private final ExecutorService executor;
    private final AgentFactory agentFactory;
    private final SystemPromptFetcher systemPromptFetcher;

    public ChatController(OpenAiService openAiService, AgentFactory agentFactory, SystemPromptFetcher systemPromptFetcher) {
        this.openAiService = openAiService;
        this.agentFactory = agentFactory;
        this.systemPromptFetcher = systemPromptFetcher;
        this.executor = new ThreadPoolExecutor(200, 200, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("chat"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @GetMapping("/v1/agents")
    public Object getAgents() {
        return agentFactory.getFunctionDefinitions();
    }

    @PostMapping("/v1/chat/completions")
    public Object chatCompletions(@RequestBody ChatCompletionRequest requestBody) {
        List<ChatMessage> chatMessages = requestBody.getMessages();
        ChatMessage systemMessage = createSystemMessage();
        if (null != systemMessage) {
            chatMessages.add(0, systemMessage);
        }

        List<FunctionDefinition> chatFunctions = createTools();

        FunctionExecutorManager functionExecutorManager = OpenAiServiceAutoConfiguration.createFunctionExecutorManager(chatFunctions);
        List<ChatTool> chatTools = chatFunctions.stream().map(ChatTool::new).collect(Collectors.toList());

        if (!chatTools.isEmpty()) {
            requestBody.setTools(chatTools);
            requestBody.setToolChoice(ToolChoice.AUTO);
        }

        boolean stream = Optional.ofNullable(requestBody.getStream()).orElse(false);

        if (stream) {
            SseEmitter sseEmitter = new SseEmitter();

            executor.submit(() -> {
                while (true) {
                    Flowable<ChatCompletionChunk> streamedChatCompletion = openAiService.streamChatCompletion(requestBody);

                    AssistantMessage accumulatedMessage = openAiService.mapStreamToAccumulatorWrapper(streamedChatCompletion)
                            .doOnNext((chatMessageAccumulatorWrapper -> {
                                ChatMessageAccumulator chatMessageAccumulator = chatMessageAccumulatorWrapper.getChatMessageAccumulator();
                                if (!chatMessageAccumulator.isFunctionCall()) {
                                    ChatCompletionChunk chatCompletionChunk = chatMessageAccumulatorWrapper.getChatCompletionChunk();
                                    String source = chatCompletionChunk.getSource();
                                    if (StringUtils.isNotEmpty(source)) {
                                        byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
                                        sseEmitter.send(SseEmitter.event().data(bytes, MediaType.APPLICATION_JSON_UTF8));
                                    }
                                }
                            }))
                            .doOnError(throwable -> {
                                sseEmitter.completeWithError(throwable);
                                log.error("streamChatCompletion error", throwable);
                            })
                            .lastElement()
                            .blockingGet()
                            .getChatMessageAccumulator()
                            .getAccumulatedMessage();
                    chatMessages.add(accumulatedMessage);

                    List<ChatToolCall> functionCalls = accumulatedMessage.getToolCalls();
                    if (Objects.isNull(functionCalls)) {
                        break;
                    }
                    List<ToolMessage> executed = this.executeFunctions(functionExecutorManager, functionCalls);
                    chatMessages.addAll(executed);
                }
                sseEmitter.complete();
            });

            return sseEmitter;
        } else {
            ChatCompletionResult completion;
            AssistantMessage message;
            while (true) {
                completion = openAiService.createChatCompletion(requestBody);
                message = completion.getChoices().get(0).getMessage();
                chatMessages.add(message);
                List<ChatToolCall> functionCalls = message.getToolCalls();
                if (Objects.nonNull(functionCalls)) {
                    List<ToolMessage> executed = this.executeFunctions(functionExecutorManager, functionCalls);
                    chatMessages.addAll(executed);
                    continue;
                }
                break;
            }
            return completion;
        }
    }

    private List<ToolMessage> executeFunctions(FunctionExecutorManager functionExecutorManager, List<ChatToolCall> functionCalls) {
        List<ToolMessage> chatMessages = new ArrayList<>();
        for (ChatToolCall functionCall : functionCalls) {
            ChatFunctionCall function = functionCall.getFunction();
            String name = function.getName();
            JsonNode arguments = function.getArguments();
            log.info("execution tool: {} - {}", name, arguments);
            String toolId = functionCall.getId();
            ToolMessage toolMessage = null;
            try {
                toolMessage = functionExecutorManager.executeAndConvertToChatMessage(name, arguments, toolId);
                log.info("executed: {}", toolMessage.getTextContent());
            } catch (AgentExecuteException e) {
                toolMessage = new ToolMessage(e.getMessage(), toolId);
                log.warn("executed: {}", e.getMessage());
            } catch (Exception e) {
                String content = "execute tool error: " + e.getMessage();
                toolMessage = new ToolMessage(content, toolId);
                log.error("execute tool error: {}", e.getMessage());
            }

            chatMessages.add(toolMessage);
        }
        return chatMessages;
    }

    private ChatMessage createSystemMessage() {
        return systemPromptFetcher.fetch();
    }

    private List<FunctionDefinition> createTools() {
        Collection<FunctionDefinition> functionDefinitions = agentFactory.getFunctionDefinitions();
        return new ArrayList<>(functionDefinitions);
    }
}
