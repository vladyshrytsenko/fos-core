package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.CuisineDto;
import com.example.model.entity.Cuisine;
import com.example.model.request.CuisineRequest;
import com.example.repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.model.dto.CuisineDto.*;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineDto create(CuisineRequest request) {
        Cuisine entity = CuisineRequest.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        Cuisine saved = this.cuisineRepository.save(entity);
        return toDto(saved);
    }

    public CuisineDto getById(Long id) {
        Cuisine found = this.cuisineRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Cuisine.class));

        return toDto(found);
    }

    public CuisineDto getByName(String name) {
        Cuisine found = this.cuisineRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Cuisine.class));

        return CuisineDto.toDto(found);
    }

    public Page<Cuisine> findAll(Pageable pageable) {
        return this.cuisineRepository.findAll(pageable);
    }

    public CuisineDto updateById(Long id, CuisineRequest cuisineExists) {
        CuisineDto byId = getById(id);

        if (StringUtils.isNotBlank(cuisineExists.getName())) {
            byId.setName(cuisineExists.getName());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Cuisine entity = toEntity(byId);
        Cuisine updated = cuisineRepository.save(entity);
        return toDto(updated);
    }

    public void deleteById(Long id) {
        this.cuisineRepository.deleteById(id);
    }
}
