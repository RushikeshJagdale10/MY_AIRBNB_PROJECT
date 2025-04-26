package com.it.airbnb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/payment")
    @Operation(summary = "Capture the payments", tags = {"Webhook"})
    public ResponseEntity<Void> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        log.info("Received Stripe Webhook event");

        try {
            // Verify and parse the event
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            log.info("Received Stripe Event: {}", event.getType());

            // Handle the specific event type
            if ("checkout.session.completed".equals(event.getType())) {
                bookingService.capturePayment(event);  // Call the service to handle payment capture
                log.info("Payment has been Captured");
            } else {
                log.info("Ignoring event type: {}", event.getType());
            }
            
            return ResponseEntity.noContent().build();
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed", e);
            throw new RuntimeException("Invalid signature", e);
        } catch (Exception e) {
            log.error("Error processing Stripe webhook", e);
            throw new RuntimeException("Error processing Stripe webhook", e);
        }
    }
}
