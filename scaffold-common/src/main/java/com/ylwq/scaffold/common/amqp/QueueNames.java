package com.ylwq.scaffold.common.amqp;

/**
 * 消息队列名称<br/>
 * 使用场景：用于在消息发送者和接收者中声明和定义队列（Queue）
 * 定义原则：项目名+业务名
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class QueueNames {

    public static final String USER_ADD = "user.add";
    public static final String USER_UPDATE = "user.update";
    public static final String USER_DELETE = "user.delete";

    public static final String COMPANY_ADD = "company.add";
    public static final String COMPANY_UPDATE = "company.update";
    public static final String COMPANY_DELETE = "company.delete";

    public static final String PROJECT_ADD = "project.add";
    public static final String PROJECT_UPDATE = "project.update";
    public static final String PROJECT_DELETE = "project.delete";
    public static final String PROJECT_TEST = "project.test";

    public QueueNames() {
    }
}
