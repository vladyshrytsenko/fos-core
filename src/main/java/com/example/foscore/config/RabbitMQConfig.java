package com.example.foscore.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue popularDishesQueue() {
        return new Queue(POPULAR_DISHES_QUEUE, false);
    }

    @Bean
    public DirectExchange popularDishesExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder
            .bind(queue)
            .to(directExchange)
            .with(POPULAR_DISHES_QUEUE);
    }

    public static final String POPULAR_DISHES_QUEUE = "popular-dishes";
    public static final String EXCHANGE_NAME = "popular-dishes-exchange";
}
