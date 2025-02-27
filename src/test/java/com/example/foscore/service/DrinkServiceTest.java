package com.example.foscore.service;

import com.example.foscore.MockData;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.request.DrinkRequest;
import com.example.foscore.repository.DrinkRepository;
import com.stripe.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrinkServiceTest {

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private DrinkService drinkService;

    @Test
    void create() {
        DrinkRequest request = MockData.drinkRequest();

        Drink entity = DrinkRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Drink savedDrink = MockData.drink();
        Product createdProduct = Mockito.mock(Product.class);

        when(this.stripeService.createProduct(savedDrink.getName())).thenReturn(createdProduct);
        when(createdProduct.getId()).thenReturn("product_id");
        when(this.drinkRepository.save(any(Drink.class))).thenReturn(savedDrink);

        DrinkDto result = this.drinkService.create(request);

        assertNotNull(result);
        assertEquals("lemonade", result.getName());
        verify(this.drinkRepository, times(1)).save(any(Drink.class));
    }

    @Test
    void getById() {
        Drink drink = MockData.drink();

        when(this.drinkRepository.findById(1L)).thenReturn(Optional.of(drink));

        DrinkDto result = this.drinkService.getById(1L);

        assertNotNull(result);
        assertEquals("lemonade", result.getName());
        verify(this.drinkRepository, times(1)).findById(1L);
    }

    @Test
    void getEntityByName() {
        Drink drink = MockData.drink();

        when(this.drinkRepository.findByName("lemonade")).thenReturn(Optional.of(drink));

        Drink result = this.drinkService.getEntityByName("lemonade");

        assertNotNull(result);
        assertEquals("lemonade", result.getName());
        verify(this.drinkRepository, times(1)).findByName("lemonade");
    }

    @Test
    void findAll() {
        Page<Drink> page = new PageImpl<>(MockData.drinkList());
        when(this.drinkRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Drink> result = this.drinkService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(this.drinkRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateById() {
        DrinkRequest request = new DrinkRequest();
        request.setName("lemonade2");

        Drink drink = MockData.drink();

        Drink updated = drink;
        updated.setName("lemonade2");
        updated.setUpdatedAt(LocalDateTime.now());

        when(this.drinkRepository.findById(1L)).thenReturn(Optional.of(drink));
        when(this.drinkRepository.save(any(Drink.class))).thenReturn(updated);

        DrinkDto result = this.drinkService.updateById(1L, request);

        assertNotNull(result);
        assertEquals("lemonade2", result.getName());
        verify(this.drinkRepository, times(1)).save(any(Drink.class));
    }

    @Test
    void deleteById() {
        doNothing().when(this.drinkRepository).deleteById(1L);
        this.drinkService.deleteById(1L);
        verify(this.drinkRepository, times(1)).deleteById(1L);
    }
}