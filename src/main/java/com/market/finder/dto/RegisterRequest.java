package com.market.finder.dto;
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
public class RegisterRequest {

    @NotBlank()
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String username;

    @NotBlank()
    //@Email(message = "Please enter a valid email address")
    @Size(max = 64, message = "Must be a valid email address")
    @Pattern(regexp = "^(?i)[a-z0-9._%+-]+@(student\\.aiub\\.edu | gmail\\.com | outlook\\.com | hotmail\\.com)$")
    private String email;

    @NotBlank()
    @Size(min = 8, max = 68, message = "Password must be follow the rule")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^._~+=`|{}\\[\\];:\"'<>,\\\\-]).*$")
    private String password;

    @NotBlank()
    private String confirmPassword;

    private String roleName = "ROLE_STUDENT";
}
