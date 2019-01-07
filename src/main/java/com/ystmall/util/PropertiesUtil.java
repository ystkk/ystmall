package com.ystmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    //当这个类被javaloader加载的时候，jvm会先执行static静态代码块
    //加载进ystmall.properties文件
    static{
        String fileName = "ystmall.properties";
        props = new Properties();
        try{
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8));
        } catch (IOException e){
            logger.error("配置文件读取异常", e);
        }
    }

    //获取某一配置的值
    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    //获取某一配置的值，带有默认值
    public static String getProperty(String key, String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }



}
