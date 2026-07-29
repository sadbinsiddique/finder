package com.market.finder.service;

import com.market.finder.dao.StaffRepository;
import com.market.finder.entity.Staff;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    @Cacheable(value = "staff", key = "'all'")
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    @Cacheable(value = "staff", key = "'age'")
    public List<Staff> findAllByOrderByAgeAsc() {
        return staffRepository.findAllByOrderByAgeAsc();
    }

    @Override
    @Cacheable(value = "staff", key = "'income'")
    public List<Staff> findAllByOrderByIncomeAsc() {
        return staffRepository.findAllByOrderByIncomeAsc();
    }

    @Override
    @Cacheable(value = "staff", key = "#theId")
    public Optional<Staff> findById(int theId) {
        return staffRepository.findById(theId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "staff", allEntries = true)
    public Staff save(Staff theStaff) {
        return staffRepository.saveAndFlush(theStaff);
    }

    @Override
    @Transactional
    @CacheEvict(value = "staff", allEntries = true)
    public void deleteById(int theId) {
        staffRepository.deleteById(theId);
        staffRepository.flush();
    }
}
