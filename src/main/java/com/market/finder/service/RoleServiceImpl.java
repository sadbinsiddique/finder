package com.market.finder.service;

import com.market.finder.dao.RoleRepository;
import com.market.finder.entity.Role;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Cacheable(value = "roles", key = "'all'")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Cacheable(value = "roles", key = "#id")
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    @Cacheable(value = "roles", key = "'name:' + #roleName")
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> findAllById(Iterable<Integer> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public void deleteById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return roleRepository.existsById(id);
    }
}
