package com.market.finder.service.cache.impl;

import com.market.finder.service.cache.CacheBackupService;
import com.market.finder.service.cache.CacheInspectorService;
import com.market.finder.service.cache.CacheManagementService;
import com.market.finder.service.cache.CacheOperatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CacheManagementServiceImpl implements CacheManagementService {

    private final CacheInspectorService inspectorService;
    private final CacheOperatorService operatorService;
    private final CacheBackupService backupService;

    public CacheManagementServiceImpl(
            @Qualifier("cacheInspectorServiceImpl") CacheInspectorService inspectorService,
            @Qualifier("cacheOperatorServiceImpl") CacheOperatorService operatorService,
            @Qualifier("cacheBackupServiceImpl") CacheBackupService backupService) {
        this.inspectorService = inspectorService;
        this.operatorService = operatorService;
        this.backupService = backupService;
    }

    @Override
    public List<CacheDetailDTO> getCacheOverview() {
        return inspectorService.getCacheOverview();
    }

    @Override
    public int getTotalCacheKeys() {
        return inspectorService.getTotalCacheKeys();
    }

    @Override
    public void flushAllCaches() {
        operatorService.flushAllCaches();
    }

    @Override
    public boolean flushCache(String cacheName) {
        return operatorService.flushCache(cacheName);
    }

    @Override
    public boolean evictKey(String cacheName, String key) {
        return operatorService.evictKey(cacheName, key);
    }

    @Override
    public void warmupCaches() {
        operatorService.warmupCaches();
    }

    @Override
    public Map<String, Object> exportCacheData() {
        return backupService.exportCacheData();
    }

    @Override
    public String exportCacheDataAsJson() {
        return backupService.exportCacheDataAsJson();
    }

    @Override
    public boolean importCacheDataFromJson(String jsonContent) {
        return backupService.importCacheDataFromJson(jsonContent);
    }
}
