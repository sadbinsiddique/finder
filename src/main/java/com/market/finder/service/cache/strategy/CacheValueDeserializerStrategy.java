package com.market.finder.service.cache.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface CacheValueDeserializerStrategy {
    boolean supports(String cacheName);
    Object deserialize(String key, Object rawVal, ObjectMapper objectMapper);
}
