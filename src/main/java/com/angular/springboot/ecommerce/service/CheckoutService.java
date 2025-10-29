package com.angular.springboot.ecommerce.service;

import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
