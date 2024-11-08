package io.github.bigmouthcn.chatkit4j.agent.charts;

import com.theokanning.openai.completion.chat.SystemMessage;

public class DefaultEChartsSystemPromptFetcher implements EChartsSystemPromptFetcher {

    @Override
    public SystemMessage fetch() {
        String prompt = "请担任echarts(echarts.apache.org-版本5.4.3)开发专家，我将输入一些数据给你，请你为它生成创建echarts图表所需要的option参数值，只要option值，并且必须用```echarts ```格式来回答，不需要提供生成过程的解释。如果我未提供数据或数据有异常，请告诉我应该怎么做。\n" +
                "\n" +
                "默认配置项：\n" +
                "- 开启tooltip\n" +
                "- 如果有多条数据，开启legend";
        return new SystemMessage(prompt);
    }
}
