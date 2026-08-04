package com.market.finder.service.user;

import com.market.finder.entity.User;
import com.market.finder.service.base.BaseService;

import java.util.Optional;

public interface UserService extends BaseService<User, String> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
    User registerNewUser(String username, String rawPassword, String roleName);
}
