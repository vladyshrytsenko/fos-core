package com.example.foscore.service.rabbitmq;

import com.example.foscore.config.RabbitMQConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@EnableRabbit
@Component
@RequiredArgsConstructor
public class PopularDishesConsumer {

    @RabbitListener(queues = RabbitMQConfig.POPULAR_DISHES_QUEUE)
    public void consumeOrderEvent(String dishContent) {
        String[] parts = dishContent.split(":", 2);

        if (parts.length == 2) {
            String type = parts[0].toUpperCase();
            String object = parts[1];

            popularDishesMap
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(object);
        }
    }

    private final Map<String, Set<String>> popularDishesMap = new HashMap<>();
}
