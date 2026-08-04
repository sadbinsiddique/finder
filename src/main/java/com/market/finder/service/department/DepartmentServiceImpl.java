package com.market.finder.service.department;

import com.market.finder.entity.Department;
import com.market.finder.repository.DepartmentRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Integer, DepartmentRepository> implements DepartmentService {

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        super(departmentRepository);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
