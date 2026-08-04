package com.market.finder.service.cache.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.finder.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class RoleCacheValueDeserializer implements CacheValueDeserializerStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RoleCacheValueDeserializer.class);

    @Override
    public boolean supports(String cacheName) {
        return "roles".equalsIgnoreCase(cacheName);
    }

    @Override
    public Object deserialize(String key, Object rawVal, ObjectMapper objectMapper) {
        if (rawVal == null) return null;
        try {
            if ("all".equalsIgnoreCase(key)) {
                return objectMapper.convertValue(rawVal, new TypeReference<List<Role>>() {});
            } else if ("role_permissions_map".equalsIgnoreCase(key)) {
                return objectMapper.convertValue(rawVal, new TypeReference<Map<String, Set<String>>>() {});
            } else if (key.startsWith("name:") || key.matches("\\d+")) {
                Role role = objectMapper.convertValue(rawVal, Role.class);
                return Optional.ofNullable(role);
            }
        } catch (Exception e) {
            logger.warn("[ROLE CACHE DESERIALIZER] Could not deserialize key '{}': {}", key, e.getMessage());
        }
        return rawVal;
    }
}
