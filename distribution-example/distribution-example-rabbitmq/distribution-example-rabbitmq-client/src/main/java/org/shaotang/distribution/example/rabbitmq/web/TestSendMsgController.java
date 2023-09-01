package org.shaotang.distribution.example.rabbitmq.web;

import org.shaotang.distribution.example.rabbitmq.consumer.MessageProducer;
import org.shaotang.distribution.example.rabbitmq.model.MsgDto;
import org.shaotang.distribution.example.rabbitmq.model.RabbitRoutKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestSendMsgController {
    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/sendMsg")
    public boolean sendMsg(@RequestParam String content, @RequestParam String routKey) {
        List<Object> msgs = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            msgs.add(content + "_" + i);

        }
        msgs.stream().forEach(e -> {
            MsgDto msgDto = new MsgDto("user", e);
            messageProducer.sendMessage(RabbitRoutKeyEnum.业务_单条消息.getRoutKey(), msgDto);
        });

        return true;
    }

    @GetMapping("/sendBatchMsg")
    public boolean sendBatchMsg(@RequestParam String content, @RequestParam String routKey) {
        List<Object> msgs = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            msgs.add(content + "_" + i);
        }
        MsgDto msgDto = new MsgDto("test", msgs);
        messageProducer.sendMessageBatch(RabbitRoutKeyEnum.业务_多条消息.getRoutKey(), msgDto);

        return true;
    }

    @GetMapping("/sendDelayMsg")
    public boolean sendDelayMsg(@RequestParam String content, @RequestParam long delayTime) {
        List<Object> msgs = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            msgs.add(content + "_" + i);
        }
        msgs.stream().forEach(e -> {
            messageProducer.sendDelayMessage("my_delay_exchange", RabbitRoutKeyEnum.业务_延迟.getRoutKey(), e, delayTime);
        });

        return true;
    }

    @GetMapping("/sendTxMsg")
    public boolean sendTxMsg(@RequestParam String content) {
        List<Object> msgs = new ArrayList<>(10);
        for (int i = 0; i < 2; i++) {
            msgs.add(content + "_" + i);
        }
        msgs.stream().forEach(e -> {
            MsgDto msgDto = new MsgDto("tx", e);
            messageProducer.sendTxMessage("my_tx_exchange", RabbitRoutKeyEnum.业务_事务.getRoutKey(), msgDto);
//            messageProducer.sendMessage(RabbitRoutKeyEnum.业务_单条消息.getRoutKey(),msgDto);
        });


        return true;
    }
}
