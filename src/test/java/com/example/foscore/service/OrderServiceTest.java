package com.example.foscore.service;

import com.example.foscore.MockData;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.request.OrderRequest;
import com.example.foscore.repository.OrderRepository;
import com.example.foscore.service.rabbitmq.PopularDishesProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MealService mealService;

    @Mock
    private DessertService dessertService;

    @Mock
    private DrinkService drinkService;

    @Mock
    private PopularDishesProducer popularDishesProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    void create() {
        OrderRequest request = MockData.orderRequest();

        Order entity = OrderRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Order savedOrder = MockData.order();

        Drink drink = MockData.drink();
        when(this.drinkService.getEntityByName(anyString())).thenReturn(drink);

        Meal meal = MockData.meal();
        when(this.mealService.getEntityByName(anyString())).thenReturn(meal);

        Dessert dessert = MockData.dessert();
        when(this.dessertService.getEntityByName(anyString())).thenReturn(dessert);

        when(this.orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDto result = this.orderService.create(1L, request);

        assertNotNull(result);
        verify(this.orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getById() {
        Order order = MockData.order();

        when(this.orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderDto result = this.orderService.getById(1L);

        assertNotNull(result);
        verify(this.orderRepository, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        Page<Order> page = new PageImpl<>(MockData.orderList());
        when(this.orderRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<OrderDto> result = this.orderService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(this.orderRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void deleteById() {
        doNothing().when(this.orderRepository).deleteById(1L);
        this.orderService.deleteById(1L);
        verify(this.orderRepository, times(1)).deleteById(1L);
    }
}