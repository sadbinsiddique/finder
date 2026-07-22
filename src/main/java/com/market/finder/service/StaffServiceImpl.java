package com.market.finder.service;

import com.market.finder.dao.StaffRepository;
import com.market.finder.entity.Staff;
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
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public List<Staff> findAllByOrderByAgeAsc() {
        return staffRepository.findAllByOrderByAgeAsc();
    }

    @Override
    public List<Staff> findAllByOrderByIncomeAsc() {
        // FIX: Was calling findAllByOrderByAgeAsc() — copy-paste bug
        return staffRepository.findAllByOrderByIncomeAsc();
    }

    @Override
    public Optional<Staff> findById(int theId) {
        return staffRepository.findById(theId);
    }

    @Override
    @Transactional
    public Staff save(Staff theStaff) {
        return staffRepository.save(theStaff);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        staffRepository.deleteById(theId);
    }
}
