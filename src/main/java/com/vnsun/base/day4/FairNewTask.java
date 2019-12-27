package com.vnsun.base.day4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vnsun.base.utils.ConnectionUtil;

/**
 * 公平转发
 *
 * 两个工作线程的情况下，一个非常忙碌，一个比较空闲。而RabbitMQ还是会平均分配消息，为了解决这样的问题可以使用basicQos 方法并将传递参数为prefetchCount = 1
 * 这样告诉 RabbitMQ 不要一次给一个工作线程多个消息。换句话说，在处理并确认前一个消息之前，不要向工作线程发送新消息。相反，它将发送到下一个还不忙的工作线程
 */
public class FairNewTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 公平转发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        // 发送消息
        for (int i = 10; i >0; i--) {
            String message = "Liang:" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
