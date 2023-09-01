package org.shaotang.distribution.example.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RabbitRoutKeyEnum {

    业务_单条消息("my_routKey"),
    业务_多条消息("my_batch_routKey"),
    业务_1("my_one_routKey"),
    业务_延迟("my_delay_routKey"),

    业务_事务("my_tx_routKey"),

    ;

    private String routKey;


}