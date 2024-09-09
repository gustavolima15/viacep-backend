package com.backendufbaendereco.demo.DTO;

import com.backendufbaendereco.demo.entities.address.City;


public record CityResponseDTO(
    Long id,
    String name,

    Integer ibgeCode
) {

    public static CityResponseDTO fromCity(City city) {
        return new CityResponseDTO(city.getId(), city.getName(), city.getIbgeCode());
    }
}
