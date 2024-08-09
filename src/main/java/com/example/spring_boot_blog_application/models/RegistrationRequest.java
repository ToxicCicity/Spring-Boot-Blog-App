package com.example.spring_boot_blog_application.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "firstName is required")
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;

    @NotEmpty(message = "lastName is required")
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;

    @Email(message = "email is not valid")
    @NotEmpty(message = "email is required")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @Size(min = 8, message = "password must be at least 8 characters long")
    @NotEmpty(message = "password is required")
    @NotBlank(message = "password cannot be blank")
    private String password;

}
