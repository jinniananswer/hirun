package com.most.core.app.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by pc on 2018-06-05.
 */
public class RedisFactory {

    private static JedisPool jedisPool = null;

    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(3);
        //等待可用连接的最大时间
        config.setMaxWaitMillis(10000);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool("10.133.17.79",6379);
    }

    //获取Jedis实例
    public synchronized static Jedis getJedis(){
        if(jedisPool != null){
            Jedis jedis = jedisPool.getResource();
            return jedis;
        }else{
            return null;
        }
    }
}
