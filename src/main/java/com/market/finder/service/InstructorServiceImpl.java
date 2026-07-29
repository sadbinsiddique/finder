package com.market.finder.service;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    @Cacheable(value = "instructors", key = "'all'")
    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    @Override
    @Cacheable(value = "instructors", key = "#id")
    public Optional<Instructor> findById(Integer id) {
        return instructorRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"instructors", "dashboard"}, allEntries = true)
    public Instructor save(Instructor instructor) {
        return instructorRepository.saveAndFlush(instructor);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"instructors", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        instructorRepository.deleteById(id);
        instructorRepository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return instructorRepository.existsById(id);
    }
}
