package com.market.finder.service.cache.impl;

import com.market.finder.config.RolePermissionCacheWarmer;
import com.market.finder.service.cache.CacheOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CacheOperatorServiceImpl implements CacheOperatorService {

    private static final Logger logger = LoggerFactory.getLogger(CacheOperatorServiceImpl.class);

    private final CacheManager cacheManager;
    private final RolePermissionCacheWarmer cacheWarmer;

    public CacheOperatorServiceImpl(CacheManager cacheManager, RolePermissionCacheWarmer cacheWarmer) {
        this.cacheManager = cacheManager;
        this.cacheWarmer = cacheWarmer;
    }

    @Override
    public void flushAllCaches() {
        logger.info("[CACHE SYSTEM] Flushing all registered caches...");
        Set<String> cacheNames = new LinkedHashSet<>(cacheManager.getCacheNames());
        cacheNames.add("roles");
        cacheNames.add("permissions");
        for (String name : cacheNames) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
        logger.info("[CACHE SYSTEM] All caches flushed successfully.");
    }

    @Override
    public boolean flushCache(String cacheName) {
        logger.info("[CACHE SYSTEM] Flushing target cache: '{}'", cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean evictKey(String cacheName, String key) {
        logger.info("[CACHE SYSTEM] Evicting key '{}' from cache '{}'", key, cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            try {
                if (key.matches("\\d+")) {
                    cache.evict(Integer.parseInt(key));
                }
            } catch (NumberFormatException ignored) {}
            return true;
        }
        return false;
    }

    @Override
    public void warmupCaches() {
        logger.info("[CACHE SYSTEM] Re-triggering cache warmup...");
        flushAllCaches();
        cacheWarmer.warmRolePermissionCache();
    }
}
