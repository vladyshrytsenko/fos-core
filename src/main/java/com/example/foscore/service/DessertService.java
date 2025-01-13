package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.request.DessertRequest;
import com.example.foscore.repository.DessertRepository;
import com.stripe.model.Price;
import com.stripe.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class DessertService {

    private final DessertRepository dessertRepository;
    private final StripeService stripeService;

    @Transactional
    public DessertDto create(DessertRequest request) {
        Dessert dessertToSave = DessertRequest.toEntity(request);
        Dessert createdDessert = this.dessertRepository.save(dessertToSave);

        Product stripeProduct = this.stripeService.createProduct(createdDessert.getName());
        Price stripePrice = this.stripeService.createPrice(stripeProduct.getId(), createdDessert.getPrice());

        return DessertDto.toDto(createdDessert);
    }

    public DessertDto getById(Long id) {
        Dessert dessertById = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        return DessertDto.toDto(dessertById);
    }

    public Dessert getByName(String name) {
        return this.dessertRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));
    }

    public Page<Dessert> findAll(Pageable pageable) {
        return this.dessertRepository.findAll(pageable);
    }

    public DessertDto updateById(Long id, DessertRequest dessertExists) {
        Dessert dessertById = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        if (isNotBlank(dessertExists.getName())) {
            dessertById.setName(dessertExists.getName());
        }
        if (dessertExists.getPortionWeight() != null) {
            dessertById.setPortionWeight(dessertExists.getPortionWeight());
        }
        if (dessertExists.getPrice() != null) {
            dessertById.setPrice(dessertExists.getPrice());
        }

        Dessert updatedDessert = dessertRepository.save(dessertById);
        return DessertDto.toDto(updatedDessert);
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }
}
