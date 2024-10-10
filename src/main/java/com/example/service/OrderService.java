package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DrinkDto;
import com.example.model.dto.LunchDto;
import com.example.model.dto.OrderDto;
import com.example.model.entity.Drink;
import com.example.model.entity.Lunch;
import com.example.model.entity.Order;
import com.example.model.request.OrderRequest;
import com.example.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private LunchService lunchService;
    private DrinkService drinkService;

    @Autowired
    public void setLunchService(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @Autowired
    public void setDrinkService(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @Transactional
    public OrderDto create(OrderRequest request) {
        Order entity = Order.builder().build();

        if (isBlank(request.getLunchName()) ||
            isBlank(request.getDrinkName())) {
            throw new RuntimeException("Lunch or Drink should not be blank");
        }

        LunchDto lunchByName = lunchService.getByName(request.getLunchName());
        Lunch lunch = LunchDto.toEntity(lunchByName);
        entity.setLunch(lunch);

        DrinkDto drinkByName = drinkService.getByName(request.getDrinkName());
        Drink drink = DrinkDto.toEntity(drinkByName);
        entity.setDrink(drink);

        if (drinkByName.getPrice() == null || lunchByName.getPrice() == null) {
            throw new RuntimeException("Price should not be null.");
        }

        float totalPrice = drinkByName.getPrice() + lunchByName.getPrice();
        entity.setTotalPrice(totalPrice);

        Order saved = this.orderRepository.save(entity);
        return OrderDto.toDto(saved);
    }

    public OrderDto getById(Long id) {
        Order found = this.orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Order.class));

        return OrderDto.toDto(found);
    }

    public Page<Order> findAll(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public OrderDto updateById(Long id, OrderRequest orderExists) {
        OrderDto byId = getById(id);

        if (orderExists.getTotalPrice() != null) {
            byId.setTotalPrice(orderExists.getTotalPrice());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Order entity = OrderDto.toEntity(byId);
        Order updated = orderRepository.save(entity);
        return OrderDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }
}
