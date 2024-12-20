package io.github.bigmouthcn.chatkit4j.agent.charts;

import io.github.bigmouthcn.chatkit4j.agent.AgentRequest;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 */
@Data
public class GenerateEChartsOptionDataRequest implements AgentRequest {

    @JsonPropertyDescription("需要展示成图表的数据集")
    private String data;
}
