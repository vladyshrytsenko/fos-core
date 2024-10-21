package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DessertDto;
import com.example.model.entity.Cuisine;
import com.example.model.entity.Dessert;
import com.example.model.request.DessertRequest;
import com.example.repository.DessertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class DessertService {

    private final DessertRepository dessertRepository;
    private final CuisineService cuisineService;

    @Transactional
    public DessertDto create(DessertRequest request) {
        Dessert entity = DessertRequest.toEntity(request);

        if (isBlank(request.getCuisineName())) {
            throw new RuntimeException("Cuisine should not be blank");
        }

        Cuisine cuisineByName = this.cuisineService.getByName(request.getCuisineName());
        entity.setCuisine(cuisineByName);

        entity.setCreatedAt(LocalDateTime.now());
        Dessert saved = this.dessertRepository.save(entity);
        return DessertDto.toDto(saved);
    }

    public DessertDto getById(Long id) {
        Dessert found = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        return DessertDto.toDto(found);
    }

    public Dessert getByName(String name) {
        return this.dessertRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));
    }

    public Page<Dessert> findAll(Pageable pageable) {
        return this.dessertRepository.findAll(pageable);
    }

    public DessertDto updateById(Long id, DessertRequest dessertExists) {
        Dessert byId = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        if (isNotBlank(dessertExists.getName())) {
            byId.setName(dessertExists.getName());
        }
        if (dessertExists.getPortionWeight() != null) {
            byId.setPortionWeight(dessertExists.getPortionWeight());
        }
        if (dessertExists.getPrice() != null) {
            byId.setPrice(dessertExists.getPrice());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Dessert updated = dessertRepository.save(byId);
        return DessertDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }
}
