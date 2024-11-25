package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DessertDto;
import com.example.model.entity.Dessert;
import com.example.model.request.DessertRequest;
import com.example.repository.DessertRepository;
import com.stripe.model.Price;
import com.stripe.model.Product;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class DessertService {

    private final Validator validator;
    private final DessertRepository dessertRepository;
    private final StripeService stripeService;

    @Transactional
    public DessertDto create(DessertRequest request) {
        Dessert entity = DessertRequest.toEntity(request);

        entity.setCreatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Dessert>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Dessert saved = this.dessertRepository.save(entity);

        Product stripeProduct = this.stripeService.createProduct(saved.getName());
        Price stripePrice = this.stripeService.createPrice(stripeProduct.getId(), saved.getPrice());

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

        Set<ConstraintViolation<Dessert>> violations = validator.validate(byId);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Dessert updated = dessertRepository.save(byId);
        return DessertDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }
}
