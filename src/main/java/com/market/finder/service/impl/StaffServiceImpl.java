package com.market.finder.service.impl;

import com.market.finder.dto.StaffRepository;
import com.market.finder.entity.Staff;
import com.market.finder.service.StaffService;
import com.market.finder.service.base.BaseServiceImpl;
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
    public List<Staff> findAll() {
        return super.findAll();
    }

    @Override
    public List<Staff> findAllByOrderByAgeAsc() {
        return repository.findAllByOrderByAgeAsc();
    }

    @Override
    public List<Staff> findAllByOrderByIncomeAsc() {
        return repository.findAllByOrderByIncomeAsc();
    }

    @Override
    public Optional<Staff> findById(Integer theId) {
        return super.findById(theId);
    }

    @Override
    @Transactional
    public Staff save(Staff theStaff) {
        return repository.saveAndFlush(theStaff);
    }

    @Override
    @Transactional
    public void deleteById(Integer theId) {
        super.deleteById(theId);
        repository.flush();
    }
}
