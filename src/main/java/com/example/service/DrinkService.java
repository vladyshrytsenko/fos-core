package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DrinkDto;
import com.example.model.entity.Drink;
import com.example.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    public DrinkDto create(DrinkDto drinkDto) {
        Drink entity = DrinkDto.toEntity(drinkDto);
        Drink saved = this.drinkRepository.save(entity);
        return DrinkDto.toDto(saved);
    }

    public DrinkDto getById(Long id) {
        Drink found = this.drinkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Drink not found!"));

        return DrinkDto.toDto(found);
    }

    public Page<Drink> findAll(Pageable pageable) {
        return this.drinkRepository.findAll(pageable);
    }

    public DrinkDto updateById(Long id, DrinkDto drinkExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.drinkRepository.deleteById(id);
    }
}
