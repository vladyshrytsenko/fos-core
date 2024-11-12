package com.example.controller;

import com.example.model.dto.PaymentDto;
import com.example.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
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

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private static final String endpointSecret = "your_webhook_secret";

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
}
