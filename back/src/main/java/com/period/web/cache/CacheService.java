package com.period.web.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 缓存服务
 * Created by admin on 16/8/18.
 */
@Component
public class CacheService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;

    /**
     * 设置缓存对象
     * @param key
     * @param value
     */
    public void set(String key,String value){
        valueOperations.set(key, value);
    }

    /**
     * 获取缓存对象
     * @param key
     * @return
     */
    public String get(String key){
        return valueOperations.get(key);
    }

    /**
     * 删除缓存对象
     * @param key
     */
    public void del(String key){
        stringRedisTemplate.delete(key);
    }

}
