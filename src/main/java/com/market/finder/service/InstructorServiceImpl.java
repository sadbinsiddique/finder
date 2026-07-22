package com.market.finder.service;

import com.market.finder.dao.InstructorRepository;
import com.market.finder.entity.Instructor;
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
    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    @Override
    public Optional<Instructor> findById(Integer id) {
        return instructorRepository.findById(id);
    }

    @Override
    @Transactional
    public Instructor save(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        instructorRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return instructorRepository.existsById(id);
    }
}
