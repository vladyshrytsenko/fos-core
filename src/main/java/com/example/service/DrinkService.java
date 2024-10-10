package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DrinkDto;
import com.example.model.entity.Drink;
import com.example.model.request.DrinkRequest;
import com.example.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    public DrinkDto create(DrinkRequest request) {
        Drink entity = DrinkRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Drink saved = this.drinkRepository.save(entity);
        return DrinkDto.toDto(saved);
    }

    public DrinkDto getById(Long id) {
        Drink found = this.drinkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Drink.class));

        return DrinkDto.toDto(found);
    }

    public DrinkDto getByName(String name) {
        Drink found = this.drinkRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Drink.class));

        return DrinkDto.toDto(found);
    }

    public Page<Drink> findAll(Pageable pageable) {
        return this.drinkRepository.findAll(pageable);
    }

    public DrinkDto updateById(Long id, DrinkRequest drinkExists) {
        DrinkDto byId = getById(id);

        if (StringUtils.isNotBlank(drinkExists.getName())) {
            byId.setName(drinkExists.getName());
        }
        if (drinkExists.getPrice() != null) {
            byId.setPrice(drinkExists.getPrice());
        }
        if (drinkExists.getIceCubes() != null) {
            byId.setIceCubes(drinkExists.getIceCubes());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Drink entity = DrinkDto.toEntity(byId);
        Drink updated = drinkRepository.save(entity);
        return DrinkDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.drinkRepository.deleteById(id);
    }
}
