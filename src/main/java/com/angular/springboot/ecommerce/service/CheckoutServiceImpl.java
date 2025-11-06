package com.angular.springboot.ecommerce.service;

import com.angular.springboot.ecommerce.dao.CustomerRepository;
import com.angular.springboot.ecommerce.dto.PaymentInfo;
import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.entity.Customer;
import com.angular.springboot.ecommerce.entity.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository, @Value ("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;
        //initialise Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Transactional
    @Override
    public PurchaseResponse placeOrder(Purchase purchase) {
        //retrieve information from DTO
        Order order = purchase.getOrder();
        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);
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

    /**
     * Permet de creer un PaymentIntent pour API Stripe
     * @param paymentInfo
     * @return
     * @throws StripeException
     */
    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("amount", paymentInfo.amount());
        params.put("currency", paymentInfo.currency());
        params.put("description", "Luv2Shop Purchase");
        params.put("receipt_email", paymentInfo.receiptEmail());
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        //generate random UUID 4 - use as order tracking number
        //UUID is a 128-bit value that is unique across space and time
        return UUID.randomUUID().toString();
    }
}
