package com.backendufbaendereco.demo.DTO;


import com.backendufbaendereco.demo.Projections.AddressResponseProjections;
import com.backendufbaendereco.demo.entities.address.Address;

import com.backendufbaendereco.demo.entities.address.City;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressResponseDTO {
    private Long id;
    private String postalCode;
    private String street;
    private String location;
    private String locationType;
    private String neighborhood;
    private Integer streetType;
    private String number;
    private String block;
    private String lot;
    private String complement;
    private CityDTO city;
    private StateDTO state;
    private Boolean isMainAddress;

    public static AddressResponseDTO addressResponse(Address address, Boolean isMainAddress) {
        AddressResponseDTO addressResponse = new AddressResponseDTO();
        addressResponse.setId(address.getId());
        addressResponse.setPostalCode(address.getPostalCode());
        addressResponse.setStreet(address.getStreet());
        addressResponse.setLocation(address.getLocation());
        addressResponse.setLocationType(address.getLocationType());
        addressResponse.setNeighborhood(address.getNeighborhood());

        addressResponse.setNumber(address.getNumber());
        addressResponse.setBlock(address.getBlock());
        addressResponse.setLot(address.getLot());
        addressResponse.setComplement(address.getComplement());


        addressResponse.setCity(
                new CityDTO(address.getCityId().getId(), address.getCityId().getName())
        );
        addressResponse.setState(
                new StateDTO(address.getStateId().getId(), address.getStateId().getName())
        );
        addressResponse.setIsMainAddress(isMainAddress.booleanValue());

        return addressResponse;
    }

    public static AddressResponseDTO createFromProjection(AddressResponseProjections projection) {
        AddressResponseDTO dto = new AddressResponseDTO();

        dto.setId(projection.getId());
        dto.setPostalCode(projection.getPostalCode());
        dto.setStreet(projection.getStreet());
        dto.setLocation(projection.getLocation());
        dto.setLocationType(projection.getLocationType());
        dto.setNeighborhood(projection.getNeighborhood());

        dto.setNumber(projection.getNumber());
        dto.setBlock(projection.getBlock());
        dto.setLot(projection.getLot());
        dto.setComplement(projection.getComplement());
        dto.setCity(
                new CityDTO(projection.getCityId(), projection.getCityName())
        );
        dto.setState(
                new StateDTO(projection.getStateId(), projection.getStateName())
        );
        dto.setIsMainAddress(projection.getIsMainAddress());


        return dto;
    }

    public static AddressResponseDTO fromAddress(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setPostalCode(address.getPostalCode());
        dto.setCity(new CityDTO(address.getCityId().getId(), address.getCityId().getName()));
        dto.setState(new StateDTO(address.getStateId().getId(), address.getStateId().getName()));
        return dto;
    }
}



