package com.example.repository;

import com.example.model.entity.Lunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    Optional<Lunch> findByName(String name);
}
