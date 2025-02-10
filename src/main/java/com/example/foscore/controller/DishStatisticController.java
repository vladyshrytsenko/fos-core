package com.example.foscore.controller;

import com.example.foscore.service.kafka.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popular-dishes")
public class DishStatisticController {

    private final KafkaConsumerService kafkaConsumerService;

    @GetMapping
    public Map<String, Integer> getPopularDishes() {
        return kafkaConsumerService.getDishPopularity();
    }
}

