package com.market.finder.service.impl;

import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Student;
import com.market.finder.service.StudentService;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Integer, StudentRepository> implements StudentService {

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository);
    }

    @Override
    @Cacheable(value = "students", key = "'all'")
    public List<Student> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "students", key = "#id")
    public Optional<Student> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public Student save(Student student) {
        return repository.saveAndFlush(student);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }
}
