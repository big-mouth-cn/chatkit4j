package io.github.bigmouthcn.chatkit4j.agent;

/**
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
public interface Agent<T extends AgentRequest> extends java.util.function.Function<T, Object> {

    /**
     * 返回方法名称
     */
    String getFunctionName();

    /**
     * 返回方法的描述
     */
    String getFunctionDescription();
}
