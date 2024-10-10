package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.LunchDto;
import com.example.model.dto.MealDto;
import com.example.model.entity.Lunch;
import com.example.model.entity.Meal;
import com.example.model.request.MealRequest;
import com.example.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
//    private LunchService lunchService;

//    @Autowired
//    public void setLunchService(LunchService lunchService) {
//        this.lunchService = lunchService;
//    }

    @Transactional
    public MealDto create(MealRequest request) {
        Meal entity = MealRequest.toEntity(request);

//        if (isBlank(request.getLunchName())) {
//            throw new RuntimeException("Lunch should not be blank");
//        }

//        LunchDto lunchByName = lunchService.getByName(request.getName());
//        Lunch lunch = LunchDto.toEntity(lunchByName);
//        entity.setLunch(lunch);

        Meal saved = this.mealRepository.save(entity);
        return MealDto.toDto(saved);
    }

    public MealDto getById(Long id) {
        Meal found = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        return MealDto.toDto(found);
    }

    public MealDto getByName(String name) {
        Meal found = this.mealRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        return MealDto.toDto(found);
    }

    public Page<Meal> findAll(Pageable pageable) {
        return this.mealRepository.findAll(pageable);
    }

    public MealDto updateById(Long id, MealRequest mealExists) {
        MealDto byId = getById(id);

        if (StringUtils.isNotBlank(mealExists.getName())) {
            byId.setName(mealExists.getName());
        }
        if (mealExists.getPortionWeight() != null) {
            byId.setPortionWeight(mealExists.getPortionWeight());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Meal entity = MealDto.toEntity(byId);
        Meal updated = mealRepository.save(entity);
        return MealDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.mealRepository.deleteById(id);
    }
}
