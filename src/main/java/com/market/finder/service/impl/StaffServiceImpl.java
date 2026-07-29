package com.market.finder.service.impl;

import com.market.finder.dao.StaffRepository;
import com.market.finder.entity.Staff;
import com.market.finder.service.StaffService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl extends BaseServiceImpl<Staff, Integer, StaffRepository> implements StaffService {

    public StaffServiceImpl(StaffRepository staffRepository) {
        super(staffRepository);
    }

    @Override
    @Cacheable(value = "staff", key = "'all'")
    public List<Staff> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "staff", key = "'age'")
    public List<Staff> findAllByOrderByAgeAsc() {
        return repository.findAllByOrderByAgeAsc();
    }

    @Override
    @Cacheable(value = "staff", key = "'income'")
    public List<Staff> findAllByOrderByIncomeAsc() {
        return repository.findAllByOrderByIncomeAsc();
    }

    @Override
    @Cacheable(value = "staff", key = "#theId")
    public Optional<Staff> findById(Integer theId) {
        return super.findById(theId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "staff", allEntries = true)
    public Staff save(Staff theStaff) {
        return repository.saveAndFlush(theStaff);
    }

    @Override
    @Transactional
    @CacheEvict(value = "staff", allEntries = true)
    public void deleteById(Integer theId) {
        super.deleteById(theId);
        repository.flush();
    }
}
