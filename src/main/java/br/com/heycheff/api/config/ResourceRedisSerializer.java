package br.com.heycheff.api.config;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class ResourceRedisSerializer implements RedisSerializer<Resource> {
    @Override
    public byte[] serialize(Resource value) throws SerializationException {
        try {
            return value.getContentAsByteArray();
        } catch (IOException | NullPointerException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public Resource deserialize(byte[] bytes) throws SerializationException {
        return new ByteArrayResource(bytes);
    }
}
