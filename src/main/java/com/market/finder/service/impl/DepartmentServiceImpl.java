package com.market.finder.service.impl;

import com.market.finder.dao.DepartmentRepository;
import com.market.finder.entity.Department;
import com.market.finder.service.DepartmentService;
import com.market.finder.service.base.BaseServiceImpl;
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
    public List<Department> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Department> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Department save(Department department) {
        return repository.saveAndFlush(department);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
