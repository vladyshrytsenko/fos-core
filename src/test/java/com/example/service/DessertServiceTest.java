package com.example.service;

import com.example.MockData;
import com.example.model.dto.DessertDto;
import com.example.model.entity.Cuisine;
import com.example.model.entity.Dessert;
import com.example.model.request.DessertRequest;
import com.example.repository.DessertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DessertServiceTest {

    @Mock
    private DessertRepository dessertRepository;

    @Mock
    private CuisineService cuisineService;

    @InjectMocks
    private DessertService dessertService;

    @Test
    void create() {
        DessertRequest request = MockData.dessertRequest();

        Dessert entity = DessertRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Dessert savedDessert = MockData.dessert();

        when(this.dessertRepository.save(any(Dessert.class))).thenReturn(savedDessert);

        DessertDto result = this.dessertService.create(request);

        assertNotNull(result);
        assertEquals("dessert_mock", result.getName());
        verify(this.dessertRepository, times(1)).save(any(Dessert.class));
    }

    @Test
    void getById() {
        Dessert dessert = MockData.dessert();

        when(this.dessertRepository.findById(anyLong())).thenReturn(Optional.of(dessert));

        DessertDto result = this.dessertService.getById(1L);

        assertNotNull(result);
        assertEquals("dessert_mock", result.getName());
        verify(this.dessertRepository, times(1)).findById(1L);
    }

    @Test
    void getByName() {
        Dessert dessert = MockData.dessert();

        when(this.dessertRepository.findByName(anyString())).thenReturn(Optional.of(dessert));

        Dessert result = this.dessertService.getByName("dessert_mock");

        assertNotNull(result);
        assertEquals("dessert_mock", result.getName());
        verify(this.dessertRepository, times(1)).findByName("dessert_mock");
    }

    @Test
    void findAll() {
        Page<Dessert> page = new PageImpl<>(MockData.dessertList());
        when(this.dessertRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Dessert> result = this.dessertService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(this.dessertRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateById() {
        DessertRequest request = new DessertRequest();
        request.setName("updated_dessert_mock");

        Dessert dessert = MockData.dessert();
        Dessert updated = MockData.updatedDessert();

        when(this.dessertRepository.findById(1L)).thenReturn(Optional.of(dessert));
        when(this.dessertRepository.save(any(Dessert.class))).thenReturn(updated);

        DessertDto result = this.dessertService.updateById(1L, request);

        assertNotNull(result);
        assertEquals("updated_dessert_mock", result.getName());
        verify(this.dessertRepository, times(1)).save(any(Dessert.class));
    }

    @Test
    void deleteById() {
        doNothing().when(this.dessertRepository).deleteById(1L);
        this.dessertService.deleteById(1L);
        verify(this.dessertRepository, times(1)).deleteById(1L);
    }
}