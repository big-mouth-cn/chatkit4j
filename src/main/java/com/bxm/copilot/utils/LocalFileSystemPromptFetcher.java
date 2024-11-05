package com.bxm.copilot.utils;

import com.theokanning.openai.completion.chat.SystemMessage;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 */
public class LocalFileSystemPromptFetcher implements SystemPromptFetcher {

    @Override
    public SystemMessage fetch() {
        return new SystemMessage(PromptUtils.fetch("prompt/main-system-prompt.md"));
    }
}
