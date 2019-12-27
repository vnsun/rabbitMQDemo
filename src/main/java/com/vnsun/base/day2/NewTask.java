package com.vnsun.base.day2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vnsun.base.utils.ConnectionUtil;

/**
 * 工作队列 轮询
 */
public class NewTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发送消息  发送10个消息
        for (int i = 0; i < 10; i++) {
            String message = "Liang:" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
