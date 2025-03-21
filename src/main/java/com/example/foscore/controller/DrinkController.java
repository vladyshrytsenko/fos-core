package com.example.foscore.controller;

import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.request.DrinkRequest;
import com.example.foscore.service.DrinkService;
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
@RequestMapping("/api/drinks")
public class DrinkController {

    @PostMapping
    public ResponseEntity<DrinkDto> create(
        @RequestBody @Valid DrinkRequest request) {

        DrinkDto created = this.drinkService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<DrinkDto> findAll(Pageable pageable) {
        return this.drinkService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkDto> getById(@PathVariable Long id) {
        DrinkDto found = this.drinkService.getById(id);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/getBy")
    public ResponseEntity<DrinkDto> getBy(@RequestParam String name) {
        DrinkDto found = this.drinkService.getBy(name);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkDto> updateById(
        @PathVariable Long id,
        @RequestBody @Valid DrinkRequest request) {

        DrinkDto updated = this.drinkService.updateById(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.drinkService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        this.drinkService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    private final DrinkService drinkService;
}
