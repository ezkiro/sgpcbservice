package com.toyfactory.pcb;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.toyfactory.pcb.model.AgentCommand;
import com.toyfactory.pcb.model.PcbGamePatch;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, PcbGamePatch> jsonRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, PcbGamePatch> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PcbGamePatch.class));
        redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());
        redisTemplate.setHashValueSerializer(redisTemplate.getValueSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, AgentCommand> agentCmdRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, AgentCommand> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(AgentCommand.class));
        redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());
        redisTemplate.setHashValueSerializer(redisTemplate.getValueSerializer());
        return redisTemplate;
    }    
    
    @Bean
    public CacheManager cacheManager(RedisTemplate agentCmdRedisTemplate) {
      RedisCacheManager cacheManager = new RedisCacheManager(agentCmdRedisTemplate);

      // Number of seconds before expiration. Defaults to unlimited (0)
      //cacheManager.setDefaultExpiration(300);
      return cacheManager;
    }    
}
