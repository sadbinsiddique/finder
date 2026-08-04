package com.market.finder.service.cache;

public interface CacheOperatorService {
    void flushAllCaches();
    boolean flushCache(String cacheName);
    boolean evictKey(String cacheName, String key);
    void warmupCaches();
}
