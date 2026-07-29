package com.market.finder.service.impl;

import com.market.finder.dao.RoleRepository;
import com.market.finder.entity.Role;
import com.market.finder.service.RoleService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    @CacheEvict(value = {"roles", "users"}, allEntries = true)
    public Role save(Role role) {
        return repository.saveAndFlush(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roles", "users"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
