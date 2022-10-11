package org.shaotang.redis.jedis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class RedissonDemo1 {
    @Autowired
    private RedissonClient redissonClient;
    Config config;

    @Before
    public void before() {
        config = new Config();
        config.setTransportMode(TransportMode.EPOLL);
        config.useClusterServers()
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress("redis://hadoop-pseudo:16379");
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
//        RedissonClient client = Redisson.create(config);
//        RAtomicLong longObject = client.getAtomicLong("myLong");
//// 同步执行方式
//        longObject.compareAndSet(3, 401);
//// 异步执行方式
//        Boolean result = longObject.compareAndSet(3, 401);
//        System.out.println(result);

        RAtomicLong test = redissonClient.getAtomicLong("test");
        System.out.println(test.get());

//        RedissonReactiveClient client = Redisson.createReactive(config);
//        RAtomicLongReactive longObject = client.getAtomicLong('myLong');
//// 异步流执行方式
//        Mono<Boolean> result = longObject.compareAndSet(3, 401);
//        RedissonRxClient client = Redisson.createRx(config);
//        RAtomicLongRx longObject = client.getAtomicLong("myLong");
//// RxJava2方式
//        Flowable<Boolean result = longObject.compareAndSet(3, 401);
    }

    public static void main(String[] args) {
    }
}
