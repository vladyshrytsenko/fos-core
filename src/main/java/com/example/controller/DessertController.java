package com.example.controller;

import com.example.model.dto.DessertDto;
import com.example.model.entity.Dessert;
import com.example.service.DessertService;
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
@RequestMapping("/api/desserts")
@RequiredArgsConstructor
public class DessertController {

    private final DessertService dessertService;

    @PostMapping
    public ResponseEntity<DessertDto> create(
        @RequestBody DessertDto dessertDto
    ) {
        DessertDto created = this.dessertService.create(dessertDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DessertDto>> findAll(Pageable pageable) {
        Page<Dessert> page = this.dessertService.findAll(pageable);
        List<DessertDto> list = DessertDto.toDtoList(page.getContent());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DessertDto> getById(@PathVariable Long id) {
        DessertDto found = this.dessertService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DessertDto> updateById(
        @PathVariable Long id,
        @RequestBody DessertDto dessertDto
    ) {
        DessertDto updated = this.dessertService.updateById(id, dessertDto);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.dessertService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
