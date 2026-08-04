package com.market.finder.service.role;

import com.market.finder.entity.Role;
import com.market.finder.service.base.BaseService;

import java.util.List;
import java.util.Optional;

public interface RoleService extends BaseService<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
    List<Role> findAllById(Iterable<Integer> ids);
    boolean existsById(Integer id);
    void getRolePermissionsMap();
}
