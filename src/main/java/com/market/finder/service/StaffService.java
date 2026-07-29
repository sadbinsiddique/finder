package com.market.finder.service;

import com.market.finder.entity.Staff;
import com.market.finder.service.base.BaseService;

import java.util.List;

public interface StaffService extends BaseService<Staff, Integer> {
    List<Staff> findAllByOrderByAgeAsc();
    List<Staff> findAllByOrderByIncomeAsc();
}
