package com.example.foscore.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    public void sendOrderEvent(String dishName) {
        kafkaTemplate.send("popular-dishes", dishName);
    }

    private final KafkaTemplate<String, String> kafkaTemplate;
}
