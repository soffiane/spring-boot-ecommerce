package com.angular.springboot.ecommerce.controller;

import com.angular.springboot.ecommerce.SpringBootEcommerceApplication;
import com.angular.springboot.ecommerce.dto.PaymentInfo;
import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.service.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CheckoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void placeOrder_ShouldReturnOrderTrackingNumber() throws Exception {
        // Given
        String orderTrackingNumber = "ORDER-123";
        Purchase purchase = new Purchase();
        PurchaseResponse response = new PurchaseResponse(orderTrackingNumber);

        when(checkoutService.placeOrder(any(Purchase.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/checkout/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingNumber", is(orderTrackingNumber)));
    }

    @Test
    void createPaymentIntent_ShouldReturnPaymentIntent() throws Exception {
        // Given
        PaymentInfo paymentInfo = new PaymentInfo(9999, "USD", "test@example.com");
        String paymentIntentJson = "{\"id\":\"pi_123\"}";

        when(checkoutService.createPaymentIntent(any(PaymentInfo.class))).thenAnswer(invocation -> {
            com.stripe.model.PaymentIntent paymentIntent = 
                new com.stripe.model.PaymentIntent();
            paymentIntent.setId("pi_123");
            return paymentIntent;
        });

        // When & Then
        mockMvc.perform(post("/api/checkout/payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentInfo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("pi_123")));
    }
}
