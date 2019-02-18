package com.ystmall.common;

import com.ystmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis 分片连接池
 * @author Shengtong Yuan
 */
public class RedisShardedPool {

    //ShardedJedis连接池->声明成static->保证在tomcat启动的时候就加载
    private static ShardedJedisPool pool;

    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));

    //jedis连接池中最大空闲状态实例数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));

    //jedis连接池中最小空闲状态实例数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));

    //在borrow一个jedis实例的时候，是否要进行验证操作，如果是true，则得到的jedis实例肯定为可用
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));

    //在return一个jedis实例的时候，是否要进行验证操作，如果是true，则放回的jedis实例肯定为可用
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));

    //redis1连接的ip地址
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    //redis1连接的端口号
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    //redis2连接的ip地址
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    //redis2连接的端口号
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));


    /**
     * 初始化Jedis连接池
     */
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽时是否阻塞。false抛出异常，true阻塞直到超时。
        config.setBlockWhenExhausted(true);

        //初始化pool
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        //设置密码
        //info1.setPassword();
        //info2.setPassword();

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        /**
         * 分片策略 Hashing.MURMUR_HASH 一致性算法
         */
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    /**
     * 静态代码块，在初始化ShardedRedisPool时初始化Jedis连接池
     */
    static{
        initPool();
    }

    /**
     * 开放ShardedJedis连接池中的ShardedJedis实例
     */
    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    /**
     * 把ShardedJedis实例放回连接池
     */
    public static void returnResource(ShardedJedis shardedJedis){
        pool.returnResource(shardedJedis);
    }

    /**
     * ShardedJedis错误连接
     */
    public static void returnBrokenResource(ShardedJedis shardedJedis){
        pool.returnBrokenResource(shardedJedis);
    }

    public static void main(String[] args) {
        ShardedJedis shardedJedis = pool.getResource();

        for(int i = 0; i < 10; i++){
            shardedJedis.set("key"+i, "value"+i);
        }

        returnResource(shardedJedis);

        System.out.println("program ends.");
    }
}
