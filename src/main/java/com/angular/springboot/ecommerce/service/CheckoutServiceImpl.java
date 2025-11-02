package com.angular.springboot.ecommerce.service;

import com.angular.springboot.ecommerce.dao.CustomerRepository;
import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.entity.Customer;
import com.angular.springboot.ecommerce.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public PurchaseResponse placeOrder(Purchase purchase) {
        //retrieve information from DTO
        Order order = purchase.getOrder();
        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNimber(orderTrackingNumber);
        //populate order with orderItems
        purchase.getOrderItems().forEach(order::addOrderItem);
        //populate order with addresses
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());
        //populate customer with order
        Customer customer = purchase.getCustomer();
        Customer customerFromDB = customerRepository.findByEmail(customer.getEmail());
        if(customerFromDB != null){
            customer = customerFromDB;
        }
        customer.addOrder(order);
        //save
        customerRepository.save(customer);
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        //generate random UUID 4 - use as order tracking number
        //UUID is a 128-bit value that is unique across space and time
        return UUID.randomUUID().toString();
    }
}
