package com.ystmall.util;

import com.ystmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 封装redis api
 * @author Shengtong Yuan
 */
//使用lombok增加log
@Slf4j
public class RedisPoolUtil {

    /**
     * 设置kv值
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e){
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置过期时间kv值
     * @param key
     * @param exTime 单位是秒
     * @param value
     * @return
     */
    public static String setEx(String key, int exTime, String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e){
            log.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 重新设置有效期
     * @param key
     * @param exTime 单位是秒
     * @return
     */
    public static Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e){
            log.error("expire key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 获取kv值
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e){
            log.error("get key:{} error", key);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除kv值
     * @param key
     * @return
     */
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e){
            log.error("del key:{} error", key);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }

        RedisPool.returnResource(jedis);
        return result;
    }

}
