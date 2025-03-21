package com.example.foscore.controller;

import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.request.DessertRequest;
import com.example.foscore.service.DessertService;
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
@RequestMapping("/api/desserts")
@RequiredArgsConstructor
public class DessertController {

    @PostMapping
    public ResponseEntity<DessertDto> create(
        @RequestBody @Valid DessertRequest request) {

        DessertDto created = this.dessertService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<DessertDto> findAll(Pageable pageable) {
        return this.dessertService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DessertDto> getById(@PathVariable Long id) {
        DessertDto found = this.dessertService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/getBy")
    public ResponseEntity<DessertDto> getBy(@RequestParam String name) {
        DessertDto found = this.dessertService.getBy(name);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DessertDto> updateById(
        @PathVariable Long id,
        @RequestBody @Valid DessertRequest request) {

        DessertDto updated = this.dessertService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.dessertService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        this.dessertService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    private final DessertService dessertService;
}
