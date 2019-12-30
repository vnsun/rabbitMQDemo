package com.vnsun.base.day7;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.vnsun.base.utils.ConnectionUtil;

import java.util.UUID;

/**
 * topic 类型的交换器 主题交换（Topic exchange）
 *
 * 不能有任意的绑定键，它必须是由点隔开的一系列的标识符组成。标识符可以是任何东西，但通常它们指定与消息相关联的一些功能
 * 其中，有几个有效的绑定键，例如 “stock.usd.nyse”， “nyse.vmw”， “quick.orange.rabbit”。可以有任何数量的标识符，最多可达 255 个字节。
 * topic 类型的交换器和 direct 类型的交换器很类似，一个特定路由的消息将被传递到与匹配的绑定键绑定的匹配的所有队列。
 */
public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String[] LOG_LEVEL_ARR = {"dao.debug", "dao.info", "dao.error",
            "service.debug", "service.info", "service.error",
            "controller.debug", "controller.info", "controller.error"};

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 发送消息
        for (String severity : LOG_LEVEL_ARR) {
            String message = "Liang-MSG log : [" +severity+ "]" + UUID.randomUUID().toString();
            // 发布消息至交换器
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();

    }
}
