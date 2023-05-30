package org.shaotang.distribution.example.kafka.consumer;

import cn.hutool.json.JSONUtil;
import org.shaotang.distribution.example.kafka.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class Consumer {

    @KafkaListener(topics = "register")
    public void consume(String message) {
        System.out.println("接收到消息：" + message);
        User user = JSONUtil.toBean(message, User.class);
        System.out.println("正在为 " + user.getName() + " 办理注册业务...");
        System.out.println("注册成功");
    }
}