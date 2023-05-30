package org.shaotang.distribution.example.kafka.web;

import cn.hutool.json.JSONUtil;
import org.shaotang.distribution.example.kafka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    KafkaTemplate<String, String> kafka;

    //调用http://127.0.0.1:8001/register 测试
    @RequestMapping("register")
    public String register(User user) {
        String message = JSONUtil.toJsonStr(user);
        System.out.println("接收到用户信息：" + message);
        kafka.send("register", message);
        //kafka.send(String topic, @Nullable V data) {
        return "OK";
    }
}