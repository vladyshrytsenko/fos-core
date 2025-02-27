package com.example.foscore.controller;

import com.example.foscore.StartupApplicationCore;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.model.request.DessertRequest;
import com.example.foscore.repository.CuisineRepository;
import com.example.foscore.repository.DessertRepository;
import com.example.foscore.security.SecurityTestConfig;
import com.example.foscore.service.CuisineService;
import com.example.foscore.service.DessertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = { StartupApplicationCore.class, SecurityTestConfig.class })
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class DessertControllerTest {

    @BeforeEach
    public void setUp() {
        this.dessertRepository.deleteAll();
        this.cuisineRepository.deleteAll();

        CuisineRequest cuisineRequest = new CuisineRequest();
        cuisineRequest.setName("Italian");
        this.cuisineService.create(cuisineRequest);
    }

    @Test
    public void testCreateDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        String dessertRequestAsString = this.objectMapper.writeValueAsString(dessertRequest);

        this.mockMvc.perform(
            post("/api/desserts")
                .header("Authorization-Test", "X-Authorization-test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dessertRequestAsString)
            )
            .andExpectAll(
                status().isCreated(),
                jsonPath("$.name").value("Chocolate Cake")
            );

        List<Dessert> desserts = this.dessertRepository.findAll();
        assertEquals(1, desserts.size());
        assertEquals("Chocolate Cake", desserts.getFirst().getName());
    }

    @Test
    public void testFindAllDesserts() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        this.dessertService.create(dessertRequest);

        this.mockMvc.perform(
            get("/api/desserts")
                .header("Authorization-Test", "X-Authorization-test")
            )
            .andExpectAll(
                status().isOk(),
                jsonPath("$", hasSize(1)),
                jsonPath("$[0].name").value("Chocolate Cake")
            );
    }

    @Test
    public void testGetDessertById() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        this.mockMvc.perform(
            get("/api/desserts/{id}", dessertDto.getId())
                .header("Authorization-Test", "X-Authorization-test")
            )
            .andExpectAll(
                status().isOk(),
                jsonPath("$.name").value("Chocolate Cake")
            );
    }

    @Test
    public void testUpdateDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        DessertRequest updateRequest = new DessertRequest();
        updateRequest.setName("Vanilla Cake");
        String updatedRequestAsString = this.objectMapper.writeValueAsString(updateRequest);

        this.mockMvc.perform(
            put("/api/desserts/{id}", dessertDto.getId())
                .header("Authorization-Test", "X-Authorization-test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequestAsString)
            )
            .andExpectAll(
                status().isOk(),
                jsonPath("$.name").value("Vanilla Cake")
            );

        Dessert updatedDessert = this.dessertRepository.findById(dessertDto.getId()).orElse(null);
        assertEquals("Vanilla Cake", updatedDessert.getName());
    }

    @Test
    public void testDeleteDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        this.mockMvc.perform(
            delete("/api/desserts/{id}", dessertDto.getId())
                .header("Authorization-Test", "X-Authorization-test")
            )
            .andExpect(status().isNoContent());

        Optional<Dessert> byId = this.dessertRepository.findById(dessertDto.getId());
        assertFalse(byId.isPresent());
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DessertRepository dessertRepository;

    @Autowired
    private DessertService dessertService;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private ObjectMapper objectMapper;
}
