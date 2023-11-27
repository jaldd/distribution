package org.shaotang.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestCache {

    //手动加载
    @Test
    public void test1() throws InterruptedException {
        // 初始化缓存，设置了100的缓存最大个数
        Cache<Integer, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(600, TimeUnit.SECONDS)
                .refreshAfterWrite(300, TimeUnit.SECONDS)
                .build(this::getData);
        int key1 = 1;
        // 使用getIfPresent方法从缓存中获取值。如果缓存中不存指定的值，则方法将返回 null：
        System.out.println(cache.getIfPresent(key1));
        // 也可以使用 get 方法获取值，该方法将一个参数为 key 的 Function 作为参数传入。如果缓存中不存在该 key
        // 则该函数将用于提供默认值，该值在计算后插入缓存中：
        System.out.println(cache.get(key1, integer -> "a"));
        cache.put(key1, "b");
        // 校验key1对应的value是否插入缓存中
        System.out.println(cache.getIfPresent(key1));
        // 移除数据，让数据失效
        cache.invalidate(key1);
        System.out.println(cache.getIfPresent(key1));
    }

    private String getData(Integer key) {

        System.out.println("in normal func");
        return key + "_value";
    }


    @Test
    public void testAutoRefresh() throws InterruptedException {
        Cache<Integer, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
//                .expireAfterAccess(2, TimeUnit.SECONDS)
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                // 增加了淘汰监听
                .removalListener(((key, value, cause) -> System.out.println("淘汰通知，key：" + key + "，原因：" + cause)))
                .build(this::getData);

        String ifPresent = cache.getIfPresent(1);
        System.out.println("first:" + ifPresent);

        cache.put(1, "1");
        System.out.println("second:" + cache.getIfPresent(1));
        Thread.sleep(1500);

        System.out.println("three:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("four:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("five:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("six:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("seven:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("eight:" + cache.getIfPresent(1));
        Thread.sleep(1000);
        System.out.println("nine:" + cache.getIfPresent(1));
        Thread.sleep(3000);
        System.out.println("ten:" + cache.getIfPresent(1));
    }


    @Test
    public void testNullKey() throws InterruptedException {
        Cache<Integer, Boolean> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(2, TimeUnit.SECONDS)
//                .expireAfterWrite(2, TimeUnit.SECONDS)
                .removalListener(((key, value, cause) -> System.out.println("淘汰通知，key：" + key + "，原因：" + cause)))
                .build();

        Boolean ifPresent = cache.getIfPresent(1);
        System.out.println("first:" + ifPresent);

        cache.put(1, true);
        System.out.println("second:" + cache.getIfPresent(1));
        Thread.sleep(1500);

        System.out.println("three:" + cache.getIfPresent(1));
        Thread.sleep(500);
        System.out.println("four:" + cache.getIfPresent(1));
        Thread.sleep(500);
        System.out.println("five:" + cache.getIfPresent(1));
    }

    public static void main(String[] args) {


    }
}
