package com.angular.springboot.ecommerce.dto;

import com.angular.springboot.ecommerce.entity.Address;
import com.angular.springboot.ecommerce.entity.Customer;
import com.angular.springboot.ecommerce.entity.Order;
import com.angular.springboot.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
