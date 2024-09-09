package com.backendufbaendereco.demo.repositories;


import com.backendufbaendereco.demo.entities.address.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateId(Long stateId);
    boolean existsById(Long id);

}
