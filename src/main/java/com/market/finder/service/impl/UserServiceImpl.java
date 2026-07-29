package com.market.finder.service.impl;

import com.market.finder.dao.UserRepository;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
import com.market.finder.service.RoleService;
import com.market.finder.service.UserService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String, UserRepository> implements UserService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        encodePasswordIfRaw(user);
        return repository.saveAndFlush(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteByUsername(String username) {
        repository.findByUsername(username).ifPresent(user -> {
            validateAdminDeletionProtection(user);
            repository.deleteByUsername(username);
            repository.flush();
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User registerNewUser(String username, String rawPassword, String roleName) {
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists: " + username);
        }

        Role role = roleService.findByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        User user = buildUserEntity(username, rawPassword, role);
        return repository.saveAndFlush(user);
    }

    private void encodePasswordIfRaw(User user) {
        String password = user.getPassword();
        if (password != null && isRawPassword(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    private boolean isRawPassword(String password) {
        return !password.startsWith("{bcrypt}")
                && !password.startsWith("$2a$")
                && !password.startsWith("$2b$");
    }

    private void validateAdminDeletionProtection(User user) {
        boolean isAdmin = user.getRoles() != null && user.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));
        if (isAdmin) {
            throw new IllegalStateException("Deletion prohibited: Admin users cannot be deleted.");
        }
    }

    private User buildUserEntity(String username, String rawPassword, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return user;
    }
}
