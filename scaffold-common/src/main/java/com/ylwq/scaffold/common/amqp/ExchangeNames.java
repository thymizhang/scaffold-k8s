package com.ylwq.scaffold.common.amqp;

/**
 * 交换机名称<br/>
 * 使用场景：用于在消息发送者和接收者中声明和定义交换机（Exchange）<br/>
 * 定义原则：项目名+交换机类型
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public abstract class ExchangeNames {

    public static final String SCAFFOLD_EXCHANGE_DIRECT = "scaffold.direct";
    public static final String SCAFFOLD_EXCHANGE_TOPIC = "scaffold.topic";
    public static final String SCAFFOLD_EXCHANGE_FANOUT = "scaffold.fanout";

    public ExchangeNames() {
    }
}
