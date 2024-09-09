package com.backendufbaendereco.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDTO(
        @NotNull(message = "email cannot be null")
        @NotBlank(message = "email cannot be blank")
        @Email(message = "Email is not valid")
        String email,
        @NotNull(message = "password cannot be null")
        @NotBlank(message = "password cannot be blank")
        String password) { }
