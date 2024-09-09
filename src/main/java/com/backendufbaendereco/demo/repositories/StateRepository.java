package com.backendufbaendereco.demo.repositories;

import com.backendufbaendereco.demo.entities.address.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    List<State> findAll();
}
