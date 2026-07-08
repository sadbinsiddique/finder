package com.market.finder.dao;

import com.market.finder.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findAllByOrderByAgeAsc();
    List<Staff> findAllByOrderByIncomeAsc();
}
