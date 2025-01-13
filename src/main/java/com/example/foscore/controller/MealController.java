package com.example.foscore.controller;

import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealDto> create(
        @RequestBody @Valid MealRequest request
    ) {
        MealDto created = this.mealService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MealDto>> findAll(
        @RequestParam Optional<Integer> number,
        @RequestParam Optional<Integer> size) {

        PageRequest pageable = PageRequest.of(number.orElse(0), size.orElse(10));
        Page<Meal> page = this.mealService.findAll(pageable);
        List<MealDto> list = MealDto.toDtoList(page.getContent());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getById(@PathVariable Long id) {
        MealDto found = this.mealService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateById(
        @PathVariable Long id,
        @RequestBody @Valid MealRequest request
    ) {
        MealDto updated = this.mealService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.mealService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
