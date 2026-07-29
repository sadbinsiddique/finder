package com.market.finder.service;

import com.market.finder.dao.UserRepository;
import com.market.finder.entity.Role;
import com.market.finder.entity.User;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("{bcrypt}") 
                && !user.getPassword().startsWith("$2a$") 
                && !user.getPassword().startsWith("$2b$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteByUsername(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            boolean isAdmin = user.getRoles() != null && user.getRoles().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getRoleName()));
            if (isAdmin) {
                throw new IllegalStateException("Deletion prohibited: Admin users cannot be deleted.");
            }
            userRepository.deleteByUsername(username);
            userRepository.flush();
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User registerNewUser(String username, String rawPassword, String roleName) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists: " + username);
        }

        Role role = roleService.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.saveAndFlush(user);
    }
}
