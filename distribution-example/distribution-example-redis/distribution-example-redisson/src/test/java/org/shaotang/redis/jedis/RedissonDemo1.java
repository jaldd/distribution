package org.shaotang.redis.jedis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class RedissonDemo1 {
    @Autowired
    private RedissonClient redissonClient;
    Config config;

    @Before
    public void before() {
        config = new Config();
//        config.setTransportMode(TransportMode.EPOLL);
        config.useSingleServer().setAddress("redis://hadoop-pseudo:16379");
    }

    @Test
    public void test1() throws ExecutionException, InterruptedException {
        RedissonClient client = Redisson.create(config);
        RAtomicLong longObject = client.getAtomicLong("myLong");
// 同步执行方式
        longObject.compareAndSet(3, 401);
// 异步执行方式
        Boolean result = longObject.compareAndSet(3, 401);
        System.out.println(result);
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

    @Test
    public void testLock() throws InterruptedException {
        config.setLockWatchdogTimeout(30000);
        RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("anyLock");
//        lock.lock(10, TimeUnit.SECONDS);

// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        log.info("res:" + res);
        if (res) {
            try {
                Thread.sleep(21000);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
    }
}
