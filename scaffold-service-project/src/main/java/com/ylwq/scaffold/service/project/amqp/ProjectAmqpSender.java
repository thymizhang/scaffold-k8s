package com.ylwq.scaffold.service.project.amqp;

import com.ylwq.scaffold.common.amqp.ExchangeNames;
import com.ylwq.scaffold.common.amqp.QueueNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 项目消息发送者<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Component
@Slf4j
public class ProjectAmqpSender {

    final
    AmqpTemplate amqpTemplate;

    final
    RabbitTemplate rabbitTemplate;

    public ProjectAmqpSender(AmqpTemplate amqpTemplate, RabbitTemplate rabbitTemplate) {
        this.amqpTemplate = amqpTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 简单模式<br/>
     * 无需做任何配置，发送者和接收者指定相同队列名称，即可收发消息
     *
     * @param content 消息内容
     */
    public void projectUpdate(Object content) {
        /* 这里换成RabbitTemplate也可以 */
        amqpTemplate.convertAndSend(QueueNames.PROJECT_TEST, content);
        log.info("发送消息：" + QueueNames.PROJECT_TEST + " " + content);
    }

    /**
     * 直连模式，通过direct交换机路由消息<br/>
     * direct类型的交换器将所有发送到该交换器的消息被转发到RoutingKey指定的队列中，也就是说路由到BindingKey和RoutingKey完全匹配的队列中。
     */
    public void directExchangeSender(String routingKey) {
        String content = "this is a direct exchange sender.";
        this.rabbitTemplate.convertAndSend(ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT, routingKey, content);
        log.info("directExchangeSender发送消息：" + content);
    }

    /**
     * 主题模式，通过topic交换机路由消息<br/>
     * topic类型的交换机将所有发送到Topic Exchange的消息被转发到所有RoutingKey中指定的Topic的队列上面。<br/>
     * Exchange将RoutingKey和某Topic进行模糊匹配：<br/>
     * name.* : 匹配以name.开头的，后面是一个单词，比如 name.abc<br/>
     * name.# : 匹配以name.开头的，后面是任意字符串，比如 name.abc、name.abc.abc<br/>
     */
    public void topicExchangeSender(String routingKey) {
        String content = "this is a topic exchange sender.";
        this.rabbitTemplate.convertAndSend(ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC, routingKey, content);
    }

    /**
     * 广播模式，通过fanout交换机路由消息<br/>
     * fanout类型不处理路由键，会把所有发送到交换器的消息路由到所有绑定的队列中。优点是转发消息最快，性能最好。
     *
     */
    public void fanoutExchangeSender() {
        String content = "this is a fanout exchange sender.";
        this.rabbitTemplate.convertAndSend(ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT, "", content);
    }
}
