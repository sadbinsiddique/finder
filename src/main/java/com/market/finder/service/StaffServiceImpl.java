package com.market.finder.service;

import com.market.finder.dao.StaffRepository;

import com.market.finder.entity.Staff;

import java.util.List;
import java.util.Optional;

public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffReposotory;

    public StaffServiceImpl(StaffRepository staffReposotory) {
        this.staffReposotory = staffReposotory;
    }

    @Override
    public List<Staff> findAll() {
        return staffReposotory.findAll();
    }

    @Override
    public List<Staff> findAllByOrderByAgeAsc() {
        return staffReposotory.findAllByOrderByAgeAsc();
    }

    @Override
    public List<Staff> findAllByOrderByIncomeAsc() {
        return staffReposotory.findAllByOrderByAgeAsc();
    }

    @Override
    public Staff findById(int theId) {
        Optional<Staff> result = staffReposotory.findById(theId);
        Staff theStaff;

        if (result.isPresent()) {
            theStaff = result.get();
        } else {
            throw new RuntimeException("Did not find staff  id - " + theId);
        }
        return theStaff;
    }

    @Override
    public Staff save(Staff theStaff) {
        return staffReposotory.save(theStaff);
    }

    @Override
    public void deleteById(int theId) {
        staffReposotory.deleteById(theId);
    }
}
