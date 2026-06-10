package com.market.finder.dao;

import com.market.finder.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    // define field for entity manager
    private EntityManager entityManager;

    //set up constructor injection
    @Autowired
    public void setEntityManager(EntityManager injection) {
        entityManager = injection;
    }

    @Override
    public List<Employee> findAll() {
        //create a Query
        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);
        //execute query and get the result list
        return theQuery.getResultList();
    }
}
