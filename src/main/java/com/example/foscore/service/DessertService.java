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

@Service
@RequiredArgsConstructor
public class DessertService {

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

    public DessertDto getBy(String name) {
        Dessert dessertByName = this.dessertRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        return DessertDto.toDto(dessertByName);
    }

    public Dessert getEntityByName(String name) {
        return this.dessertRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));
    }

    public Page<DessertDto> findAll(Pageable pageable) {
        Page<Dessert> dessertPage = this.dessertRepository.findAll(pageable);
        return dessertPage.map(DessertDto::toDto);
    }

    public DessertDto updateById(Long id, DessertRequest dessertExists) {
        Dessert dessertById = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        dessertById.setName(dessertExists.getName());
        dessertById.setPortionWeight(dessertExists.getPortionWeight());
        dessertById.setPrice(dessertExists.getPrice());

        Dessert updatedDessert = dessertRepository.save(dessertById);
        return DessertDto.toDto(updatedDessert);
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }

    private final DessertRepository dessertRepository;
    private final StripeService stripeService;
}
