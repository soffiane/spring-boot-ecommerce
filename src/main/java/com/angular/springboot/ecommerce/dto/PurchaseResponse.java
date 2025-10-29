package com.angular.springboot.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Data genere le constructeur pour les champs final uniquement
@Data
public class PurchaseResponse {
    private final String orderTrackingNumber;
}
