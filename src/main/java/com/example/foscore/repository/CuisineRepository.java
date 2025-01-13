package com.example.foscore.repository;

import com.example.foscore.model.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

    Optional<Cuisine> findByName(String name);

}
