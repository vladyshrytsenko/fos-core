package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.PaymentDto;
import com.example.model.entity.Payment;
import com.example.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentDto getById(String id) {
        Payment found = this.paymentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Payment.class));

        return PaymentDto.toDto(found);
    }
}
