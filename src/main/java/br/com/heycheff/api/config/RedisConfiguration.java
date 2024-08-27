package br.com.heycheff.api.config;

import br.com.heycheff.api.util.constants.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration {

    private static final Long ONE_HOUR_TTL = 1L;
    public static final String UNDERSCORE = "_";

    @Value("${heycheff.redis.host}")
    String hostname;
    @Value("${heycheff.redis.port}")
    Integer port;
    @Value("${heycheff.redis.ttl}")
    Long defaultTTL;
    @Value("${heycheff.redis.user}")
    String username;
    @Value("${heycheff.redis.password}")
    String password;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        var redisConfig = new RedisStandaloneConfiguration(hostname, port);
        redisConfig.setUsername(username);
        redisConfig.setPassword(password);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTTL))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheCustomizer() {
        return builder -> {
            var hardlyUpdatedCache = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofHours(ONE_HOUR_TTL));

            builder.withCacheConfiguration(CacheNames.MEASURE_UNIT, hardlyUpdatedCache);
            builder.withCacheConfiguration(CacheNames.TAGS, hardlyUpdatedCache);

            var mediaCache = RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair
                            .fromSerializer(new ResourceRedisSerializer()));

            builder.withCacheConfiguration(CacheNames.MEDIA, mediaCache);
        };
    }
}
