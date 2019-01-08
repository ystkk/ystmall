package com.ystmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * TokenCache 用于重置密码的token验证
 * @author Shengtong Yuan
 */
public class TokenCache {
    
    private static Logger logger= LoggerFactory.getLogger(TokenCache.class);

    //声明静态内存块. guava中的本地缓存
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder()
            //initialCapacity(1000)初始化容量为1000
            .initialCapacity(1000)
            //maximumSize(100000)当超过最大量10000，使用LRU算法移除缓存项
            .maximumSize(100000)
            //expireAfterAccess(12,TimeUnit.HOURS)有效期12小时
            .expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载，
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (Exception e){
            logger.error("localCache get error", e);
        }
        return null;
    }
}
