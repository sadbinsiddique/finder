package com.market.finder.service;

import com.market.finder.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findByUsername(String username);

    User save(User user);

    void deleteByUsername(String username);

    User registerNewUser(String username, String rawPassword, String roleName);
}
