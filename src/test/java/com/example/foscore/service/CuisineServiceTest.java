package com.example.foscore.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.foscore.MockData;
import com.example.foscore.model.dto.CuisineDto;
import com.example.foscore.model.entity.Cuisine;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.repository.CuisineRepository;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuisineServiceTest {

    @Mock
    private CuisineRepository cuisineRepository;

    @InjectMocks
    private CuisineService cuisineService;

    @Test
    public void create() {
        CuisineRequest request = MockData.cuisineRequest();
        Cuisine entity = CuisineRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Cuisine savedCuisine = MockData.cuisine();

        when(this.cuisineRepository.save(any(Cuisine.class))).thenReturn(savedCuisine);

        CuisineDto result = this.cuisineService.create(request);

        assertNotNull(result);
        assertEquals("Mexican", result.getName());
        verify(this.cuisineRepository, times(1)).save(any(Cuisine.class));
    }

    @Test
    public void getById() {
        Cuisine cuisine = MockData.cuisine();

        when(this.cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));

        CuisineDto result = this.cuisineService.getById(1L);

        assertNotNull(result);
        assertEquals("Mexican", result.getName());
        verify(this.cuisineRepository, times(1)).findById(1L);
    }

    @Test
    public void getByName() {
        Cuisine cuisine = MockData.cuisine();

        when(this.cuisineRepository.findByName(anyString())).thenReturn(Optional.of(cuisine));

        Cuisine result = this.cuisineService.getByName("Mexican");

        assertNotNull(result);
        assertEquals("Mexican", result.getName());
        verify(this.cuisineRepository, times(1)).findByName("Mexican");
    }

    @Test
    public void findAll() {
        Page<Cuisine> page = new PageImpl<>(MockData.cuisineList());
        when(this.cuisineRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Cuisine> result = this.cuisineService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(this.cuisineRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void updateById() {
        CuisineRequest request = new CuisineRequest("Polish");

        Cuisine cuisine = MockData.cuisine();

        Cuisine updatedCuisine = MockData.updatedCuisine();

        when(this.cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
        when(this.cuisineRepository.save(any(Cuisine.class))).thenReturn(updatedCuisine);

        CuisineDto result = this.cuisineService.updateById(1L, request);

        assertNotNull(result);
        assertEquals("Polish", result.getName());
        verify(this.cuisineRepository, times(1)).save(any(Cuisine.class));
    }

    @Test
    public void deleteById() {
        doNothing().when(this.cuisineRepository).deleteById(1L);
        this.cuisineService.deleteById(1L);
        verify(this.cuisineRepository, times(1)).deleteById(1L);
    }
}
