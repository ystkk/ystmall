package com.ystmall.common;

import com.ystmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redisson初始化
 * @author Shengtong Yuan
 */
@Component
@Slf4j
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    //redis1连接的ip地址
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    //redis1连接的端口号
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    //redis2连接的ip地址
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    //redis2连接的端口号
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(redis1Port).toString());
            redisson = (Redisson) Redisson.create(config);

            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.error("redisson init error", e);
            e.printStackTrace();
        }
    }
}
