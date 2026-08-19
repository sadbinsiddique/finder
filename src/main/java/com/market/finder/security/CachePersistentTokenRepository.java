package com.market.finder.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class CachePersistentTokenRepository implements PersistentTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(CachePersistentTokenRepository.class);
    private static final String CACHE_NAME = "rememberMe";

    private final CacheManager cacheManager;

    public CachePersistentTokenRepository(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private Cache getCache() {
        return cacheManager.getCache(CACHE_NAME);
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        logger.info("[REMEMBER-ME CACHE] Storing new token in cache layer for user='{}', series='{}'",
                token.getUsername(), token.getSeries());
        Cache cache = getCache();
        if (cache != null) {
            cache.put("series:" + token.getSeries(), token);

            @SuppressWarnings("unchecked")
            Set<String> userSeries = cache.get("user:" + token.getUsername(), Set.class);
            if (userSeries == null) {
                userSeries = new HashSet<>();
            }
            userSeries.add(token.getSeries());
            cache.put("user:" + token.getUsername(), userSeries);
        }
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        logger.info("[REMEMBER-ME CACHE] Updating token in cache layer for series='{}'", series);
        Cache cache = getCache();
        if (cache != null) {
            PersistentRememberMeToken existing = cache.get("series:" + series, PersistentRememberMeToken.class);
            if (existing != null) {
                PersistentRememberMeToken updatedToken = new PersistentRememberMeToken(
                        existing.getUsername(), series, tokenValue, lastUsed);
                cache.put("series:" + series, updatedToken);
            }
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Cache cache = getCache();
        if (cache != null) {
            PersistentRememberMeToken token = cache.get("series:" + seriesId, PersistentRememberMeToken.class);
            if (token != null) {
                logger.info("[REMEMBER-ME CACHE] Cache hit for token series='{}'", seriesId);
                return token;
            }
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        logger.info("[REMEMBER-ME CACHE] Evicting remember-me tokens from cache layer for user='{}'", username);
        Cache cache = getCache();
        if (cache != null) {
            @SuppressWarnings("unchecked")
            Set<String> userSeries = cache.get("user:" + username, Set.class);
            if (userSeries != null) {
                for (String series : userSeries) {
                    cache.evict("series:" + series);
                }
                cache.evict("user:" + username);
            }
        }
    }
}
