package com.addplus.server.api.modelenum;

/**
 * 类名: QueueEnum
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/9/27 下午12:06
 * @description 类描述: RabbitMQ队列初始化枚举类
 */
public enum QueueEnum {
    /**
     * 日志处理队列
     */
    LOG_QUEUE("log_exchange", "log_queue", "log_routingkey");

    QueueEnum(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    private String exchange;

    private String queue;

    private String routingKey;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
