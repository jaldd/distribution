package org.shaotang.distribution.example.kafka.web;

import cn.hutool.json.JSONUtil;
import org.shaotang.distribution.example.kafka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class ProducerController {

    @Autowired
    KafkaTemplate<String, String> kafka;
    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    //调用http://127.0.0.1:8001/register 测试
    @RequestMapping("register")
    public String register(User user) {
        String message = JSONUtil.toJsonStr(user);
        System.out.println("接收到用户信息：" + message);
        kafka.send("register", message);

        //kafka.send(String topic, @Nullable V data) {
        return "OK";
    }

    //调用http://127.0.0.1:8001/register/topic 测试
    @RequestMapping("register/topic/{topic}")
    public String registerTopic(@PathVariable("topic") String topic) {
//        String message = JSONUtil.toJsonStr(user);
//        System.out.println("接收到用户信息：" + message);
//        kafka.send("register", message);
//        //kafka.send(String topic, @Nullable V data) {

//        registry.registerListenerContainer();

        kafka.send("register-topic", topic);
        return "OK";
    }


    //调用http://127.0.0.1:8001/register/topic 测试
    @RequestMapping("test/topic/{topic}/{message}")
    public String registerTopic(@PathVariable("topic") String topic, @PathVariable("message") String message) {
//        String message = JSONUtil.toJsonStr(user);
//        System.out.println("接收到用户信息：" + message);
//        kafka.send("register", message);
//        //kafka.send(String topic, @Nullable V data) {

//        registry.registerListenerContainer();

        kafka.send(topic, message);
        return "OK";
    }
    @Autowired
    private ConsumerFactory consumerFactory;

    //调用http://127.0.0.1:8001/register/topic 测试
    @RequestMapping("listListeners")
    public String listListeners() {

        List listeners = consumerFactory.getListeners();
        Collection<MessageListenerContainer> listenerContainers = kafkaListenerEndpointRegistry.getListenerContainers();
        return "OK";
    }

}