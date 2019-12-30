package com.vnsun.base.day7;

import com.rabbitmq.client.*;
import com.vnsun.base.utils.ConnectionUtil;

import java.io.IOException;
import java.util.Random;

/**
 * LOG INFO : *.error
 */
public class ReceiveLogsTopic2 {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String[] LOG_LEVEL_ARR = {"#", "dao.error", "*.error", "dao.*", "service.#", "*.controller.#"};

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 设置日志级别
        int rand = 2;
        String severity  = LOG_LEVEL_ARR[rand];
        // 创建一个非持久的、唯一的、自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换器和队列
        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        // 打印
        System.out.println(" [*] LOG INFO : " + severity);
        // 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
