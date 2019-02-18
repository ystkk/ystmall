package com.ystmall.common;

import com.ystmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 * @author Shengtong Yuan
 */
public class RedisPool {

    //jedis连接池->声明成static->保证在tomcat启动的时候就加载
    private static JedisPool pool;

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

    //redis连接的ip地址
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    //redis连接的端口号
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));


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
        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }

    /**
     * 静态代码块，在初始化RedisPool时初始化Jedis连接池
     */
    static{
        initPool();
    }

    /**
     * 开放jedis连接池中的jedis实例
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }

    /**
     * 把jedis实例放回连接池
     */
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    /**
     * jedis错误连接
     */
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("test","test");
        returnResource(jedis);

        //销毁连接池中的所有连接
        pool.destroy();
        System.out.println("program ends.");
    }

}
