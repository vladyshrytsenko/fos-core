package com.example.foscore.service.rabbitmq;

import com.example.foscore.config.RabbitMQConfig;
import com.example.foscore.model.enums.DishType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopularDishesProducer {

    public void sendOrderEvent(DishType type, String dishName) {
        this.rabbitTemplate.convertAndSend(
            RabbitMQConfig.POPULAR_DISHES_QUEUE,
            type.name().concat(":").concat(dishName)
        );
    }

    private final RabbitTemplate rabbitTemplate;
}
