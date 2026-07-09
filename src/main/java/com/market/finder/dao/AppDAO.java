package com.market.finder.dao;

import com.market.finder.entity.Instructor;
import com.market.finder.entity.InstructorDetail;

public interface AppDAO {
    void save(Instructor theInstructor);

    Instructor findInstructorById(int theId);

    InstructorDetail findInstructorDetailById(int theId);
}
