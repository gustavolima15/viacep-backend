package com.backendufbaendereco.demo.repositories;


import com.backendufbaendereco.demo.entities.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);
    User findByVerificationCode(String verificationCode);

    @Query("SELECT u FROM user u JOIN FETCH u.addresses")
    List<User> findUsersWithAddresses();

    @Query("SELECT u FROM user u WHERE (SELECT COUNT(a) FROM u.addresses a) > 1")
    List<User> findUsersWithMultipleAddresses();

    @Query("SELECT u FROM user u WHERE u.email = :email")
    List<User> findUserByEmail(@Param("email") String email);

}
