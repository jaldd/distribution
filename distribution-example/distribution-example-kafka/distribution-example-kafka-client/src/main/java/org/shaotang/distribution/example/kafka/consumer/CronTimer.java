package org.shaotang.distribution.example.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @author およそ神
 * @version JDK 1.8
 * 定时启动、停止监听器
 */
@EnableScheduling
@Component
public class CronTimer {
    //    /**
//     * @KafkaListener注解所标注的方法并不会在IOC容器中被注册为Bean，
//     * 而是会被注册在KafkaListenerEndpointRegistry中，
//     * 而KafkaListenerEndpointRegistry在SpringIOC中已经被注册为Bean
//     **/
    @Autowired
    private KafkaListenerEndpointRegistry registry;
    @Autowired
    private ConsumerFactory consumerFactory;
//    // 监听器容器工厂(设置禁止KafkaListener自启动)
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
//        container.setConsumerFactory(consumerFactory);
//        //禁止KafkaListener自启动
//        container.setAutoStartup(false);
//        return container;
//    }
//    // 监听器
//    @KafkaListener(id="timingConsumer",topics = "topic1",containerFactory = "delayContainerFactory")
//    public void onMessage1(ConsumerRecord<?, ?> record){
//        System.out.println("消费成功："+record.topic()+"-"+record.partition()+"-"+record.value());
//    }
//    // 定时启动监听器  11点03分启动
//    @Scheduled(cron = "0 7 11 * * ? ")
//    public void startListener() {
//        System.out.println("启动监听器...");
//        // "timingConsumer"是@KafkaListener注解后面设置的监听器ID,标识这个监听器
//        if (!registry.getListenerContainer("timingConsumer").isRunning()) {
//            registry.getListenerContainer("timingConsumer").start();
//        }
//        //registry.getListenerContainer("timingConsumer").resume();
//    }
//    // 定时停止监听器 11点04分关闭
//    @Scheduled(cron = "0 8 11 * * ? ")
//    public void shutDownListener() {
//        System.out.println("关闭监听器...");
//        registry.getListenerContainer("timingConsumer").pause();
//    }


    @KafkaListener(topics = "register-topic")
    public void receive(final String topic) {

        TopicPartitionOffset topicPartitionOffset = new TopicPartitionOffset(
                topic, 1);

        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener((MessageListener<Object, String>) message -> {

            Object key = message.key();
            String value = message.value();
            System.out.println(key);
            System.out.println(value);
            // process my message
        });


        KafkaMessageListenerContainer<Object, String> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.start();
    }

}