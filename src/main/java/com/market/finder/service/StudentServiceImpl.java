package com.market.finder.service;

import com.market.finder.dao.StudentRepository;
import com.market.finder.entity.Student;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Cacheable(value = "students", key = "'all'")
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Cacheable(value = "students", key = "#id")
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public Student save(Student student) {
        return studentRepository.saveAndFlush(student);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"students", "dashboard"}, allEntries = true)
    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
        studentRepository.flush();
    }
}
