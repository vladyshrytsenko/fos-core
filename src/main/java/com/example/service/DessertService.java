package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DessertDto;
import com.example.model.entity.Dessert;
import com.example.repository.DessertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DessertService {

    private final DessertRepository dessertRepository;

    public DessertDto create(DessertDto dessertDto) {
        Dessert entity = DessertDto.toEntity(dessertDto);
        Dessert saved = this.dessertRepository.save(entity);
        return DessertDto.toDto(saved);
    }

    public DessertDto getById(Long id) {
        Dessert found = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Dessert not found!"));

        return DessertDto.toDto(found);
    }

    public Page<Dessert> findAll(Pageable pageable) {
        return this.dessertRepository.findAll(pageable);
    }

    public DessertDto updateById(Long id, DessertDto dessertExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }
}
