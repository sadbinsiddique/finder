package com.market.finder.service.cache;

import com.market.finder.service.CacheManagementService.CacheDetailDTO;
import java.util.List;

public interface CacheInspectorService {
    List<CacheDetailDTO> getCacheOverview();
    int getTotalCacheKeys();
}
