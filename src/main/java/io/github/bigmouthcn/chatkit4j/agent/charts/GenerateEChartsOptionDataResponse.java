package io.github.bigmouthcn.chatkit4j.agent.charts;

import io.github.bigmouthcn.chatkit4j.agent.AgentResponse;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2024/9/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class GenerateEChartsOptionDataResponse implements AgentResponse {

    @JsonPropertyDescription("是否成功")
    private boolean success;

    @JsonPropertyDescription("图表Option")
    private String echartsOption;
}
