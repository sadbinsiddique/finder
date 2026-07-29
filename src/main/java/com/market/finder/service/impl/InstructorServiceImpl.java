package com.market.finder.service.impl;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
import com.market.finder.service.InstructorService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorServiceImpl extends BaseServiceImpl<Instructor, Integer, InstructorRepository> implements InstructorService {

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        super(instructorRepository);
    }

    @Override
    @Cacheable(value = "instructors", key = "'all'")
    public List<Instructor> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "instructors", key = "#id")
    public Optional<Instructor> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"instructors", "dashboard"}, allEntries = true)
    public Instructor save(Instructor instructor) {
        return repository.saveAndFlush(instructor);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"instructors", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
