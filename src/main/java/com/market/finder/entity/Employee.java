package com.market.finder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name cannot exceed 45 characters")
    @Column(name = "fast_name", length = 45, nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name cannot exceed 45 characters")
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|edu|org|net|gov|mil|io|co|in|uk|ca|de|fr|jp|au|dev|app|tech|info|biz|me|us|ai|store|online|site|bd)(\\.[a-zA-Z]{2,3})?$",
            message = "Please enter a valid email address"
    )
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    @Column(length = 64, nullable = false)
    private String email;

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
