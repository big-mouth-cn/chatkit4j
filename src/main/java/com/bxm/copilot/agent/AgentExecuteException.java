package com.bxm.copilot.agent;

/**
 * 在 Agent 执行时抛出这个异常，系统会将这个异常的信息当做 tool 的处理结果返回给 GPT。
 * @author huxiao
 * @date 2024/10/17
 * @since 1.0.0
 */
public class AgentExecuteException extends RuntimeException {

    public AgentExecuteException() {
    }

    public AgentExecuteException(String message) {
        super(message);
    }

    public AgentExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentExecuteException(Throwable cause) {
        super(cause);
    }

    public AgentExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
