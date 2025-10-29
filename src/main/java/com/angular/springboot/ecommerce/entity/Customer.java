package com.angular.springboot.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orderList;

    public void addOrder(Order order) {
        if (order != null) {
            if (orderList == null) {
                orderList = new HashSet<>();
            }
            orderList.add(order);
            order.setCustomer(this);
        }
    }
    /*private String password;

    private String phoneNumber;

    private Address billingAdress;

    private Address shippingAddress;*/
}
