package com.market.finder.config;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Callable;


@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger("com.market.finder.CACHE");

    // Color
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            public Cache getCache(@NonNull String name) {
                Cache cache = super.getCache(name);
                return cache != null ? new LoggingCacheWrapper(cache) : null;
            }
        };
    }

    private record LoggingCacheWrapper(Cache delegate) implements Cache {

        @Override
        public @NonNull String getName() {
            return delegate.getName();
        }

        @Override
        public @NonNull Object getNativeCache() {
            return delegate.getNativeCache();
        }

        private void logCacheAction(String action, String color, Object key) {
            if (key != null) {
                logger.info("{}[CACHE {}]{} -> cache='{}', key='{}'", color, action, ANSI_RESET, getName(), key);
            } else {
                logger.info("{}[CACHE {}]{} -> cache='{}'", color, action, ANSI_RESET, getName());
            }
        }

        @Override
        public ValueWrapper get(@NonNull Object key) {
            ValueWrapper value = delegate.get(key);
            if (value != null) {
                logCacheAction("HIT", ANSI_CYAN, key);
            }
            return value;
        }

        @Override
        public <T> T get(@NonNull Object key, Class<T> type) {
            T value = delegate.get(key, type);
            if (value != null) {
                logCacheAction("HIT", ANSI_CYAN, key);
            }
            return value;
        }

        @Override
        public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
            return delegate.get(key, valueLoader);
        }

        @Override
        public void put(@NonNull Object key, Object value) {
            logCacheAction("STORE", ANSI_GREEN, key);
            delegate.put(key, value);
        }

        @Override
        public void evict(@NonNull Object key) {
            logCacheAction("EVICT", ANSI_RED, key);
            delegate.evict(key);
        }

        @Override
        public void clear() {
            logCacheAction("CLEAR", ANSI_RED, null);
            delegate.clear();
        }
    }
}
