package com.market.finder.service.cache.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.finder.entity.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PermissionCacheValueDeserializer implements CacheValueDeserializerStrategy {

    private static final Logger logger = LoggerFactory.getLogger(PermissionCacheValueDeserializer.class);

    @Override
    public boolean supports(String cacheName) {
        return "permissions".equalsIgnoreCase(cacheName);
    }

    @Override
    public Object deserialize(String key, Object rawVal, ObjectMapper objectMapper) {
        if (rawVal == null) return null;
        try {
            if ("all".equalsIgnoreCase(key)) {
                return objectMapper.convertValue(rawVal, new TypeReference<List<Permission>>() {});
            } else if (key.startsWith("name:")) {
                Permission perm = objectMapper.convertValue(rawVal, Permission.class);
                return Optional.ofNullable(perm);
            }
        } catch (Exception e) {
            logger.warn("[PERMISSION CACHE DESERIALIZER] Could not deserialize key '{}': {}", key, e.getMessage());
        }
        return rawVal;
    }
}
