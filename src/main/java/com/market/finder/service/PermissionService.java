package com.market.finder.service;

import com.market.finder.entity.Permission;

import java.util.List;

/**
 * DIP: Controllers depend on this interface.
 */
public interface PermissionService {

    List<Permission> findAll();
}
