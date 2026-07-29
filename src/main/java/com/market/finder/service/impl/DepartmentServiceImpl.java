package com.market.finder.service.impl;

import com.market.finder.dao.DepartmentRepository;
import com.market.finder.entity.Department;
import com.market.finder.service.DepartmentService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Integer, DepartmentRepository> implements DepartmentService {

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        super(departmentRepository);
    }

    @Override
    @Cacheable(value = "departments", key = "'all'")
    public List<Department> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "departments", key = "#id")
    public Optional<Department> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"departments", "dashboard"}, allEntries = true)
    public Department save(Department department) {
        return repository.saveAndFlush(department);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"departments", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
