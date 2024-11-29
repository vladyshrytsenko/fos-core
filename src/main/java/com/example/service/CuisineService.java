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

import static com.example.model.dto.CuisineDto.*;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineDto create(CuisineRequest request) {
        Cuisine cuisineToSave = CuisineRequest.toEntity(request);
        Cuisine createdCuisine = this.cuisineRepository.save(cuisineToSave);
        return toDto(createdCuisine);
    }

    public CuisineDto getById(Long id) {
        Cuisine cuisineById = this.cuisineRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Cuisine.class));

        return toDto(cuisineById);
    }

    public Cuisine getByName(String name) {
        return this.cuisineRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Cuisine.class));
    }

    public Page<Cuisine> findAll(Pageable pageable) {
        return this.cuisineRepository.findAll(pageable);
    }

    public CuisineDto updateById(Long id, CuisineRequest cuisineExists) {
        CuisineDto cuisineById = this.getById(id);

        if (StringUtils.isNotBlank(cuisineExists.getName())) {
            cuisineById.setName(cuisineExists.getName());
        }

        Cuisine cuisineToUpdate = toEntity(cuisineById);
        Cuisine updatedCuisine = cuisineRepository.save(cuisineToUpdate);
        return toDto(updatedCuisine);
    }

    public void deleteById(Long id) {
        this.cuisineRepository.deleteById(id);
    }
}
