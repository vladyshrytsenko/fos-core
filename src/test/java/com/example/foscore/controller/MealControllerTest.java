package com.example.foscore.controller;

import com.example.foscore.StartupApplication;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.repository.CuisineRepository;
import com.example.foscore.repository.MealRepository;
import com.example.foscore.service.CuisineService;
import com.example.foscore.service.MealService;
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
public class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealService mealService;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mealRepository.deleteAll();
        this.cuisineRepository.deleteAll();

        CuisineRequest cuisineRequest = new CuisineRequest();
        cuisineRequest.setName("Italian");
        this.cuisineService.create(cuisineRequest);
    }

    @Test
    public void testCreateMeal() throws Exception {
        MealRequest request = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Italian");
            setPortionWeight(100);
            setPrice(3f);
        }};

        this.mockMvc.perform(post("/api/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Tacos"));

        List<Meal> meals = this.mealRepository.findAll();
        assertEquals(1, meals.size());
        assertEquals("Tacos", meals.getFirst().getName());
    }

    @Test
    public void testFindAllMeals() throws Exception {
        MealRequest request = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Italian");
            setPortionWeight(100);
            setPrice(3f);
        }};
        this.mealService.create(request);

        this.mockMvc.perform(get("/api/meals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Tacos"));
    }

    @Test
    public void testGetMealById() throws Exception {
        MealRequest request = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Italian");
            setPortionWeight(100);
            setPrice(3f);
        }};
        MealDto mealDto = this.mealService.create(request);

        this.mockMvc.perform(get("/api/meals/{id}", mealDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Tacos"));
    }

    @Test
    public void testUpdateMeal() throws Exception {
        MealRequest request = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Italian");
            setPortionWeight(100);
            setPrice(3f);
        }};
        MealDto mealDto = this.mealService.create(request);

        MealRequest updateRequest = new MealRequest();
        updateRequest.setName("Quesadilla");
        String updatedRequestAsString = objectMapper.writeValueAsString(updateRequest);

        this.mockMvc.perform(put("/api/meals/{id}", mealDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequestAsString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Quesadilla"));

        Meal updatedMeal = this.mealRepository.findById(mealDto.getId()).orElse(null);
        assertEquals("Quesadilla", updatedMeal.getName());
    }

    @Test
    public void testDeleteMeal() throws Exception {
        MealRequest request = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Italian");
            setPortionWeight(100);
            setPrice(3f);
        }};
        MealDto mealDto = this.mealService.create(request);

        this.mockMvc.perform(delete("/api/meals/{id}", mealDto.getId()))
            .andExpect(status().isNoContent());

        Optional<Meal> byId = this.mealRepository.findById(mealDto.getId());
        assertFalse(byId.isPresent());
    }
}
