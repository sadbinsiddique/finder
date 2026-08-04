package com.market.finder.service.cache.impl;

import com.market.finder.service.cache.CacheManagementService.CacheDetailDTO;
import com.market.finder.service.cache.CacheInspectorService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheInspectorServiceImpl implements CacheInspectorService {

    private final CacheManager cacheManager;

    public CacheInspectorServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheDetailDTO> getCacheOverview() {
        List<CacheDetailDTO> details = new ArrayList<>();
        Set<String> cacheNames = new LinkedHashSet<>(cacheManager.getCacheNames());
        cacheNames.add("roles");
        cacheNames.add("permissions");

        for (String name : cacheNames) {
            Cache cache = cacheManager.getCache(name);
            if (cache == null) continue;

            Object nativeCache = cache.getNativeCache();
            int keyCount = 0;
            String storeType = nativeCache != null ? nativeCache.getClass().getSimpleName() : "Unknown";
            List<String> keys = new ArrayList<>();

            if (nativeCache instanceof Map<?, ?> map) {
                keyCount = map.size();
                for (Object k : map.keySet()) {
                    keys.add(String.valueOf(k));
                }
            }

            details.add(new CacheDetailDTO(name, keyCount, storeType, keys));
        }

        return details;
    }

    @Override
    public int getTotalCacheKeys() {
        return getCacheOverview().stream()
                .mapToInt(CacheDetailDTO::keyCount)
                .sum();
    }
}
