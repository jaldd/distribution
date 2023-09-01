package org.shaotang.distribution.example.rabbitmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.shaotang.distribution.example.rabbitmq.model.MsgDto;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

@Slf4j
@Component
public class MessageProducer {
    // 这里可以指定一个默认发送使用的交换机
    @Value("${amqp-binding.exchange-name:my_exchange}")
    private String exchangeName;


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("txRabbitTemplat")
    private RabbitTemplate txRabbitTemplate;


    /**
     * 指定的routKey 发送信息
     *
     * @param message
     */
    public void sendMessage(String routKey, Object message) {
        this.sendMessage(exchangeName, routKey, JSONObject.toJSONString(message));
    }

    /**
     * 通过交换机，路由key 发送消息
     *
     * @param exchangeName
     * @param routKey
     * @param message
     */
    public void sendMessage(String exchangeName, String routKey, Object message) {
        // 设置消息的唯一标识符
        long deliveryTag = System.currentTimeMillis();
        rabbitTemplate.convertAndSend(exchangeName, routKey, message, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setMessageId(String.valueOf("messageId_" + deliveryTag));
            return messagePostProcessor;
        }, new CorrelationData(UUID.randomUUID().toString()));

    }


    /**
     * 指定的routKey 发送批量信息
     *
     * @param messages
     */
    public void sendMessageBatch(String routKey, MsgDto messages) {
        this.sendMessageBatch(exchangeName, routKey, JSONObject.toJSONString(messages));
    }

    /**
     * 通过交换机，路由key 发送批量信息
     *
     * @param exchangeName
     * @param routKey
     * @param messages
     */
    public void sendMessageBatch(String exchangeName, String routKey, Object messages) {
        rabbitTemplate.convertSendAndReceive(exchangeName, routKey, messages, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setMessageId(String.valueOf("messageId_" + 1));
            return messagePostProcessor;
        }, new CorrelationData(UUID.randomUUID().toString()));
    }

    /**
     * 指定的routKey 发送信息
     *
     * @param message
     */
    public void sendDelayMessage(String routKey, Object message, long delayTime) {
        this.sendDelayMessage(exchangeName, routKey, message, delayTime);
    }

    /**
     * 指定的routKey 发送延迟信息
     *
     * @param message
     */
    public void sendDelayMessage(String exchangeName, String routKey, Object message, long delayTime) {
        log.debug("producer send delay message:{}", message);
        rabbitTemplate.convertAndSend(exchangeName, routKey, message, header -> {
            header.getMessageProperties().setHeader("x-delay", delayTime);
            return header;
        });
    }

    /**
     * 指定的routKey 发送事务信息
     *
     * @param message
     */
    @SneakyThrows
    public void sendTxMessage(String exchangeName, String routKey, Object message) {
        log.debug("producer send delay message:{}", message);
        String messageStr = JSONObject.toJSONString(message);
        // method 1：
//        sendTransactedMsgByNewChannel(exchangeName,routKey,message);
        // method2:
        sendTransactedMsgByNTemplate(exchangeName, routKey, messageStr);


    }

    private void sendTransactedMsgByNTemplate(String exchangeName, String routKey, String message) {
        txRabbitTemplate.execute(channel -> {
            try {
                String messageId = UUID.randomUUID().toString() + "_messageId";
                String correlationId = UUID.randomUUID().toString() + "_correId";

                // 创建 BasicProperties 对象并设置属性
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .messageId(messageId)
                        .correlationId(correlationId)
                        .build();
                channel.txSelect(); // 开启事务

                channel.basicPublish(exchangeName, routKey, properties, message.getBytes(Charset.forName("UTF-8"))); // 发送消息
//                "124".substring(7);
                channel.txCommit(); // 提交事务
            } catch (Exception e) {
                channel.txRollback(); // 回滚事务
            }
            return true;
        });
    }

    @SneakyThrows
    private void sendTransactedMsgByNewChannel(String exchangeName, String routKey, String message) {
        // 获取新的channel 对象
        Channel channel = txRabbitTemplate.getConnectionFactory().createConnection().createChannel(true);
        // 开启事务
        channel.txSelect();
        try {
            // 消息格式化
            channel.basicPublish(exchangeName, routKey, null, message.getBytes(Charset.forName("UTF-8")));
            // 消息提交
            channel.txCommit();
        } catch (IOException e) {
            channel.txRollback();
            throw e;
        }
    }


}