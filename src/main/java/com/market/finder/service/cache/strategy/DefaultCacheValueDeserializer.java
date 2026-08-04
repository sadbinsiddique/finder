package com.market.finder.service.cache.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultCacheValueDeserializer implements CacheValueDeserializerStrategy {

    @Override
    public boolean supports(String cacheName) {
        return true;
    }

    @Override
    public Object deserialize(String key, Object rawVal, ObjectMapper objectMapper) {
        return rawVal;
    }
}
