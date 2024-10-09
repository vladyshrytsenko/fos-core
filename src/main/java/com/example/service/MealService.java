package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.MealDto;
import com.example.model.entity.Meal;
import com.example.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public MealDto create(MealDto mealDto) {
        Meal entity = MealDto.toEntity(mealDto);
        Meal saved = this.mealRepository.save(entity);
        return MealDto.toDto(saved);
    }

    public MealDto getById(Long id) {
        Meal found = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Meal not found!"));

        return MealDto.toDto(found);
    }

    public Page<Meal> findAll(Pageable pageable) {
        return this.mealRepository.findAll(pageable);
    }

    public MealDto updateById(Long id, MealDto mealExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.mealRepository.deleteById(id);
    }
}
