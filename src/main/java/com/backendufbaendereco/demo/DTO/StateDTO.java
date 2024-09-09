package com.backendufbaendereco.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateDTO {
    private Long id;
    private String name;

    public StateDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
// getters e setters
}