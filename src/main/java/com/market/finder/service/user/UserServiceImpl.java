package com.market.finder.service.user;

import com.market.finder.entity.Instructor;
import com.market.finder.entity.Role;
import com.market.finder.entity.Student;
import com.market.finder.entity.User;
import com.market.finder.repository.InstructorRepository;
import com.market.finder.repository.StudentRepository;
import com.market.finder.repository.UserRepository;
import com.market.finder.service.base.BaseServiceImpl;
import com.market.finder.service.role.RoleService;
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
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder,
                           StudentRepository studentRepository, InstructorRepository instructorRepository) {
        super(userRepository);
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            repository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
                if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                }
            });
        }
        encodePasswordIfRaw(user);
        return repository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        repository.findByUsername(username).ifPresent(user -> {
            validateAdminDeletionProtection(user);
            repository.deleteByUsername(username);
            repository.flush();
        });
    }

    @Override
    @Transactional
    public User registerNewUser(String username, String rawPassword, String roleName) {
        return registerNewUser(username, null, rawPassword, roleName);
    }

    @Override
    @Transactional
    public User registerNewUser(String username, String email, String rawPassword, String roleName) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        String cleanUsername = username.trim();
        if (repository.findByUsername(cleanUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + cleanUsername);
        }

        String cleanEmail = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
        if (cleanEmail != null && repository.findByEmail(cleanEmail).isPresent()) {
            throw new IllegalArgumentException("An account with email '" + cleanEmail + "' already exists.");
        }

        String targetRole = (roleName == null || roleName.trim().isEmpty()) ? "ROLE_STUDENT" : roleName.trim();
        Role role = roleService.findByRoleName(targetRole)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + targetRole));

        User user = buildUserEntity(cleanUsername, rawPassword, role);
        user.setEmail(cleanEmail);
        return repository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void resetPassword(String username, String newPassword) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.saveAndFlush(user);
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

    @Override
    public Optional<User> findUserByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Optional.empty();
        }
        String trimmed = identifier.trim();
        // 1. Direct username lookup
        Optional<User> userOpt = repository.findByUsername(trimmed);
        if (userOpt.isPresent()) {
            return userOpt;
        }

        if (trimmed.contains("@")) {
            // 2. Direct email lookup on Users table
            Optional<User> userByEmail = repository.findByEmail(trimmed);
            if (userByEmail.isPresent()) {
                return userByEmail;
            }

            // 3. Email lookup for Student and Instructor
            Optional<Student> studentOpt = studentRepository.findByEmail(trimmed);
            if (studentOpt.isPresent() && studentOpt.get().getUsername() != null) {
                return repository.findByUsername(studentOpt.get().getUsername());
            }

            Optional<Instructor> instructorOpt = instructorRepository.findByEmail(trimmed);
            if (instructorOpt.isPresent() && instructorOpt.get().getUsername() != null) {
                return repository.findByUsername(instructorOpt.get().getUsername());
            }

            // 4. Fallback prefix check (username prefix of email address)
            String prefix = trimmed.substring(0, trimmed.indexOf("@"));
            Optional<User> prefixUser = repository.findByUsername(prefix);
            if (prefixUser.isPresent()) {
                return prefixUser;
            }
        }

        return Optional.empty();
    }

    @Override
    public String getEmailByUsername(String username) {
        if (username == null || username.isBlank()) {
            return "";
        }
        String trimmed = username.trim();
        Optional<User> userOpt = repository.findByUsername(trimmed);
        if (userOpt.isPresent() && userOpt.get().getEmail() != null) {
            return userOpt.get().getEmail();
        }

        Optional<Student> studentOpt = studentRepository.findByUsername(trimmed);
        if (studentOpt.isPresent() && studentOpt.get().getEmail() != null) {
            return studentOpt.get().getEmail();
        }

        Optional<Instructor> instructorOpt = instructorRepository.findByUsername(trimmed);
        if (instructorOpt.isPresent() && instructorOpt.get().getEmail() != null) {
            return instructorOpt.get().getEmail();
        }

        return trimmed + "@university.edu"; // default fallback
    }
}
