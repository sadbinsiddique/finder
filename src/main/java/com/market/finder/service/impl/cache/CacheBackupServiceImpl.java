package com.market.finder.service.impl.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.market.finder.service.cache.CacheBackupService;
import com.market.finder.service.cache.strategy.CacheValueDeserializerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CacheBackupServiceImpl implements CacheBackupService {

    private static final Logger logger = LoggerFactory.getLogger(CacheBackupServiceImpl.class);

    private final CacheManager cacheManager;
    private final List<CacheValueDeserializerStrategy> deserializerStrategies;
    private final ObjectMapper objectMapper;

    public CacheBackupServiceImpl(CacheManager cacheManager, List<CacheValueDeserializerStrategy> deserializerStrategies) {
        this.cacheManager = cacheManager;
        this.deserializerStrategies = deserializerStrategies;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public Map<String, Object> exportCacheData() {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("exportTimestamp", LocalDateTime.now().toString());
        root.put("application", "finder");

        Set<String> cacheNames = new LinkedHashSet<>(cacheManager.getCacheNames());
        cacheNames.add("roles");
        cacheNames.add("permissions");
        root.put("totalCaches", cacheNames.size());

        Map<String, Map<String, Object>> cachesMap = new LinkedHashMap<>();
        int totalKeyCount = 0;

        for (String name : cacheNames) {
            Cache cache = cacheManager.getCache(name);
            if (cache == null) continue;

            Object nativeCache = cache.getNativeCache();
            Map<String, Object> cacheEntries = new LinkedHashMap<>();

            if (nativeCache instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String keyStr = String.valueOf(entry.getKey());
                    Object val = entry.getValue();
                    if (val instanceof Optional<?> opt) {
                        val = opt.orElse(null);
                    }
                    if (val != null) {
                        cacheEntries.put(keyStr, val);
                        totalKeyCount++;
                    }
                }
            }
            cachesMap.put(name, cacheEntries);
        }

        root.put("totalKeys", totalKeyCount);
        root.put("caches", cachesMap);

        return root;
    }

    @Override
    public String exportCacheDataAsJson() {
        try {
            Map<String, Object> cacheData = exportCacheData();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cacheData);
        } catch (Exception e) {
            logger.error("[CACHE BACKUP] Failed to export cache data as JSON", e);
            throw new RuntimeException("Error exporting cache data", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importCacheDataFromJson(String jsonContent) {
        try {
            Map<String, Object> importedRoot = objectMapper.readValue(jsonContent, Map.class);
            if (!importedRoot.containsKey("caches")) {
                logger.warn("[CACHE BACKUP] Import JSON missing 'caches' root key.");
                return false;
            }

            Map<String, Map<String, Object>> cachesMap = (Map<String, Map<String, Object>>) importedRoot.get("caches");
            for (Map.Entry<String, Map<String, Object>> cacheEntry : cachesMap.entrySet()) {
                String cacheName = cacheEntry.getKey();
                Map<String, Object> entries = cacheEntry.getValue();

                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null && entries != null) {
                    for (Map.Entry<String, Object> entry : entries.entrySet()) {
                        String key = entry.getKey();
                        Object rawVal = entry.getValue();
                        Object typedVal = deserializeValueUsingStrategy(cacheName, key, rawVal);

                        if (typedVal != null) {
                            cache.put(key, typedVal);
                            if (key.matches("\\d+")) {
                                try {
                                    cache.put(Integer.parseInt(key), typedVal);
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            }
            logger.info("[CACHE BACKUP] Cache data imported successfully from JSON.");
            return true;
        } catch (Exception e) {
            logger.error("[CACHE BACKUP] Failed to import cache data from JSON", e);
            return false;
        }
    }

    private Object deserializeValueUsingStrategy(String cacheName, String key, Object rawVal) {
        for (CacheValueDeserializerStrategy strategy : deserializerStrategies) {
            if (strategy.supports(cacheName)) {
                return strategy.deserialize(key, rawVal, objectMapper);
            }
        }
        return rawVal;
    }
}
