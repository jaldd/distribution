package org.shaotang.redis.jedis.listener;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TopicListener implements ApplicationRunner, Ordered {

    @Autowired
    private RedissonClient redisson;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        RTopic topic = redisson.getTopic("USER");
        topic.addListener(String.class, (charSequence, user) -> log.info("Redisson监听器收到消息:{}", user));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}