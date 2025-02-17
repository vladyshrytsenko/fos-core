package com.example.foscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.foscore"})
@EntityScan(basePackages = {"com.example.foscore.model.entity"})
@EnableJpaRepositories(basePackages = {"com.example.foscore.repository"})
@EnableScheduling
@EnableFeignClients
public class StartupApplicationCore {

    public static void main(String[] args) {
        SpringApplication.run(StartupApplicationCore.class, args);
    }

}
