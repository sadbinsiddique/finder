package com.market.finder.controller;

import com.market.finder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Basic CRUD is auto-provided (save, findById, delete, etc.)
    Optional<User> findByUsernameAndEnabledTrue(String username);
}