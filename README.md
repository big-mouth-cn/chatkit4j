# ChatKit4J
ChatKit for Java，是一个可快速对接AI接口的Java开发框架，你不需要关心如何与AI服务商（如OpenAI）对接，只需要将ChatKit4J接入到你的应用程序里，开箱即用。

## Features
- 内置 `/v1/chat/completions` 接口，可通过第三方客户端直接使用；
- 通过实现 `Agent` 接口来快速创建一个 Agent，LLM 会在合适的时机调用它。

# Quick Start
## Import

### Gradle
```angular2html
implementation 'io.github.bigmouthcn:chatkit4j:1.0.0'
```

### Maven
```angular2html
<dependency>
    <groupId>io.github.bigmouthcn</groupId>
    <artifactId>chatkit4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration
| Property                                       | Default                          | Description  |
|:-----------------------------------------------|:---------------------------------|:-------------|
| `chatkit4j.web.async-task-core-pool-size`      | `200`                            | Chat接口核心线程数  |
| `chatkit4j.web.async-task-max-pool-size`  | `200`                            | Chat接口最大线程数  |
| `chatkit4j.web.async-task-queue-capacity` | `1000`                           | Chat接口队列大小   |
| `chatkit4j.web.async-request-timeout`     | `5m`                             | Chat接口最大请求时间 |
| `chatkit4j.openai.base-url`           | `https://api.aigateway.work/v1/` | LLM服务器地址     |
| `chatkit4j.openai.access-key`         |                                  | LLM服务器接口密钥   |
| `chatkit4j.openai.timeout`            | `5m`                             | LLM服务器超时时间   |

## with SpringBoot
```yaml
server:
  port: 8294
```

```java
@SpringBootApplication
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}
```

```shell
curl http://127.0.0.1:8294/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gpt-3.5-turbo",
    "messages": [
      {
        "role": "system",
        "content": "You are a helpful assistant."
      },
      {
        "role": "user",
        "content": "Hello!"
      }
    ]
  }'
```

![iShot_2024-11-01_16.34.45.png](docs%2FiShot_2024-11-01_16.34.45.png)

## other examples

### 创建一个`使用JavaScript计算列式`的Agent
- 创建一个Agent请求对象
```java
@Data
public class ExpressionEvalRequest implements AgentRequest {

    @JsonPropertyDescription("需要执行计算的列式")
    @JsonProperty(required = true)
    private String expression;
}
```
- 创建一个Agent
```java
@Slf4j
@Configuration
public class ExpressionEvalFunction implements Agent<ExpressionEvalRequest> {

    @Override
    public String getFunctionName() {
        return "calculated_expression";
    }

    @Override
    public String getFunctionDescription() {
        return "执行计算列式，得到计算结果";
    }

    @Override
    public Object apply(ExpressionEvalRequest expressionEvalRequest) {
        try {
            String expression = expressionEvalRequest.getExpression();
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return engine.eval(expression);
        } catch (ScriptException e) {
            log.error("Expression eval error: {}", e.getMessage());
        }
        return null;
    }
}
```

- 测试
```shell
curl http://127.0.0.1:8294/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gpt-3.5-turbo",
    "messages": [
      {
        "role": "system",
        "content": "You are a helpful assistant."
      },
      {
        "role": "user",
        "content": "帮我算一下：58加22乘以400除以20等于多少？"
      }
    ]
  }'
```

```
[2024-11-01 16:57:13.051] [INFO] 52510 [nio-8294-exec-7] [c.b.c.controller.CopilotController] : execution tool: calculated_expression - {"expression":"58 + 22 * 400 / 20"}
[2024-11-01 16:57:13.164] [INFO] 52510 [nio-8294-exec-7] [c.b.c.controller.CopilotController] : executed: 498
```

![iShot_2024-11-01_16.58.28.png](docs%2FiShot_2024-11-01_16.58.28.png)

# FAQs
- 如何自定义Agent？  
创建一个实现`Agent`接口的类，并使用`@Component`注解将其注册到Spring容器中。

- 为什么Agent没有被调用？  
确保配置的AI服务商是支持FunctionCall的，并且接口协议和OpenAI的接口协议一致。