package com.vnsun.base.day3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.vnsun.base.utils.ConnectionUtil;

/**
 * 消息应答
 *
 * 如果你杀死正在执行任务的某个工作进程，我们会丢失它正在处理的信息。我们还会丢失所有发送给该特定工作进程但尚未处理的消息。
 * 但是，我们不想失去任何消息。如果某个工作进程被杀死时，我们希望把这个任务交给另一个工作进程。 为了确保消息永远不会丢失，RabbitMQ 支持消息应答。
 * 从消费者发送一个确认信息告诉 RabbitMQ 已经收到消息并已经被接收和处理，然后RabbitMQ 可以自由删除它。
 * 如果消费者被杀死而没有发送应答，RabbitMQ 会认为该信息没有被完全的处理，然后将会重新转发给别的消费者。如果同时有其他消费者在线，则会迅速将其重新提供给另一个消费者
 *
 * 开启两个工作   关闭其中一个观察另一个执行情况
 *
 *
 *  *****************************************************************************************************************
 * 消息持久化
 * 如上述所说虽然我们确保消费者死去，任务也不会丢失，会交由另外的消费者来执行，但是如果rabbit服务器停止，我们的任务也会丢失
 *
 * 需要如下两件设置保证即使Rabbit服务器停止，任务也不会丢失
 *
 * 1.我们需要分别将队列和消息标记为持久化。 首先，我们需要确保 RabbitMQ 永远不会失去我们的队列。为了这样做，我们需要将其声明为持久化的
 * boolean durable = true;
 * channel.queueDeclare("hello_dirable", durable, false, false, null);
 *2.其次，我们需要标识我们的信息为持久化的。通过设置 MessageProperties 值为 PERSISTENT_TEXT_PLAIN
 *channel.basicPublish("", "hello_dirable", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
 *
 * 我们开启一个发送者发送消息，然后，关闭 RabbitMQ 服务，再重新开启，观察输出结果
 *
 *
 * 注意更改了模式之后需要删除queues  然后启动
 */
public class AckNewTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 消息持久化操作
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 发送消息
        for (int i = 0; i < 10; i++) {
            String message = "Liang:" + i;
            //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
