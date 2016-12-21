package com.period.web.cache.conf;

/**
 * Created by hushuang on 16/8/18.
 */

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.period.web.cache"})
public class CacheConfig {

    /** redis缓存服务器地址    */
    @Value("${period.redis.host}")
    private String host;
    /** redis缓存服务器端口    */
    @Value("${period.redis.port}")
    private Integer port;
    /** redis缓存服务器连接超时时间    */

    @Value("${period.redis.maxTotal}")
    private Integer maxTotal;

    @Value("${period.redis.maxIdle}")
    private Integer maxIdle;

    @Value("${period.redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${period.redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(maxWaitMillis); //  最大等待时间
        config.setMaxTotal(maxTotal);         //  最大连接数
        config.setMinIdle(maxIdle);           //  允许最小的空闲连接数
        config.setTestOnBorrow(testOnBorrow);  //  申请到连接时是否效验连接是否有效,对性能有影响,建议关闭
        config.setTestOnReturn(false);  //  使用完连接放回连接池时是否效验连接是否有效,对性能有影响,建议关闭
        config.setTestWhileIdle(true);  //  申请到连接时,如果空闲时间大于TimeBetweenEvictionRunsMillis时间,效验连接是否有效,建议开启,对性能有效不大
        config.setTimeBetweenEvictionRunsMillis(30000); //TestWhileIdle的判断依据
        return config;
    }

    @Bean(name = "jedisFactory")
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
        return jedisConnectionFactory;
    }

    /**
     * redis模板配置
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean(name = "stringRedisTemplate",autowire = Autowire.BY_NAME)
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
        stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

}

