package com.market.finder.event;

import com.market.finder.service.permission.PermissionService;
import com.market.finder.service.role.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RolePermissionCacheWarmer {

    private static final Logger logger = LoggerFactory.getLogger(RolePermissionCacheWarmer.class);
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RolePermissionCacheWarmer(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmRolePermissionCache() {
        logger.info("[CACHE WARMUP] Pre-loading caches at application startup...");
        try {
            permissionService.findAll();
            roleService.findAll();
            Map<String, Set<String>> roleMap = roleService.getRolePermissionsMap();
            logger.info("[CACHE WARMUP] Caches successfully pre-loaded with {} roles.", roleMap != null ?
                    roleMap.size() : 0);
        } catch (Exception e) {
            logger.error("[CACHE WARMUP] Error during cache warmup: {}", e.getMessage(), e);
        }
    }
}
