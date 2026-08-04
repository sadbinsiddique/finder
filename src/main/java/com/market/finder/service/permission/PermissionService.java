package com.market.finder.service.permission;

import com.market.finder.entity.Permission;
import com.market.finder.service.base.BaseService;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface PermissionService extends BaseService<Permission, Integer> {
    List<Permission> findAllById(Iterable<Integer> ids);
    Optional<Permission> findByPermissionName(String permissionName);
    List<Permission> saveAll(Iterable<Permission> permissions);
}
