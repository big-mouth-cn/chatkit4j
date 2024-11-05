package com.bxm.copilot.agent.charts;

import com.theokanning.openai.completion.chat.SystemMessage;

public interface EChartsSystemPromptFetcher {

    /**
     * 获取系统提示，如果返回 null 则不使用系统提示词。
     */
    SystemMessage fetch();
}
