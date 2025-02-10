package com.example.foscore.controller;

import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.request.PaymentRequest;
import com.example.foscore.service.PaymentService;
import com.example.foscore.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                paymentRequest.getAmount(),
                paymentRequest.getCurrency()
            );
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            return response;
        } catch (StripeException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating PaymentIntent", e);
        }
    }

    @PostMapping("/stripe/webhook")
    public void handleStripeEvent(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            switch (event.getType()) {
                case "invoice.payment_succeeded":
                    Invoice invoice = (Invoice) event.getData().getObject();
                    break;
                case "invoice.payment_failed":
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable String uuid) {
        PaymentDto found = this.paymentService.getById(uuid);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    private final PaymentService paymentService;
    private final StripeService stripeService;

    private static final String endpointSecret = "your_webhook_secret";
}
