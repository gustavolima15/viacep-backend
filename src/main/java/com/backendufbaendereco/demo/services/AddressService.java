package com.backendufbaendereco.demo.services;

import com.backendufbaendereco.demo.DTO.AddressRequestDTO;
import com.backendufbaendereco.demo.DTO.AddressResponseDTO;
import com.backendufbaendereco.demo.DTO.CityResponseDTO;
import com.backendufbaendereco.demo.DTO.StateResponseDTO;
import com.backendufbaendereco.demo.Exeption.ValidationException;
import com.backendufbaendereco.demo.entities.address.Address;
import com.backendufbaendereco.demo.entities.address.City;
import com.backendufbaendereco.demo.entities.address.State;
import com.backendufbaendereco.demo.repositories.addressRepository.AddressRepository;
import com.backendufbaendereco.demo.repositories.CityRepository;
import com.backendufbaendereco.demo.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private AddressRepository addressRepository;



    public List<CityResponseDTO> getCitiesByState(Long stateId){

        return cityRepository.findByStateId(stateId).stream() .map(CityResponseDTO::fromCity).collect(Collectors.toList());

    }

    public List<StateResponseDTO> findAllStates(){
        return stateRepository.findAll().stream().map(StateResponseDTO::fromState).collect(Collectors.toList());
    }

    public State getStateIdByCityId(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() -> new ValidationException("City not found"));

        State state = city.getState();
        if (state == null) new ValidationException("State not found");

        return state;
    }

    public Address createAddress(AddressRequestDTO address) {

        City city = cityRepository.findById(address.getCityId())
                            .orElseThrow(() -> new ValidationException("City not found with id: " + address.getCityId()));

        State state = this.getStateIdByCityId(address.getCityId());
        Address addressData = address.toAddress(city, state);

        return  addressRepository.save(addressData);

    }

     public Page<AddressResponseDTO> getAllAddressByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Address> addresses = addressRepository.findAllByUserId(userId, pageable);

      
        return addresses.map(AddressResponseDTO::fromAddress);
    }
   
}
