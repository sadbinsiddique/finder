package com.market.finder.dao;

import com.market.finder.entity.Role;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findAll();

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findById(@NonNull Integer id);

    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findByRoleName(String roleName);
}