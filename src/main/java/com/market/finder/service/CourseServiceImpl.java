package com.market.finder.service;

import com.market.finder.dao.CourseRepository;
import com.market.finder.entity.Course;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Cacheable(value = "courses", key = "'all'")
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    @Cacheable(value = "courses", key = "#id")
    public Optional<Course> findById(Integer id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"courses", "dashboard"}, allEntries = true)
    public Course save(Course course) {
        return courseRepository.saveAndFlush(course);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"courses", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        courseRepository.deleteById(id);
        courseRepository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return courseRepository.existsById(id);
    }
}
