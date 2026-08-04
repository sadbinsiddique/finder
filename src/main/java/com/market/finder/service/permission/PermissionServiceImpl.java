package com.market.finder.service.permission;

import com.market.finder.repository.PermissionRepository;
import com.market.finder.entity.Permission;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer, PermissionRepository> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository);
    }

    @Override
    @Cacheable(value = "permissions", key = "'all'")
    public List<Permission> findAll() {
        return super.findAll();
    }

    @Override
    public List<Permission> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Cacheable(value = "permissions", key = "'name:' + #permissionName")
    public Optional<Permission> findByPermissionName(String permissionName) {
        return repository.findByPermissionName(permissionName);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"permissions", "roles"}, allEntries = true)
    public Permission save(Permission permission) {
        return repository.saveAndFlush(permission);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"permissions", "roles"}, allEntries = true)
    public List<Permission> saveAll(Iterable<Permission> permissions) {
        List<Permission> saved = repository.saveAll(permissions);
        repository.flush();
        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"permissions", "roles"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }
}
