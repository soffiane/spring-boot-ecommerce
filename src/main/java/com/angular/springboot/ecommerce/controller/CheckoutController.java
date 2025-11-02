package com.angular.springboot.ecommerce.controller;

import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin("http://localhost:4200")
@RequestMapping("api/checkout")
public class CheckoutController {

    private CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {
        return checkoutService.placeOrder(purchase);
    }
}
