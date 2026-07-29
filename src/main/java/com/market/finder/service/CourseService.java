package com.market.finder.service;

import com.market.finder.entity.Course;
import com.market.finder.service.base.BaseService;

public interface CourseService extends BaseService<Course, Integer> {
    boolean existsById(Integer id);
}
