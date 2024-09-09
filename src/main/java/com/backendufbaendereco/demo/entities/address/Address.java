package com.backendufbaendereco.demo.entities.address;

import com.backendufbaendereco.demo.entities.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "Address")
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postal_code",length = 8,nullable = false)
    private String postalCode;
    private String street;
    private String location;
    @Column(name = "location_type")
    private String locationType;

    private String neighborhood;

    @Column(name = "street_type")
    private int streetType;
    private String number;
    private String block;
    private String lot;
    private String complement;


    @ManyToOne()
    @JoinColumn(name = "city_id", nullable = false)
    private City cityId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    @ManyToOne()
    @JoinColumn(name = "state_id",nullable = false)
    private State stateId;


    @ManyToMany(mappedBy = "addresses")
    private Set<User> users = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


}