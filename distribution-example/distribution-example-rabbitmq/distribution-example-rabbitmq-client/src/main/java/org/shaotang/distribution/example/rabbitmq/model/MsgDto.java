package org.shaotang.distribution.example.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MsgDto implements Serializable {
    // 消息类型
    private String msgType;
    // 消息体
    private Object body;
}