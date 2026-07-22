package com.market.finder.service;

import com.market.finder.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * DIP: Controllers depend on this interface, not on RoleRepository directly.
 */
public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Integer id);

    List<Role> findAllById(Iterable<Integer> ids);

    Role save(Role role);

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
