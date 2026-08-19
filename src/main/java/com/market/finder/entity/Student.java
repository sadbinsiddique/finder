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
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
            message = "Please enter a valid email address with a recognized domain"
    )
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    @Column(length = 64, nullable = false)
    private String email;

    @Size(max = 50, message = "Username cannot exceed 50 characters")
    @Column(name = "username", length = 50)
    private String username;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StudentDetail studentDetail;

    public void setUser(User user) {
        if (user != null) {
            this.username = user.getUsername();
        }
    }
}