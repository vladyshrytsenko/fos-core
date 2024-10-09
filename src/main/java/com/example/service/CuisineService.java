package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.CuisineDto;
import com.example.model.entity.Cuisine;
import com.example.repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.model.dto.CuisineDto.*;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineDto create(CuisineDto cuisineDto) {
        Cuisine entity = CuisineDto.toEntity(cuisineDto);
        Cuisine saved = this.cuisineRepository.save(entity);
        return toDto(saved);
    }

    public CuisineDto getById(Long id) {
        Cuisine found = this.cuisineRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cuisine not found!"));

        return toDto(found);
    }

    public Page<Cuisine> findAll(Pageable pageable) {
        return this.cuisineRepository.findAll(pageable);
    }

    public CuisineDto updateById(Long id, CuisineDto cuisineExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.cuisineRepository.deleteById(id);
    }
}
