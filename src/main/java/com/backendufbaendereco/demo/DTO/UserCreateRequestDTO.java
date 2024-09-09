package com.backendufbaendereco.demo.DTO;



import com.backendufbaendereco.demo.entities.user.User;
import com.backendufbaendereco.demo.enums.RoleEnum;
import com.backendufbaendereco.demo.validators.enumValidation.EnumValue;
import jakarta.validation.constraints.*;

public record UserCreateRequestDTO(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name cannot be longer than 100 characters")
        String name,
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        @Email(message = "Email is not valid")
        String email,
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 8,message = "Name cannot be less than 8 characters")
        String password,

        @NotNull(message = "role cannot be null")

        @EnumValue(enumClass = RoleEnum.class, ignoreCase = true)
        RoleEnum role
){
    public User toModel(){
        return new User(name, email, password,role);
    }
}
