package com.market.finder.service.department;

import com.market.finder.entity.Department;
import com.market.finder.service.base.BaseService;

public interface DepartmentService extends BaseService<Department, Integer> {
    boolean existsById(Integer id);
}
