package com.market.finder.service.course;

import com.market.finder.entity.Course;
import com.market.finder.repository.CourseRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Integer, CourseRepository> implements CourseService {

    public CourseServiceImpl(CourseRepository courseRepository) {
        super(courseRepository);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
