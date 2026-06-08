package com.market.finder.entity;

public class Student {
    private String fastName;
    private String lastName;
    private String email;

    //constructor
    public Student() {}
    public Student(String fastName, String lastName, String email) {
        this.fastName = fastName;
        this.lastName = lastName;
        this.email = email;
    }

    //getter method
    public String getFastName() {
        return fastName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }


    // setter Methode
    public void setFastName(String fastName) {
        this.fastName = fastName;
    }
    public void setLastName(String lastName) {
        this.lastName  =  lastName;
    }
    public void setEmail (String email) {
        this.email = email;
    }

}


