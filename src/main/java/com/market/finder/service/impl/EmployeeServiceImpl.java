package com.market.finder.service.impl;

import com.market.finder.dao.EmployeeRepository;
import com.market.finder.entity.Employee;
import com.market.finder.service.EmployeeService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Integer, EmployeeRepository> implements EmployeeService {

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        super(employeeRepository);
    }

    @Override
    @Cacheable(value = "employees", key = "'all'")
    public List<Employee> findAll() {
        return repository.findAllByOrderByLastNameAsc();
    }

    @Override
    @Cacheable(value = "employees", key = "#theId")
    public Optional<Employee> findById(Integer theId) {
        return super.findById(theId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", allEntries = true)
    public Employee save(Employee theEmployee) {
        return repository.saveAndFlush(theEmployee);
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteById(Integer theId) {
        super.deleteById(theId);
        repository.flush();
    }
}
