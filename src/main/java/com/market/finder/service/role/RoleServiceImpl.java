package com.market.finder.service.role;

import com.market.finder.repository.RoleRepository;
import com.market.finder.entity.Permission;
import com.market.finder.entity.Role;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer, RoleRepository> implements RoleService {

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
    }

    @Override
    @Cacheable(value = "roles", key = "'all'")
    public List<Role> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "roles", key = "#id")
    public Optional<Role> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Cacheable(value = "roles", key = "'name:' + #roleName")
    public Optional<Role> findByRoleName(String roleName) {
        return repository.findByRoleName(roleName);
    }

    @Override
    public List<Role> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "roles", key = "'role_permissions_map'")
    public void getRolePermissionsMap() {
        Map<String, Set<String>> rolePermissionsMap = new HashMap<>();
        List<Role> roles = repository.findAll();
        for (Role role : roles) {
            Set<String> permissionNames = role.getPermissions() != null
                    ? role.getPermissions().stream()
                            .map(Permission::getPermissionName)
                            .collect(Collectors.toSet())
                    : Collections.emptySet();
            rolePermissionsMap.put(role.getRoleName(), permissionNames);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roles", "permissions"}, allEntries = true)
    public Role save(Role role) {
        return repository.saveAndFlush(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roles", "permissions"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
