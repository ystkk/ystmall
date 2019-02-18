package com.ystmall.util;

import com.ystmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * 封装redis api
 * @author Shengtong Yuan
 */
//使用lombok增加log
@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设置kv值
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value){
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.set(key,value);
        } catch (Exception e){
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
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
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.setex(key,exTime,value);
        } catch (Exception e){
            log.error("setex key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * 分布式定时关单，如果锁不存在才会设置成功
     * @param key
     * @param value
     * @return
     */
    public static Long setnx(String key, String value){
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.setnx(key,value);
        } catch (Exception e){
            log.error("setnx key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * 重新设置有效期
     * @param key
     * @param exTime 单位是秒
     * @return
     */
    public static Long expire(String key, int exTime){
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.expire(key, exTime);
        } catch (Exception e){
            log.error("expire key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * 具有原子性的设置值返回值
     * @param key
     * @param value
     * @return
     */
    public static String getSet(String key, String value){
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.getSet(key,value);
        } catch (Exception e){
            log.error("getset key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * 获取kv值
     * @param key
     * @return
     */
    public static String get(String key){
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.get(key);
        } catch (Exception e){
            log.error("get key:{} error", key);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * 删除kv值
     * @param key
     * @return
     */
    public static Long del(String key){
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getJedis();
            result = shardedJedis.del(key);
        } catch (Exception e){
            log.error("del key:{} error", key);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

}
