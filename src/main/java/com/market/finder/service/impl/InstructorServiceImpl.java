package com.market.finder.service.impl;

import com.market.finder.dto.InstructorRepository;
import com.market.finder.entity.Instructor;
import com.market.finder.service.InstructorService;
import com.market.finder.service.base.BaseServiceImpl;
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
    public List<Instructor> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Instructor> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Instructor save(Instructor instructor) {
        return repository.saveAndFlush(instructor);
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
