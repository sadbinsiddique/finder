package com.market.finder.service;

import com.market.finder.dao.PermissionRepository;
import com.market.finder.entity.Permission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Cacheable(value = "permissions", key = "'all'")
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findAllById(Iterable<Integer> ids) {
        return permissionRepository.findAllById(ids);
    }

    @Override
    @Cacheable(value = "permissions", key = "'name:' + #permissionName")
    public Optional<Permission> findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permissions", allEntries = true)
    public Permission save(Permission permission) {
        return permissionRepository.saveAndFlush(permission);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permissions", allEntries = true)
    public List<Permission> saveAll(Iterable<Permission> permissions) {
        List<Permission> saved = permissionRepository.saveAll(permissions);
        permissionRepository.flush();
        return saved;
    }
}

