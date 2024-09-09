package com.backendufbaendereco.demo.DTO;

import com.backendufbaendereco.demo.entities.address.State;

public record StateResponseDTO(
         Long id,
         String name,
         String abbreviation) {

   public static StateResponseDTO fromState(State state){
       return new StateResponseDTO(state.getId(), state.getName(), state.getAbbreviation());
   }
}
