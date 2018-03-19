package com.auxiliary;

import redis.clients.jedis.Jedis;

/**
 * @auther ucmed Wenjun Choi
 * @create 2018/3/9
 */
public class RedisTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.ping());
    }
}
