package com.backendufbaendereco.demo.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityDTO {
    private Long id;
    private String name;

    public CityDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters e setters
}