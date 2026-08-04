package com.market.finder.service.staff;

import com.market.finder.entity.Staff;
import com.market.finder.repository.StaffRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl extends BaseServiceImpl<Staff, Integer, StaffRepository> implements StaffService {

    public StaffServiceImpl(StaffRepository staffRepository) {
        super(staffRepository);
    }

    @Override
    public List<Staff> findAllByOrderByAgeAsc() {
        return repository.findAllByOrderByAgeAsc();
    }

    @Override
    public List<Staff> findAllByOrderByIncomeAsc() {
        return repository.findAllByOrderByIncomeAsc();
    }
}
