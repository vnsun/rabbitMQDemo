package com.vnsun.base.day1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.vnsun.base.utils.ConnectionUtil;

/**
 * 发送端
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建一个链接
        Connection con = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = con.createChannel();
        // 指定一个队列
        // queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        // 参数1 queue ：队列名
        // 参数2 durable ：是否持久化
        // 参数3 exclusive ：仅创建者可以使用的私有队列，断开后自动删除
        // 参数4 autoDelete : 当所有消费客户端连接断开后，是否自动删除队列
        // 参数5 arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 设置发送的消息
        String message = "Hello,World!";
        // basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        // 参数1 exchange ：交换器
        // 参数2 routingKey ： 路由键
        // 参数3 props ： 消息的其他参数
        // 参数4 body ： 消息体
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Send" + message);
        //关闭通道和链接
        channel.close();
        con.close();

    }
}
