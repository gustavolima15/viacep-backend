package com.backendufbaendereco.demo.DTO;


import com.backendufbaendereco.demo.entities.address.Address;
import com.backendufbaendereco.demo.entities.address.City;
import com.backendufbaendereco.demo.entities.address.State;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDTO {

    private Long id;

    @NotBlank
    @Size(max = 8, min = 8)
    private String postalCode;
    @NotBlank
    private  String street;
    @NotBlank
    private   String location;
    @NotBlank
    private  String locationType;
    @NotBlank

    private  String neighborhood;

    private  Integer streetType;
    @NotBlank
    private String number;
    private  String block;
    private String lot;
    private  String complement;
    @NotNull

    private  Long cityId;
    @NotNull
    private  Long stateId;


    private boolean isMainAddress;


    public Address toAddress( City city, State state) {

        Address address = new Address();

        address.setPostalCode(this.getPostalCode());
        address.setStreet(this.getStreet());
        address.setLocation(this.getLocation());
        address.setLocationType(this.getLocationType());
        address.setNeighborhood(this.getNeighborhood());

        address.setNumber(this.getNumber());
        address.setBlock(this.getBlock());
        address.setLot(this.getLot());
        address.setComplement(this.getComplement());
        address.setStateId(state);
        address.setCityId(city);

        return address;
    }

    public Address toPutAddress( City city, State state) {
        Address address = new Address();
        address.setPostalCode(this.getPostalCode());
        address.setId(this.getId());
        address.setStreet(this.getStreet());
        address.setLocation(this.getLocation());
        address.setLocationType(this.getLocationType());
        address.setNeighborhood(this.getNeighborhood());
        address.setNumber(this.getNumber());
        address.setBlock(this.getBlock());
        address.setLot(this.getLot());
        address.setComplement(this.getComplement());



        address.setStateId(state);
        address.setCityId(city);

        return address;
    }


    public boolean getIsMainAddress() {
        return this.isMainAddress;
    }
}
