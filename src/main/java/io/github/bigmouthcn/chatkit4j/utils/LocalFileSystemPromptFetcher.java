package io.github.bigmouthcn.chatkit4j.utils;

import com.theokanning.openai.completion.chat.SystemMessage;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 */
public class LocalFileSystemPromptFetcher implements SystemPromptFetcher {

    @Override
    public SystemMessage fetch() {
        String content = PromptUtils.fetch("prompt/main-system-prompt.md");
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return new SystemMessage(content);
    }
}
