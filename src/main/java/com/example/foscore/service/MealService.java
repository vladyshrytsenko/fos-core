package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Cuisine;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.repository.MealRepository;
import com.stripe.model.Price;
import com.stripe.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MealService {

    @Transactional
    public MealDto create(MealRequest request) {
        Meal mealToSave = MealRequest.toEntity(request);

        Cuisine cuisineByName = this.cuisineService.getByName(request.getCuisineName());
        mealToSave.setCuisine(cuisineByName);
        Meal createdMeal = this.mealRepository.save(mealToSave);

        Product stripeProduct = this.stripeService.createProduct(createdMeal.getName());
        Price stripePrice = this.stripeService.createPrice(stripeProduct.getId(), createdMeal.getPrice());

        return MealDto.toDto(createdMeal);
    }

    public MealDto getById(Long id) {
        Meal mealById = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        return MealDto.toDto(mealById);
    }

    public MealDto getByName(String name) {
        Meal mealByName = this.mealRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        return MealDto.toDto(mealByName);
    }

    public Meal getEntityByName(String name) {
        return this.mealRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));
    }

    public Page<MealDto> findAll(Pageable pageable) {
        Page<Meal> mealPage = this.mealRepository.findAll(pageable);
        return mealPage.map(MealDto::toDto);
    }

    public MealDto updateById(Long id, MealRequest mealExists) {
        Meal mealById = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        mealById.setName(mealExists.getName());
        mealById.setPortionWeight(mealExists.getPortionWeight());
        mealById.setPrice(mealExists.getPrice());

        Meal updatedMeal = mealRepository.save(mealById);
        return MealDto.toDto(updatedMeal);
    }

    public void deleteById(Long id) {
        this.mealRepository.deleteById(id);
    }

    private final MealRepository mealRepository;
    private final CuisineService cuisineService;
    private final StripeService stripeService;
}
