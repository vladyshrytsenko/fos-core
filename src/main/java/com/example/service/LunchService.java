package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.CuisineDto;
import com.example.model.dto.DessertDto;
import com.example.model.dto.LunchDto;
import com.example.model.dto.MealDto;
import com.example.model.entity.Cuisine;
import com.example.model.entity.Dessert;
import com.example.model.entity.Lunch;
import com.example.model.entity.Meal;
import com.example.model.request.LunchRequest;
import com.example.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class LunchService {

    private final LunchRepository lunchRepository;
    private CuisineService cuisineService;
    private MealService mealService;
    private DessertService dessertService;

    @Autowired
    public void setCuisineService(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Autowired
    public void setDessertService(DessertService dessertService) {
        this.dessertService = dessertService;
    }

    @Transactional
    public LunchDto create(LunchRequest request) {
        Lunch entity = LunchRequest.toEntity(request);

        if (isBlank(request.getCuisineName()) ||
            isBlank(request.getMealName()) ||
            isBlank(request.getDessertName())) {
            throw new RuntimeException("Cuisine or Meal or Dessert should not be blank");
        }

        CuisineDto cuisineByName = this.cuisineService.getByName(request.getCuisineName());
        Cuisine cuisine = CuisineDto.toEntity(cuisineByName);
        entity.setCuisine(cuisine);

        MealDto mealByName = this.mealService.getByName(request.getMealName());
        Meal meal = MealDto.toEntity(mealByName);
        entity.setMeal(meal);

        DessertDto dessertByName = this.dessertService.getByName(request.getDessertName());
        Dessert dessert = DessertDto.toEntity(dessertByName);
        entity.setDessert(dessert);

        Lunch saved = this.lunchRepository.save(entity);
        return LunchDto.toDto(saved);
    }

    public LunchDto getById(Long id) {
        Lunch found = this.lunchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Lunch.class));

        return LunchDto.toDto(found);
    }

    public LunchDto getByName(String name) {
        Lunch found = this.lunchRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Lunch.class));

        return LunchDto.toDto(found);
    }

    public Page<Lunch> findAll(Pageable pageable) {
        return this.lunchRepository.findAll(pageable);
    }

    public LunchDto updateById(Long id, LunchRequest lunchExists) {
        LunchDto byId = getById(id);

        if (isNotBlank(lunchExists.getName())) {
            byId.setName(lunchExists.getName());
        }
        if (lunchExists.getPrice() != null) {
            byId.setPrice(lunchExists.getPrice());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Lunch entity = LunchDto.toEntity(byId);
        Lunch updated = lunchRepository.save(entity);
        return LunchDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.lunchRepository.deleteById(id);
    }
}
