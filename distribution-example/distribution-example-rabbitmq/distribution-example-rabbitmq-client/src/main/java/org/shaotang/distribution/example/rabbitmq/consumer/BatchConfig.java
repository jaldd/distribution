package org.shaotang.distribution.example.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component("rabbitMqCustomerConfig")
public class BatchConfig {
    @Value("${env:prod}")
    private String env;
    @Autowired
    SimpleRabbitListenerContainerFactory containerFactory;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void simpleListenerBatchInit() {
        log.info("设置批量-----");
        containerFactory.setBatchListener(true);
        if ("prod".equals(env)) {
            // 依照不同的环境进行开启
            containerFactory.setAutoStartup(true);
        }


        // 设置 ConfirmCallback 回调函数 确认消息是否成功发送到 Exchang
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                if (null == correlationData) {
                    // 延迟消息 correlationData 为null
                    return;
                }
                log.debug("Message sent successfully:{} ", correlationData.getId());

            } else {
                if (null == correlationData && null == cause) {
                    // 延迟消息 correlationData 为null
                    return;
                }
                log.error("Message sent failed: {}", correlationData.getId() + ", cause: " + cause);
            }
        });
        // ReturnCallback  处理的是未路由的消息返回的情况
        rabbitTemplate.setReturnCallback((oneMessage, replyCode, replyText, exchange, routingKey) -> {
            // 判断是否是延迟消息
            if (routingKey.indexOf("delay") != -1) {
                // 是一个延迟消息，忽略这个错误提示
                return;
            }
            log.debug("Message returned: {}", new String(oneMessage.getBody()) + ", replyCode: " + replyCode + ", replyText: " + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey);
        });

    }


}