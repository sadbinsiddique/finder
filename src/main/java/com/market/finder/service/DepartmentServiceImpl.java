package com.market.finder.service;

import com.market.finder.dao.DepartmentRepository;
import com.market.finder.entity.Department;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Cacheable(value = "departments", key = "'all'")
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    @Cacheable(value = "departments", key = "#id")
    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"departments", "dashboard"}, allEntries = true)
    public Department save(Department department) {
        return departmentRepository.saveAndFlush(department);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"departments", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        departmentRepository.deleteById(id);
        departmentRepository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return departmentRepository.existsById(id);
    }
}
