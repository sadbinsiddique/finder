package com.market.finder.dto;

import com.market.finder.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    List<User> findAll();

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
