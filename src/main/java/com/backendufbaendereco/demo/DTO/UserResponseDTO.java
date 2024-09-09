package com.backendufbaendereco.demo.DTO;

public record UserResponseDTO(
    Long id,
    String name,
    String email,
    String verificationCode
){
}
