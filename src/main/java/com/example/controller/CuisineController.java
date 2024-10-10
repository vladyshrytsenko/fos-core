package com.example.controller;

import com.example.model.dto.CuisineDto;
import com.example.model.entity.Cuisine;
import com.example.model.request.CuisineRequest;
import com.example.service.CuisineService;
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

import java.util.List;

@RestController
@RequestMapping("/api/cuisines")
@RequiredArgsConstructor
public class CuisineController {

    private final CuisineService cuisineService;

    @PostMapping
    public ResponseEntity<CuisineDto> create(
        @RequestBody CuisineRequest request
    ) {
        CuisineDto created = this.cuisineService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CuisineDto>> findAll(Pageable pageable) {
        Page<Cuisine> page = this.cuisineService.findAll(pageable);
        List<CuisineDto> list = CuisineDto.toDtoList(page.getContent());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineDto> getById(@PathVariable Long id) {
        CuisineDto found = this.cuisineService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineDto> updateById(
        @PathVariable Long id,
        @RequestBody CuisineRequest request
    ) {
        CuisineDto updated = this.cuisineService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.cuisineService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
