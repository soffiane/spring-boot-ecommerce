package com.angular.springboot.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String state;

    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    //primarykeyjoincolumn permet de lier l'adresse a l'ordre - id de l'order et celui de l'adresse
    @PrimaryKeyJoinColumn
    private Order order;
}
