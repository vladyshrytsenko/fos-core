package com.example.foscore.controller;

import com.example.foscore.model.dto.CuisineDto;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.service.CuisineService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuisines")
@RequiredArgsConstructor
public class CuisineController {

    @PostMapping
    public ResponseEntity<CuisineDto> create(
        @RequestBody @Valid CuisineRequest request
    ) {
        CuisineDto created = this.cuisineService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<CuisineDto> findAll(Pageable pageable) {
        return this.cuisineService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineDto> getById(@PathVariable Long id) {
        CuisineDto found = this.cuisineService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineDto> updateById(
        @PathVariable Long id,
        @RequestBody @Valid CuisineRequest request
    ) {
        CuisineDto updated = this.cuisineService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.cuisineService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private final CuisineService cuisineService;
}
