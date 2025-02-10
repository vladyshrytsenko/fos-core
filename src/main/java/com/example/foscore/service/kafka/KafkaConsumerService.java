package com.example.foscore.service.kafka;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Getter
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "popular-dishes", groupId = "popular-dishes-group")
    public void consumeOrderEvent(String dishName) {
        dishPopularity.merge(dishName, 1, Integer::sum);
    }

    private final Map<String, Integer> dishPopularity = new ConcurrentHashMap<>();
}
