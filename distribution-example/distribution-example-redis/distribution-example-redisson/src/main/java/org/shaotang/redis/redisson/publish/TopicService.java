package org.shaotang.redis.redisson.publish;

import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
 
    @Autowired
    private RedissonClient redisson;
 
    public void send(String user) {
 
        if (StringUtils.isNotBlank(user)) {
            RTopic topic = redisson.getTopic("USER");
            topic.publish(user);
        }
    }
}