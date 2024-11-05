package com.example.controller.rest;

import com.example.model.dto.OrderDto;
import com.example.model.dto.PaymentDto;
import com.example.model.entity.Order;
import com.example.model.enums.PaymentStatus;
import com.example.model.request.CreatePayment;
import com.example.model.request.CreatePaymentItem;
import com.example.model.request.OrderRequest;
import com.example.service.OrderService;
import com.example.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(
        @RequestBody OrderRequest request
    ) {
        OrderDto created = this.orderService.create(request);

        PaymentDto payment = PaymentDto.builder()
            .orderId(created.getId())
            .status(PaymentStatus.PENDING.name())
            .totalPrice(created.getTotalPrice())
            .build();

        PaymentDto createdPayment = this.paymentService.create(payment);

        Map<String, String> response = new HashMap<>();
        response.put("paymentId", createdPayment.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll(
        @RequestParam Optional<Integer> number,
        @RequestParam Optional<Integer> size) {

        PageRequest pageable = PageRequest.of(number.orElse(0), size.orElse(10));
        Page<Order> page = this.orderService.findAll(pageable);
        List<OrderDto> list = OrderDto.toDtoList(page.getContent());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        OrderDto found = this.orderService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
