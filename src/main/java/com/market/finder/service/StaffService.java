package com.market.finder.service;

import com.market.finder.entity.Staff;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Staff operations.
 */
public interface StaffService {

    List<Staff> findAll();

    List<Staff> findAllByOrderByAgeAsc();

    List<Staff> findAllByOrderByIncomeAsc();

    Optional<Staff> findById(int theId);

    Staff save(Staff theStaff);

    void deleteById(int theId);
}
