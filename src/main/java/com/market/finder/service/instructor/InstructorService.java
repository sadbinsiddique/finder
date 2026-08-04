package com.market.finder.service.instructor;

import com.market.finder.entity.Instructor;
import com.market.finder.service.base.BaseService;

public interface InstructorService extends BaseService<Instructor, Integer> {
    boolean existsById(Integer id);
}
