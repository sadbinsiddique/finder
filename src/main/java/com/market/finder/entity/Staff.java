package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name cannot exceed 45 characters")
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name cannot exceed 45 characters")
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|edu|org|net|gov|mil|io|co|in|uk|ca|de|fr|jp|au|dev|app|tech|info|biz|me|us|ai|store|online|site|bd)(\\.[a-zA-Z]{2,3})?$",
            message = "Please enter a valid email address with a recognized domain (e.g. gmail.com, outlook.com, university.edu)"
    )
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Min(value = 0, message = "Income cannot be negative")
    @Column(name = "income")
    private int income;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    @Column(name = "title", length = 50)
    private String title;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be at most 100")
    @Column(name = "age")
    private int age;

    public Staff() {
    }

    public Staff(String email, String lastName, String firstName, int income, String title, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.income = income;
        this.title = title;
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                ", income='" + income + '\'' +
                ", title='" + title + '\'' +
                '}';

    }
}

