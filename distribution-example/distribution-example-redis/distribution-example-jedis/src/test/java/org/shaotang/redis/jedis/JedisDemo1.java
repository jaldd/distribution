package org.shaotang.redis.jedis;

import redis.clients.jedis.Jedis;

public class JedisDemo1 {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("hadoop-pseudo",16379);
        String value = jedis.ping();
        System.out.println(value);
        jedis.set("foo", "bar");
        System.out.println(jedis.get("foo"));
    }
}