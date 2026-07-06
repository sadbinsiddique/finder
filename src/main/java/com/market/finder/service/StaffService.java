package com.market.finder.service;

import com.market.finder.entity.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> findAll();

    List<Staff> findAllByOrderByAgeAsc();

    List<Staff> findAllByOrderByIncomeAsc();

    Staff findById(int theId);

    Staff save(Staff theStaff);

    void deleteById(int theId);
}
