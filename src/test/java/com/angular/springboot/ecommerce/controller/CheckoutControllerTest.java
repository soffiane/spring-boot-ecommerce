package com.angular.springboot.ecommerce.controller;

import com.angular.springboot.ecommerce.dto.PaymentInfo;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.service.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {
    
    private static final String TEST_ORDER_TRACKING = "ORDER-123";

    private MockMvc mockMvc;

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutController checkoutController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
    }

    @Test
    void placeOrder_ShouldReturnOrderTrackingNumber() throws Exception {
        // Given
        String orderTrackingNumber = "ORDER-123";
        PurchaseResponse response = new PurchaseResponse(orderTrackingNumber);
        
        when(checkoutService.placeOrder(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/checkout/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingNumber").value(orderTrackingNumber));
    }

    @Test
    void createPaymentIntent_ShouldReturnPaymentIntent() throws Exception {
        // Given
        PaymentInfo paymentInfo = new PaymentInfo(9999, "usd", "test@example.com");
        String requestBody = objectMapper.writeValueAsString(paymentInfo);
        
        // Création d'un mock PaymentIntent
        PaymentIntent mockPaymentIntent = new PaymentIntent();
        mockPaymentIntent.setId("pi_123");
        
        when(checkoutService.createPaymentIntent(any(PaymentInfo.class)))
            .thenReturn(mockPaymentIntent);

        // When & Then
        mockMvc.perform(post("/api/checkout/payment-intent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pi_123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
