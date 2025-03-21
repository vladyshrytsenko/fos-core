package com.example.foscore.controller;

import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

    @PostMapping
    public ResponseEntity<MealDto> create(
        @RequestBody @Valid MealRequest request) {

        MealDto created = this.mealService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<MealDto> findAll(Pageable pageable) {
        return this.mealService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getById(@PathVariable Long id) {
        MealDto found = this.mealService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/getBy")
    public ResponseEntity<MealDto> getBy(@RequestParam String name) {
        MealDto found = this.mealService.getBy(name);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateById(
        @PathVariable Long id,
        @RequestBody @Valid MealRequest request) {

        MealDto updated = this.mealService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.mealService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        this.mealService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    private final MealService mealService;
}
