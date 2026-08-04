package com.market.finder.service.student;

import com.market.finder.entity.Student;
import com.market.finder.repository.StudentRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Integer, StudentRepository> implements StudentService {

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository);
    }
}
