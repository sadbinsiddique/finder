package com.market.finder.service.instructor;

import com.market.finder.entity.Instructor;
import com.market.finder.repository.InstructorRepository;
import com.market.finder.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImpl extends BaseServiceImpl<Instructor, Integer, InstructorRepository> implements InstructorService {

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        super(instructorRepository);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
