package com.backendufbaendereco.demo.Projections;

public interface AddressResponseProjections {
    Long getId();
    String getPostalCode();
    String getStreet();
    String getLocation();
    String getLocationType();
    String getNeighborhood();

    String getNumber();
    String getBlock();
    String getLot();
    String getComplement();
    Long getCityId();
    Long getStateId();
    String getCityName();
    String getStateName();
    Boolean getIsMainAddress();
}