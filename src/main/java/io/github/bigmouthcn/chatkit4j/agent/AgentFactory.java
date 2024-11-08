package io.github.bigmouthcn.chatkit4j.agent;

import com.google.common.collect.Lists;
import com.theokanning.openai.function.FunctionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * AgentFactory 实现了 BeanPostProcessor，
 * 因此实现 {@link Agent} 接口的Bean会被扫描到，当这个Bean是直接实现{@link Agent}，或者通过继承父类实现的，第一个泛型是{@link AgentRequest}的。
 * 会被注册到这个工厂里，并且为它生成{@link FunctionDefinition 函数定义}。
 * 因此你可以通过 {@link #getFunctionDefinitions()} 来获得所有注册的函数定义。
 *
 * @author huxiao
 * @date 2024-09-12
 * @since 1.0.0
 */
@Slf4j
public class AgentFactory implements BeanPostProcessor {

    private Collection<FunctionDefinition> functionDefinitions = Lists.newArrayList();

    public Collection<FunctionDefinition> getFunctionDefinitions() {
        return functionDefinitions;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Agent<?>) {
            Agent<AgentRequest> agent = (Agent<AgentRequest>) bean;
            Class<?> clazz = ClassUtils.getUserClass(agent);

            boolean createdFun;

            // 获取父类定义
            Type genericSuperclass = clazz.getGenericSuperclass();
            createdFun = isCreatedFun(agent, genericSuperclass);

            if (!createdFun) {
                // 获取接口定义
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                for (Type genericInterface : genericInterfaces) {
                    createdFun = isCreatedFun(agent, genericInterface);
                    if (createdFun) {
                        // 如果成功创建了，那么需要结束，否则下一个接口可能无法创建而返回 false
                        break;
                    }
                }
            }

            if (!createdFun) {
                throw new BeanCreationException("Failed to create Agent definition for bean: " + clazz.getName());
            }
        }
        return bean;
    }

    private boolean isCreatedFun(Agent<AgentRequest> agent, Type superclassOrInterface) {
        if (superclassOrInterface instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclassOrInterface;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                Class<AgentRequest> reqType = (Class<AgentRequest>) actualTypeArguments[0];
                functionDefinitions.add(create(reqType, agent));
                return true;
            }
        }
        return false;
    }

    private FunctionDefinition create(Class<AgentRequest> reqClass, Agent<AgentRequest> agent) {
        String functionName = agent.getFunctionName();
        String functionDescription = agent.getFunctionDescription();
        FunctionDefinition definition = new FunctionDefinition.Builder<AgentRequest>()
                .name(functionName)
                .description(functionDescription)
                .parametersDefinitionByClass(reqClass)
                .executor(agent)
                .build();
        if (log.isInfoEnabled()) {
            log.info("Created Agent definition for bean: " + agent.getClass().getName());
        }
        return definition;
    }
}
