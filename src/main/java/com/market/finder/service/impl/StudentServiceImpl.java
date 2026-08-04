package com.market.finder.service.impl;

import com.market.finder.dto.StudentRepository;
import com.market.finder.entity.Student;
import com.market.finder.service.StudentService;
import com.market.finder.service.base.BaseServiceImpl;
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
    public List<Student> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    @Transactional
    public Student save(Student student) {
        return repository.saveAndFlush(student);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        super.deleteById(id);
        repository.flush();
    }
}
