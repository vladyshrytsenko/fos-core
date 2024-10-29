package com.example.service;

import com.example.model.dto.OrderDto;
import com.example.model.dto.PaymentDto;
import com.example.model.entity.Order;
import com.example.model.entity.Payment;
import com.example.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentDto create(PaymentDto request) {

        Payment entity = PaymentDto.toEntity(request);

        if (request.getOrderId() != null) {
            OrderDto orderById = this.orderService.getById(request.getOrderId());
            Order order = OrderDto.toEntity(orderById);
            entity.setOrder(order);
        }
        Payment saved = this.paymentRepository.save(entity);
        return PaymentDto.toDto(saved);
    }

    public Optional<Payment> getById(String id) {
        return this.paymentRepository.findById(id);
    }
}
