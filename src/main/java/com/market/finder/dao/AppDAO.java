package com.market.finder.dao;

import com.market.finder.entity.Instructor;

public interface AppDAO {
    void save(Instructor theInstructor);
    Instructor findInstructorById(int theId);

    void deleteInstructorById(int theId);
}
