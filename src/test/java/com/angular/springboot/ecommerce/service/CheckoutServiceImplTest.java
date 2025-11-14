package com.angular.springboot.ecommerce.service;

import com.angular.springboot.ecommerce.dao.CustomerRepository;
import com.angular.springboot.ecommerce.dto.PaymentInfo;
import com.angular.springboot.ecommerce.dto.Purchase;
import com.angular.springboot.ecommerce.dto.PurchaseResponse;
import com.angular.springboot.ecommerce.entity.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private Customer testCustomer;
    private Order testOrder;
    private Purchase testPurchase;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setEmail("test@example.com");
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderTrackingNumber("ORDER-123");
        testOrder.setTotalQuantity(2);
        testOrder.setTotalPrice(new BigDecimal("99.99"));

        Address billingAddress = new Address();
        billingAddress.setId(1L);
        billingAddress.setStreet("123 Test St");
        billingAddress.setCity("Test City");
        billingAddress.setCountry("Test Country");
        billingAddress.setZipCode("12345");

        Address shippingAddress = new Address();
        shippingAddress.setId(2L);
        shippingAddress.setStreet("123 Test St");
        shippingAddress.setCity("Test City");
        shippingAddress.setCountry("Test Country");
        shippingAddress.setZipCode("12345");

        testPurchase = new Purchase();
        testPurchase.setCustomer(testCustomer);
        testPurchase.setOrder(testOrder);
        testPurchase.setBillingAddress(billingAddress);
        testPurchase.setShippingAddress(shippingAddress);
        testPurchase.setOrderItems(new HashSet<>());
    }

    @Test
    void placeOrder_NewCustomer_ShouldSaveAndReturnTrackingNumber() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        PurchaseResponse response = checkoutService.placeOrder(testPurchase);

        // Then
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        verify(customerRepository, times(1)).findByEmail(anyString());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void placeOrder_ExistingCustomer_ShouldUpdateAndReturnTrackingNumber() {
        // Given
        when(customerRepository.findByEmail(anyString())).thenReturn(testCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        PurchaseResponse response = checkoutService.placeOrder(testPurchase);

        // Then
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        verify(customerRepository, times(1)).findByEmail(anyString());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createPaymentIntent_ShouldReturnPaymentIntent() throws StripeException {
        // Given
        PaymentInfo paymentInfo = new PaymentInfo(
                9999,
                "USD",
                "test@example.com"
        );

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            PaymentIntent mockIntent = mock(PaymentIntent.class);
            when(mockIntent.getId()).thenReturn("pi_123");
            
            mockedPaymentIntent.when(() -> PaymentIntent.create(anyMap()))
                    .thenReturn(mockIntent);

            // When
            PaymentIntent result = checkoutService.createPaymentIntent(paymentInfo);

            // Then
            assertNotNull(result);
            assertEquals("pi_123", result.getId());
        }
    }
}
