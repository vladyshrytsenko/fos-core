package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.entity.Payment;
import com.example.foscore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentDto getById(String id) {
        Payment paymentById = this.paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Payment.class));

        return PaymentDto.toDto(paymentById);
    }
}
