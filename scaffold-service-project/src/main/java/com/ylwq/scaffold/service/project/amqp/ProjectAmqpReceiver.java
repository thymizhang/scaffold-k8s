package com.ylwq.scaffold.service.project.amqp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbitmq.client.Channel;
import com.ylwq.scaffold.common.amqp.ExchangeNames;
import com.ylwq.scaffold.common.amqp.QueueNames;
import com.ylwq.scaffold.service.project.cache.ProjectCache;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import com.ylwq.scaffold.service.project.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 项目消息接收者<br/>
 * <br/>
 * RabbitMQ Exchange类型：<br/>
 * direct Exchange：直连交换机，该交换机会完全匹配 routing key (完全一致才算匹配)，将消息转发到匹配的队列上；<br/>
 * topic Exchange：主题交换机，该交换机会模糊匹配 routing key，将消息转发到匹配的队列上；<br/>
 * fanout Exchange: 广播交换机，该交换机不管 routing key，将消息转发到所有绑定到该交换机的队列上；<br/>
 * headers Exchange：headers类型的交换机分发消息不依赖routingKey，是使用发送消息时basicProperties对象中的headers来匹配的。<br/>
 * 注意：direct Exchange、topic Exchange依赖routing key，而fanout Exchange、headers Exchange不依赖routing key<br/>
 * <br/>
 * 技术要点：<br/>
 * 1. 通过 @RabbitListener 的 bindings 属性声明 Binding（若 RabbitMQ 中不存在该绑定所需要的 Queue、Exchange、RouteKey 则自动创建，若存在则抛出异常）;<br/>
 * 2. 定义autoDelete = "true"，会在连接关闭后，自动删除交换机或队列；<br/>
 * 3. 如果不同类型的交换机绑定了同一个队列，那么监听时会造成随机相应，建议每个交换机绑定不同的队列；<br/>
 * 4. 延迟消息：通过安装RabbitMQ插件`rabbitmq_delayed_message_exchange`实现；<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Component
@Slf4j
public class ProjectAmqpReceiver {

    final
    ProjectInfoService projectInfoService;

    final
    ProjectCache projectCache;

    public ProjectAmqpReceiver(ProjectInfoService projectInfoService, ProjectCache projectCache) {
        this.projectInfoService = projectInfoService;
        this.projectCache = projectCache;
    }

    /**
     * 简单模式，指定队列即可收发消息<br/>
     * 业务说明：<br/>
     * 项目更新监听，如果项目信息更新，则更新项目所在公司的项目列表到缓存<br/>
     * 技术要点：<br/>
     * `@RabbitListener`中一定要使用queuesToDeclare声明队列，否则不会自动创建队列
     *
     * @param msg 接收的消息内容
     */
    @RabbitListener(queuesToDeclare = @Queue(QueueNames.PROJECT_TEST))
    public void projectUpdate(String msg) {
        log.info("projectUpdate收到消息：" + msg);
        /* 重新获取公司项目 */
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectInfo::getCompanyId, msg);
        List<ProjectInfo> projectInfos = projectInfoService.list(lambdaQueryWrapper);
        /* 更新到缓存 */
        if (projectInfos.size() > 0) {
            this.projectCache.pushCompanyProjects(msg, projectInfos);
        }
    }

    /**
     * 直连模式，基于订阅者消息模型（Publish/Subscribe）<br/>
     *
     * @param message 消息
     * @param channel 消息通道，如果不需要手动应答，可去掉该参数
     * @throws IOException 异常
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.PROJECT_ADD, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT, autoDelete = "true"),
                    key = "project.add"
            )
    )
    @RabbitHandler
    public void directExchangeReceiver1(Message message, Channel channel) throws IOException {
        log.info("directExchangeReceiver1 当前交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT);
        log.info("directExchangeReceiver1 当前消息队列：" + QueueNames.PROJECT_ADD);
        log.info("directExchangeReceiver1 消息：" + message.toString());
        log.info("directExchangeReceiver1 ReceivedExchange：" + message.getMessageProperties().getReceivedExchange());
        log.info("directExchangeReceiver1 ReceivedRoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("directExchangeReceiver1 DeliveryTag：" + message.getMessageProperties().getDeliveryTag());
        /* 使用@RabbitHandler手动应答消息队列 */
        String error = "error";
        if (message.getMessageProperties().getHeaders().get(error) == null) {
            /* 发送应答回执ACK */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("directExchangeReceiver1: 消息已经确认");
        } else {
            /* 如果队列出现异常，此时拒绝消息，消息会被丢弃，不会重回队列 */
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            log.info("directExchangeReceiver1: 消息拒绝");
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.PROJECT_UPDATE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT, autoDelete = "true"),
                    key = "project.update"
            )
    )
    @RabbitHandler
    public void directExchangeReceiver2(Message message, @Payload String body) {
        log.info("directExchangeReceiver2 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT);
        log.info("directExchangeReceiver2 队列：" + QueueNames.PROJECT_UPDATE);
        log.info("directExchangeReceiver2 RoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("directExchangeReceiver2 BindingKey：project.update");
        log.info("directExchangeReceiver2 消息内容：" + body);

    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.PROJECT_DELETE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT, autoDelete = "true"),
                    key = "project.delete"
            )
    )
    @RabbitHandler
    public void directExchangeReceiver3(Message message, String msg) {
        log.info("directExchangeReceiver3 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_DIRECT);
        log.info("directExchangeReceiver3 队列：" + QueueNames.PROJECT_DELETE);
        log.info("directExchangeReceiver3 RoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("directExchangeReceiver3 BindingKey：project.delete");
        log.info("directExchangeReceiver3 消息内容：" + msg);

    }

    /**
     * 主题模式，基于订阅者消息模型（Publish/Subscribe）<br/>
     * 技术要点：使用autoDelete = "true"，会在连接关闭后自动删除交换机和队列，
     *
     * @param body    消息body，通过@Payload获取，存在于Message中
     * @param headers 消息头信息，通过@Headers获取，存在于Message中
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.COMPANY_ADD, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC, type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true", autoDelete = "true"),
                    key = {"company.#", "project.#"}
            )
    )
    public void topicExchangeReceiver1(Message message, @Payload String body, @Headers Map<String, Object> headers) {
        log.info("topicExchangeReceiver1 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC);
        log.info("topicExchangeReceiver1 队列：" + QueueNames.COMPANY_ADD);
        log.info("topicExchangeReceiver1 RoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("topicExchangeReceiver1 BindingKey：company.# project.#");
        /* 使用 @Payload 和 @Headers 注解可以消息中的 body 与 headers 信息 */
        log.info("topicExchangeReceiver1 消息内容：" + body);
        log.info("topicExchangeReceiver1 消息头：" + headers);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.COMPANY_UPDATE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC, type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true", autoDelete = "true"),
                    key = {"#.name"}
            )
    )
    public void topicExchangeReceiver2(Message message) {
        log.info("topicExchangeReceiver2 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC);
        log.info("topicExchangeReceiver2 队列：" + QueueNames.COMPANY_UPDATE);
        log.info("topicExchangeReceiver2 RoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("topicExchangeReceiver2 BindingKey：#.name");
        log.info("topicExchangeReceiver2 消息：" + message.toString());
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.COMPANY_DELETE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC, type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true", autoDelete = "true"),
                    key = {"project.*"}
            )
    )
    public void topicExchangeReceiver3(Message message, String msg) {
        log.info("topicExchangeReceiver3 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_TOPIC);
        log.info("topicExchangeReceiver3 队列：" + QueueNames.COMPANY_DELETE);
        log.info("topicExchangeReceiver3 RoutingKey：" + message.getMessageProperties().getReceivedRoutingKey());
        log.info("topicExchangeReceiver3 BindingKey：project.*");
        log.info("topicExchangeReceiver3 消息内容：" + msg);
    }

    /**
     * 广播模式，基于订阅者消息模型（Publish/Subscribe）<br/>
     * 技术要点：在fanout模式下，无需定义binding key，因为fanout会忽略。
     *
     * @param message 消息
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.USER_ADD, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT, type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true", autoDelete = "true")
            )
    )
    public void fanoutExchangeReceiver1(Message message) {
        log.info("fanoutExchangeReceiver1 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT);
        log.info("fanoutExchangeReceiver1 队列：" + QueueNames.USER_ADD);
        log.info("fanoutExchangeReceiver1 消息内容：" + message.toString());
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.USER_UPDATE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT, type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true", autoDelete = "true")
            )
    )
    public void fanoutExchangeReceiver2(@Payload String body) {
        log.info("fanoutExchangeReceiver2 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT);
        log.info("fanoutExchangeReceiver2 队列：" + QueueNames.USER_UPDATE);
        log.info("fanoutExchangeReceiver2 消息内容：" + body);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QueueNames.USER_DELETE, durable = "true", autoDelete = "true"),
                    exchange = @Exchange(value = ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT, type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true", autoDelete = "true")
            )
    )
    public void fanoutExchangeReceiver3(String msg) {
        log.info("fanoutExchangeReceiver3 交换机：" + ExchangeNames.SCAFFOLD_EXCHANGE_FANOUT);
        log.info("fanoutExchangeReceiver3 队列：" + QueueNames.USER_DELETE);
        log.info("fanoutExchangeReceiver3 消息内容：" + msg);
    }
}
