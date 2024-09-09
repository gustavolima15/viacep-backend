package com.backendufbaendereco.demo.repositories.userAddressMappingRepository;

import com.backendufbaendereco.demo.DTO.AddressResponseDTO;
import com.backendufbaendereco.demo.Projections.AddressResponseProjections;
import com.backendufbaendereco.demo.entities.address.Address;
import com.backendufbaendereco.demo.entities.user.User;
import com.backendufbaendereco.demo.entities.user.UserAddressMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserAddressMappingRepository   {
    
    void save(UserAddressMapping userAddressMapping);


    void updateIsMainAddressByUserId(User user);


    Optional<UserAddressMapping> findByUserIdAndByAddressId(User user, Address address);


    Page<UserAddressMapping> findAllUserAddress(Pageable pageable,Long userId);


    Optional<UserAddressMapping> findByAddressIdUserAddress(Long userId, Long addressId);

    void deleteById(Long id);

}
