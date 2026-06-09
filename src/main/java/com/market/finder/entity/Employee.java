package com.market.finder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    //all table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="fast_name")
    private String fastName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email")
    private String email;

    public Employee(){}
    public Employee(String email, String lastName, String fastName) {
        this.email = email;
        this.lastName = lastName;
        this.fastName = fastName;
    }

    public int getId() {
        return id;
    }
    public String getFastName() {
        return fastName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setFastName(String fastName) {
        this.fastName = fastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fastName='" + fastName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
