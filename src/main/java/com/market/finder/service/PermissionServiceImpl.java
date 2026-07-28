package com.market.finder.service;

import com.market.finder.dao.PermissionRepository;
import com.market.finder.entity.Permission;
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
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findAllById(Iterable<Integer> ids) {
        return permissionRepository.findAllById(ids);
    }


    @Override
    public Optional<Permission> findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    @Override
    @Transactional
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public List<Permission> saveAll(Iterable<Permission> permissions) {
        return permissionRepository.saveAll(permissions);
    }
}

