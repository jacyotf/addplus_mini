package com.addplus.server.web.rabbitmq;

import com.addplus.server.api.modelenum.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "addplus.rabbitmq_connector", havingValue = "true")
public class QueueConfig {

    @Bean(value = "logExchange")
    public TopicExchange topicExchange() {
        return new TopicExchange(QueueEnum.LOG_QUEUE.getExchange(), true, false);
    }

    @Bean(value = "logQueue")
    public Queue msmQueue() {
        return new Queue(QueueEnum.LOG_QUEUE.getQueue(), true);
    }

    @Bean(value = "bingdingLog")
    public Binding bindingMsm(Queue msmQueue, TopicExchange msmExchange) {
        return BindingBuilder.bind(msmQueue).to(msmExchange).with(QueueEnum.LOG_QUEUE.getRoutingKey());
    }

}
