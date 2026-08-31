package com.market.finder.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BasePersonEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name cannot exceed 45 characters")
    @Column(name = "first_name", length = 45)
    protected String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name cannot exceed 45 characters")
    @Column(name = "last_name", length = 45)
    protected String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    @Column(name = "email", length = 64)
    protected String email;

    public BasePersonEntity(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
