package com.market.finder.entity;

import com.market.finder.entity.base.BasePersonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "staff")
public class Staff extends BasePersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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

