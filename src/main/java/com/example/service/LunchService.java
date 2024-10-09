package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.LunchDto;
import com.example.model.entity.Lunch;
import com.example.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LunchService {

    private final LunchRepository lunchRepository;

    public LunchDto create(LunchDto lunchDto) {
        Lunch entity = LunchDto.toEntity(lunchDto);
        Lunch saved = this.lunchRepository.save(entity);
        return LunchDto.toDto(saved);
    }

    public LunchDto getById(Long id) {
        Lunch found = this.lunchRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Lunch not found!"));

        return LunchDto.toDto(found);
    }

    public Page<Lunch> findAll(Pageable pageable) {
        return this.lunchRepository.findAll(pageable);
    }

    public LunchDto updateById(Long id, LunchDto lunchExists) {
        return null;
    }

    public void deleteById(Long id) {
        this.lunchRepository.deleteById(id);
    }
}
