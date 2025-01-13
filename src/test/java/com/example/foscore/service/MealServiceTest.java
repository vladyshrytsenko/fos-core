package com.example.foscore.service;

import com.example.foscore.MockData;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.repository.MealRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private CuisineService cuisineService;

    @InjectMocks
    private MealService mealService;

    @Test
    void create() {
        MealRequest request = MockData.mealRequest();

        Meal entity = MealRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Meal savedMeal = MockData.meal();

        when(this.mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

        MealDto result = this.mealService.create(request);

        assertNotNull(result);
        assertEquals("meal_mock", result.getName());
        verify(this.mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void getById() {
        Meal meal = MockData.meal();

        when(this.mealRepository.findById(anyLong())).thenReturn(Optional.of(meal));

        MealDto result = this.mealService.getById(1L);

        assertNotNull(result);
        assertEquals("meal_mock", result.getName());
        verify(this.mealRepository, times(1)).findById(1L);
    }

    @Test
    void getByName() {
        Meal meal = MockData.meal();

        when(this.mealRepository.findByName("meal_mock")).thenReturn(Optional.of(meal));

        Meal result = this.mealService.getByName("meal_mock");

        assertNotNull(result);
        assertEquals("meal_mock", result.getName());
        verify(this.mealRepository, times(1)).findByName("meal_mock");
    }

    @Test
    void findAll() {
        Page<Meal> page = new PageImpl<>(MockData.mealList());
        when(this.mealRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Meal> result = this.mealService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(this.mealRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateById() {
        MealRequest request = new MealRequest();
        request.setName("meal_updated");
        request.setPortionWeight(234);

        Meal meal = MockData.meal();

        Meal updated = MockData.updatedMeal();

        when(this.mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(this.mealRepository.save(any(Meal.class))).thenReturn(updated);

        MealDto result = this.mealService.updateById(1L, request);

        assertNotNull(result);
        assertEquals("meal_updated", result.getName());
        verify(this.mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void deleteById() {
        doNothing().when(this.mealRepository).deleteById(1L);
        this.mealService.deleteById(1L);
        verify(this.mealRepository, times(1)).deleteById(1L);
    }
}