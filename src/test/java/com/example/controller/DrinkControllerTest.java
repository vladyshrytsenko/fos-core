package com.example.controller;

import com.example.StartupApplication;
import com.example.model.dto.DrinkDto;
import com.example.model.entity.Drink;
import com.example.model.request.DrinkRequest;
import com.example.repository.DrinkRepository;
import com.example.service.DrinkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StartupApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DrinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.drinkRepository.deleteAll();
    }

    @Test
    public void testCreateDrink() throws Exception {
        DrinkRequest request = new DrinkRequest();
        request.setName("Lemonade");
        request.setPrice(1f);

        this.mockMvc.perform(post("/api/drinks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Lemonade"));

        List<Drink> drinks = this.drinkRepository.findAll();
        assertEquals(1, drinks.size());
        assertEquals("Lemonade", drinks.getFirst().getName());
    }

    @Test
    public void testFindAllDrinks() throws Exception {
        DrinkRequest request = new DrinkRequest();
        request.setName("Lemonade");
        request.setPrice(1f);
        this.drinkService.create(request);

        this.mockMvc.perform(get("/api/drinks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Lemonade"));
    }

    @Test
    public void testGetDrinkById() throws Exception {
        DrinkRequest request = new DrinkRequest();
        request.setName("Lemonade");
        request.setPrice(1f);
        DrinkDto drinkDto = this.drinkService.create(request);

        this.mockMvc.perform(get("/api/drinks/{id}", drinkDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Lemonade"));
    }

    @Test
    public void testUpdateDrink() throws Exception {
        DrinkRequest request = new DrinkRequest();
        request.setName("Lemonade");
        request.setPrice(1f);
        DrinkDto drinkDto = this.drinkService.create(request);

        DrinkRequest updateRequest = new DrinkRequest();
        updateRequest.setName("Iced Tea");
        String updatedRequestAsString = objectMapper.writeValueAsString(updateRequest);

        this.mockMvc.perform(put("/api/drinks/{id}", drinkDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequestAsString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Iced Tea"));

        Drink updatedDrink = this.drinkRepository.findById(drinkDto.getId()).orElse(null);
        assertEquals("Iced Tea", updatedDrink.getName());
    }

    @Test
    public void testDeleteDrink() throws Exception {
        DrinkRequest request = new DrinkRequest();
        request.setName("Lemonade");
        request.setPrice(1f);
        DrinkDto drinkDto = this.drinkService.create(request);

        this.mockMvc.perform(delete("/api/drinks/{id}", drinkDto.getId()))
            .andExpect(status().isNoContent());

        Optional<Drink> byId = this.drinkRepository.findById(drinkDto.getId());
        assertFalse(byId.isPresent());
    }
}

