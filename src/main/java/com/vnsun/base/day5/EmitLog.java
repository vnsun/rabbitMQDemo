package com.vnsun.base.day5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vnsun.base.utils.ConnectionUtil;

/**
 * 发布订阅
 *
 * 交换器
 * fanout 将所有收到的消息广播到所有它所知道的队列
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        // 指定一个交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 发送消息
        String message = "Liang-MSG log.";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
