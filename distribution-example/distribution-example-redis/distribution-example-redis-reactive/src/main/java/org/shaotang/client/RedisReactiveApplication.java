package org.shaotang.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RedisReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisReactiveApplication.class, args);
    }


    @RestController
    class RedisRestController {

        private StringRedisTemplate template;


    }
}
