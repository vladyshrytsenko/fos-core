package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DrinkDto;
import com.example.model.dto.LunchDto;
import com.example.model.dto.OrderDto;
import com.example.model.entity.Drink;
import com.example.model.entity.Lunch;
import com.example.model.entity.Order;
import com.example.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LunchService lunchService;
    private final DrinkService drinkService;

    @Transactional
    public OrderDto create(OrderDto orderDto) {
        Order entity = OrderDto.toEntity(orderDto);

        LunchDto lunchDto = orderDto.getLunch();
        DrinkDto drinkDto = orderDto.getDrink();
        if (lunchDto != null) {
            LunchDto createdLunch = this.lunchService.create(lunchDto);
            Lunch lunch = LunchDto.toEntity(createdLunch);
            entity.setLunch(lunch);
        }
        if (drinkDto != null) {
            DrinkDto createdDrink = this.drinkService.create(drinkDto);
            Drink drink = DrinkDto.toEntity(createdDrink);
            entity.setDrink(drink);
        }

        Order saved = this.orderRepository.save(entity);
        return OrderDto.toDto(saved);
    }

    public OrderDto getById(Long id) {
        Order found = this.orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        return OrderDto.toDto(found);
    }

    public Page<Order> findAll(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public OrderDto updateById(Long id, OrderDto orderExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }

}
