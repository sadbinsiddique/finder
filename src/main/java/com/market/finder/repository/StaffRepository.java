package com.market.finder.repository;

import com.market.finder.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findAllByOrderByAgeAsc();
    List<Staff> findAllByOrderByIncomeAsc();
}
