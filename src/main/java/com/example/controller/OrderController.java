package com.example.controller.rest;

import com.example.model.dto.OrderDto;
import com.example.model.entity.Order;
import com.example.model.request.OrderRequest;
import com.example.service.OrderService;
import com.example.service.PaymentService;
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

import java.util.List;
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
    public ResponseEntity<OrderDto> create(
        @RequestBody OrderRequest request
    ) {
        OrderDto created = this.orderService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
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
