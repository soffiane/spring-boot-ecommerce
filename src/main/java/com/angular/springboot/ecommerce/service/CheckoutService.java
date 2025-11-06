package com.angular.springboot.ecommerce.service;

import com.angular.springboot.ecommerce.dto.PaymentInfo;
import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
