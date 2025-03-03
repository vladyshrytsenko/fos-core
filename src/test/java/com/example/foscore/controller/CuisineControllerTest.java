//package com.example.foscore.controller;
//
//import com.example.foscore.StartupApplicationCore;
//import com.example.foscore.model.dto.CuisineDto;
//import com.example.foscore.model.entity.Cuisine;
//import com.example.foscore.model.request.CuisineRequest;
//import com.example.foscore.repository.CuisineRepository;
//import com.example.foscore.security.SecurityTestConfig;
//import com.example.foscore.service.CuisineService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(classes = { StartupApplicationCore.class, SecurityTestConfig.class })
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class CuisineControllerTest {
//
//    @BeforeEach
//    public void setUp() {
//        this.cuisineRepository.deleteAll();
//    }
//
//    @Test
//    public void testCreateCuisine() throws Exception {
//        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
//        String cuisineRequestAsString = this.objectMapper.writeValueAsString(cuisineRequest);
//
//        this.mockMvc.perform(
//            post("/api/cuisines")
//                .header("Authorization-Test", "X-Authorization-test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(cuisineRequestAsString)
//            )
//            .andExpectAll(
//                status().isCreated(),
//                jsonPath("$.name").value("Mexican")
//            );
//
//        List<Cuisine> cuisines = this.cuisineRepository.findAll();
//        assertEquals(1, cuisines.size());
//        assertEquals("Mexican", cuisines.getFirst().getName());
//    }
//
//    @Test
//    public void testFindAllCuisines() throws Exception {
//        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
//        this.cuisineService.create(cuisineRequest);
//
//        this.mockMvc.perform(
//            get("/api/cuisines")
//                .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpectAll(
//                status().isOk(),
//                jsonPath("$[0].name").value("Mexican")
//            );
//    }
//
//    @Test
//    public void testGetCuisineById() throws Exception {
//        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
//        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);
//
//        this.mockMvc.perform(
//            get("/api/cuisines/{id}", cuisineDto.getId())
//                .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpectAll(
//                status().isOk(),
//                jsonPath("$.name").value("Mexican")
//            );
//    }
//
//    @Test
//    public void testUpdateCuisine() throws Exception {
//        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
//        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);
//
//        CuisineRequest updateRequest = new CuisineRequest("Polish");
//        String updatedRequestAsString = this.objectMapper.writeValueAsString(updateRequest);
//
//        this.mockMvc.perform(
//            put("/api/cuisines/{id}", cuisineDto.getId())
//                .header("Authorization-Test", "X-Authorization-test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(updatedRequestAsString)
//            )
//            .andExpectAll(
//                status().isOk(),
//                jsonPath("$.name").value("Polish")
//            );
//
//        Cuisine updatedCuisine = this.cuisineRepository.findById(cuisineDto.getId()).orElse(null);
//        assertEquals("Polish", updatedCuisine.getName());
//    }
//
//    @Test
//    public void testDeleteCuisine() throws Exception {
//        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
//        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);
//
//        this.mockMvc.perform(
//            delete("/api/cuisines/{id}", cuisineDto.getId())
//                .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpect(status().isNoContent());
//
//        Optional<Cuisine> byId = this.cuisineRepository.findById(cuisineDto.getId());
//        assertFalse(byId.isPresent());
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CuisineRepository cuisineRepository;
//
//    @Autowired
//    private CuisineService cuisineService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//}
