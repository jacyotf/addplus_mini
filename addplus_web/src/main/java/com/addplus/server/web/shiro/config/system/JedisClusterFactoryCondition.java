package com.addplus.server.web.shiro.config.system;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 类名: JedisConnectionFactoryCondition
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/1/12 下午2:01
 * @description 类描述: 判断是否JedisClusterConnectionFactory
 */
public class JedisClusterFactoryCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String model =conditionContext.getEnvironment().getProperty("spring.redis.model");
        if("cluster".equals(model)){
            return true;
        }
        return false;
    }
}
