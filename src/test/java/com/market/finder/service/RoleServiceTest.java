package com.market.finder.service;

import com.market.finder.config.CacheConfig;
import com.market.finder.entity.Role;
import com.market.finder.repository.RoleRepository;
import com.market.finder.service.role.RoleService;
import com.market.finder.service.role.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoleServiceTest.TestConfig.class)
class RoleServiceTest {

    @Configuration
    @Import(CacheConfig.class)
    static class TestConfig {
        @Bean
        public RoleRepository roleRepository() {
            return mock(RoleRepository.class);
        }

        @Bean
        public RoleService roleService(RoleRepository roleRepository) {
            return new RoleServiceImpl(roleRepository);
        }
    }

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        reset(roleRepository);
        Objects.requireNonNull(cacheManager.getCache("roles")).clear();
    }

    @Test
    void testFindAll_CachingBehavior() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setRoleName("ROLE_ADMIN");

        when(roleRepository.findAll()).thenReturn(List.of(adminRole));

        // First call should hit repository
        List<Role> firstCall = roleService.findAll();
        assertEquals(1, firstCall.size());

        // Second call should return cached value without hitting repository again
        List<Role> secondCall = roleService.findAll();
        assertEquals(1, secondCall.size());

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindById_CachingBehavior() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setRoleName("ROLE_ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(adminRole));

        // First call - cache miss
        Optional<Role> roleOpt1 = roleService.findById(1);
        assertTrue(roleOpt1.isPresent());

        // Second call - cache hit
        Optional<Role> roleOpt2 = roleService.findById(1);
        assertTrue(roleOpt2.isPresent());

        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    void testFindByRoleName_CachingBehavior() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setRoleName("ROLE_ADMIN");

        when(roleRepository.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        // First call - cache miss
        Optional<Role> roleOpt1 = roleService.findByRoleName("ROLE_ADMIN");
        assertTrue(roleOpt1.isPresent());

        // Second call - cache hit
        Optional<Role> roleOpt2 = roleService.findByRoleName("ROLE_ADMIN");
        assertTrue(roleOpt2.isPresent());

        verify(roleRepository, times(1)).findByRoleName("ROLE_ADMIN");
    }

    @Test
    void testSave_EvictsCache() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setRoleName("ROLE_ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(adminRole));
        when(roleRepository.saveAndFlush(any(Role.class))).thenReturn(adminRole);

        // Populate cache
        roleService.findById(1);
        verify(roleRepository, times(1)).findById(1);

        // Save evicts cache
        roleService.save(adminRole);

        // Subsequent call should hit repository again
        roleService.findById(1);
        verify(roleRepository, times(2)).findById(1);
    }

    @Test
    void testDeleteById_EvictsCache() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setRoleName("ROLE_ADMIN");

        when(roleRepository.findById(1)).thenReturn(Optional.of(adminRole));
        doNothing().when(roleRepository).deleteById(1);

        // Populate cache
        roleService.findById(1);
        verify(roleRepository, times(1)).findById(1);

        // Delete evicts cache
        roleService.deleteById(1);

        // Subsequent call should hit repository again
        roleService.findById(1);
        verify(roleRepository, times(2)).findById(1);
    }
}
