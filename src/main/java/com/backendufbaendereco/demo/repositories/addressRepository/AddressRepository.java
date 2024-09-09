package com.backendufbaendereco.demo.repositories.addressRepository;


import com.backendufbaendereco.demo.entities.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Page<Address> findAllByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT e from Address e WHERE e.user.id = :userId")
    List<Address> findAddressByUser(@Param("userId") Long userId);
}
