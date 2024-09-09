package com.backendufbaendereco.demo.controllers;


import com.auth0.jwt.interfaces.Claim;
import com.backendufbaendereco.demo.DTO.*;
import com.backendufbaendereco.demo.entities.user.User;
import com.backendufbaendereco.demo.helper.JwtHelper;
import com.backendufbaendereco.demo.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserCreateRequestDTO userRequest) throws MessagingException, UnsupportedEncodingException {
       User user = userRequest.toModel();
        UserResponseDTO savedUser = userService.registerUser(user);
        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping("/verify")
    public String verify(@Param("code") String code){
       return userService.verify(code) ? "verify_success" : "verify_fail" ;
    }

    @GetMapping()
    public List<UserFindResponseDTO> findAll(){
        return userService.findAll();
    }

    @GetMapping("{id}")
    public UserFindResponseDTO findById(@PathVariable("id") Long id){
        return userService.findById(id);
    }


    @PostMapping("/{id}/address")

    public ResponseEntity<?>   createUserAddress(@PathVariable("id") Long id, @RequestBody @Valid AddressRequestDTO address, HttpServletRequest request){

        Map<String, Claim> userData = jwtHelper.getUserDataFromJwtToken(request);
        Long userId = userData.get("userId").asLong();

        if (!id.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O ID fornecido não corresponde ao usuário logado");
        }
        userService.createUserAddress(address,id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}/address")
    public ResponseEntity<?>   updateUserAddress(@PathVariable("id") Long id, @RequestBody @Valid AddressRequestDTO address, HttpServletRequest request){

        Map<String, Claim> userData = jwtHelper.getUserDataFromJwtToken(request);
        Long userId = userData.get("userId").asLong();

        if (!id.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O ID fornecido não corresponde ao usuário logado");
        }
        AddressResponseDTO updatedAddress =  userService.updateUserAddress(address,id);
        return ResponseEntity.ok().body(updatedAddress);
    }

    @DeleteMapping ("/{id}/address/{addressId}")
    public ResponseEntity<?>   deleteUserAddress(@PathVariable("id") Long id, @PathVariable("addressId") Long addressId, HttpServletRequest request){

        Map<String, Claim> userData = jwtHelper.getUserDataFromJwtToken(request);
        Long userId = userData.get("userId").asLong();

        if (!id.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O ID fornecido não corresponde ao usuário logado");
        }
        userService.deleteUserAddress(addressId,id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<?> findAllUserAddress(@PathVariable("id") Long id, Pageable pageable, HttpServletRequest request) {
    Map<String, Claim> userData = jwtHelper.getUserDataFromJwtToken(request);
    Long userId = userData.get("userId").asLong();

    if (!id.equals(userId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O ID fornecido não corresponde ao usuário logado");
    }

    Page<AddressResponseDTO> addresses = userService.findAllUserAddress(id, pageable);
    return ResponseEntity.ok().body(addresses);
}


    @GetMapping ("/{id}/address/{addressId}")
    public ResponseEntity<?>  findByAddressIdUserAddress(@PathVariable("id") Long id ,@PathVariable("addressId") Long addressId, HttpServletRequest request){
        Map<String, Claim> userData = jwtHelper.getUserDataFromJwtToken(request);
        Long userId = userData.get("userId").asLong();

        if (!id.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O ID fornecido não corresponde ao usuário logado");
        }

        AddressResponseDTO addressResponse = userService.findByAddressIdUserAddress(id,addressId);
        return  ResponseEntity.status(HttpStatus.OK).body(addressResponse);

    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getVerificationCode()))
                .toList();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-addresses")
    public List<User> getUsersWithAddresses() {
        return userService.findUsersWithAddresses();
    }

    @GetMapping("/multiple-addresses")
    public List<User> getUsersWithMultipleAddresses() {
        return userService.findUsersWithMultipleAddresses();
    }

    @GetMapping("/find-by-email")
    public List<User> getUserByEmail(@RequestParam("email") String email) {
        return userService.findUserByEmail(email);
    }



}
