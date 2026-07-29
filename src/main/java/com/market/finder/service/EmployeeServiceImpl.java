package com.market.finder.service;

import com.market.finder.dao.EmployeeRepository;
import com.market.finder.entity.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DIP: Controller depends on EmployeeService interface, not this concrete class.
 * SRP: Only handles Employee business logic.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Cacheable(value = "employees", key = "'all'")
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    @Cacheable(value = "employees", key = "#theId")
    public Optional<Employee> findById(int theId) {
        return employeeRepository.findById(theId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", allEntries = true)
    public Employee save(Employee theEmployee) {
        return employeeRepository.saveAndFlush(theEmployee);
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
        employeeRepository.flush();
    }
}