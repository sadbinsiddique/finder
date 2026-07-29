package com.market.finder.service.impl;

import com.market.finder.dao.CourseRepository;
import com.market.finder.entity.Course;
import com.market.finder.service.CourseService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Integer, CourseRepository> implements CourseService {

    public CourseServiceImpl(CourseRepository courseRepository) {
        super(courseRepository);
    }

    @Override
    public List<Course> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return repository.saveAndFlush(course);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
