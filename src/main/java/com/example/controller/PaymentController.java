package com.example.controller.rest;

import com.example.model.dto.PaymentDto;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable String uuid) {
        PaymentDto found = this.paymentService.getById(uuid);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }
}
