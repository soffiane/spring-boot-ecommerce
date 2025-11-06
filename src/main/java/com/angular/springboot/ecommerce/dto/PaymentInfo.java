package com.angular.springboot.ecommerce.dto;

public record PaymentInfo(int amount, String currency, String receiptEmail) {
}
