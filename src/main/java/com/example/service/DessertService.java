package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DessertDto;
import com.example.model.dto.LunchDto;
import com.example.model.entity.Dessert;
import com.example.model.entity.Lunch;
import com.example.model.request.DessertRequest;
import com.example.repository.DessertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private LunchService lunchService;

//    @Autowired
//    public void setLunchService(LunchService lunchService) {
//        this.lunchService = lunchService;
//    }

    @Transactional
    public DessertDto create(DessertRequest request) {
        Dessert entity = DessertRequest.toEntity(request);

//        if (isBlank(request.getLunchName())) {
//            throw new RuntimeException("Lunch should not be blank");
//        }

//        LunchDto lunchByName = lunchService.getByName(request.getName());
//        Lunch lunch = LunchDto.toEntity(lunchByName);
//        entity.setLunch(lunch);

        entity.setCreatedAt(LocalDateTime.now());
        Dessert saved = this.dessertRepository.save(entity);
        return DessertDto.toDto(saved);
    }

    public DessertDto getById(Long id) {
        Dessert found = this.dessertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        return DessertDto.toDto(found);
    }

    public DessertDto getByName(String name) {
        Dessert found = this.dessertRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Dessert.class));

        return DessertDto.toDto(found);
    }

    public Page<Dessert> findAll(Pageable pageable) {
        return this.dessertRepository.findAll(pageable);
    }

    public DessertDto updateById(Long id, DessertRequest dessertExists) {
        DessertDto byId = getById(id);

        if (isNotBlank(dessertExists.getName())) {
            byId.setName(dessertExists.getName());
        }
        if (dessertExists.getPortionWeight() != null) {
            byId.setPortionWeight(dessertExists.getPortionWeight());
        }
        byId.setUpdatedAt(LocalDateTime.now());

        Dessert entity = DessertDto.toEntity(byId);
        Dessert updated = dessertRepository.save(entity);
        return DessertDto.toDto(updated);
    }

    public void deleteById(Long id) {
        this.dessertRepository.deleteById(id);
    }
}
