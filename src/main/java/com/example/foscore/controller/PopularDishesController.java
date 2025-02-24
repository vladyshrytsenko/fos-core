package com.example.foscore.controller;

import com.example.foscore.service.rabbitmq.PopularDishesConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popular")
public class PopularDishesController {

    @GetMapping
    public Map<String, Set<String>> getPopularDishes() {
        return popularDishesConsumer.getPopularDishesMap();
    }

    private final PopularDishesConsumer popularDishesConsumer;
}
