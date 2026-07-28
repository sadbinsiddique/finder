package com.market.finder.service;

import com.market.finder.entity.Permission;

import java.util.List;
import java.util.Optional;

/**
 * DIP: Controllers and Initializers depend on this interface.
 */
public interface PermissionService {

    List<Permission> findAll();

    List<Permission> findAllById(Iterable<Integer> ids);

    Optional<Permission> findByPermissionName(String permissionName);

    Permission save(Permission permission);

    List<Permission> saveAll(Iterable<Permission> permissions);
}


