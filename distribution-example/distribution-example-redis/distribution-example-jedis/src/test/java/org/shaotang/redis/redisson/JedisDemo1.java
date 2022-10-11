package org.shaotang.redis.redisson;

import redis.clients.jedis.Jedis;

public class JedisDemo1 {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("hadoop-pseudo",16379);
        String value = jedis.ping();
        System.out.println(value);
    }
}
