package org.shaotang.caffeine;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class TestCaffeineCache {


    public static void main(String[] args) {
        AsyncCache<Integer, Integer> asyncCache = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(2)
                .expireAfterAccess(Duration.ofSeconds(3))
                .buildAsync();

        Mono<Integer> mono1 = getMono1(asyncCache,1);
        Object block1 = mono1.block();
        System.out.println("block1:" + block1);
        Mono<Integer> mono2 = getMono1(asyncCache,2);
        Object block2 = mono2.block();
        System.out.println("block2:" + block2);
        Mono<Integer> mono3 = getMono1(asyncCache,3);
        Object block3 = mono3.block();
        System.out.println("block3:" + block3);
        Mono<Integer> mono11 = getMono1(asyncCache,1);
        Object block11 = mono11.block();
        System.out.println("block11:" + block11);
        Mono<Integer> mono21 = getMono1(asyncCache,2);
        Object block21 = mono21.block();
        System.out.println("block21:" + block21);
        Mono<Integer> mono31 = getMono1(asyncCache,3);
        Object block31 = mono31.block();
        System.out.println("block31:" + block31);
    }

    private static Mono<Integer> getMono1(AsyncCache<Integer, Integer> asyncCache,int key) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        Mono<Integer> mono1 = Mono.fromFuture(asyncCache.get(key, k -> null)).switchIfEmpty(Mono.just(key)
                .doOnNext(val -> asyncCache.put(key, Mono.just(val).toFuture())));
        return mono1;
    }


}
