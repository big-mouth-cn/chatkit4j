package io.github.bigmouthcn.chatkit4j.utils;

import com.theokanning.openai.completion.chat.SystemMessage;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 * @see LocalFileSystemPromptFetcher
 */
public interface SystemPromptFetcher {

    /**
     * 获取系统提示，如果返回 null 则不使用系统提示词。
     */
    SystemMessage fetch();
}
