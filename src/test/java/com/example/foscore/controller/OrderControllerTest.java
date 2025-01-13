package com.example.foscore.controller;

import com.example.foscore.StartupApplication;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.model.request.DessertRequest;
import com.example.foscore.model.request.DrinkRequest;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.model.request.OrderRequest;
import com.example.foscore.repository.CuisineRepository;
import com.example.foscore.repository.DessertRepository;
import com.example.foscore.repository.DrinkRepository;
import com.example.foscore.repository.MealRepository;
import com.example.foscore.repository.OrderRepository;
import com.example.foscore.service.CuisineService;
import com.example.foscore.service.DessertService;
import com.example.foscore.service.DrinkService;
import com.example.foscore.service.MealService;
import com.example.foscore.service.OrderService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StartupApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DessertRepository dessertRepository;

    @Autowired
    private DessertService dessertService;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.cuisineRepository.deleteAll();
        this.drinkRepository.deleteAll();
        this.dessertRepository.deleteAll();
        this.mealRepository.deleteAll();
        this.orderRepository.deleteAll();

        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        this.cuisineService.create(cuisineRequest);

        DrinkRequest drinkRequest = new DrinkRequest() {{
            setName("Lemonade");
            setPrice(1f);
        }};
        this.drinkService.create(drinkRequest);

        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setPortionWeight(200);
            setPrice(4f);
        }};
        this.dessertService.create(dessertRequest);

        MealRequest mealRequest = new MealRequest() {{
            setName("Tacos");
            setCuisineName("Mexican");
            setPortionWeight(100);
            setPrice(3f);
        }};
        this.mealService.create(mealRequest);
    }

    @Test
    public void testCreateOrder() throws Exception {
        OrderRequest request = new OrderRequest() {{
            setDrinkName("Lemonade");
            setDessertName("Chocolate Cake");
            setMealName("Tacos");
        }};

        this.mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        List<Order> orders = this.orderRepository.findAll();
        assertEquals(1, orders.size());
        assertEquals(8f, orders.getFirst().getTotalPrice());
    }

    @Test
    public void testFindAllOrders() throws Exception {
        OrderRequest request = new OrderRequest() {{
            setDrinkName("Lemonade");
            setDessertName("Chocolate Cake");
            setMealName("Tacos");
        }};
        this.orderService.create(request);

        this.mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].totalPrice").value(8f));
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderRequest request = new OrderRequest() {{
            setDrinkName("Lemonade");
            setDessertName("Chocolate Cake");
            setMealName("Tacos");
        }};
        OrderDto orderDto = this.orderService.create(request);

        this.mockMvc.perform(get("/api/orders/{id}", orderDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice").value(8f));
    }

//    @Test
//    public void testDeleteOrder() throws Exception {
//        OrderRequest request = new OrderRequest() {{
//            setDrinkName("Lemonade");
//            setDessertName("Chocolate Cake");
//            setMealName("Tacos");
//        }};
//        OrderDto orderDto = this.orderService.create(request);
//
//        this.mockMvc.perform(delete("/api/orders/{id}", orderDto.getId()))
//            .andExpect(status().isNoContent());
//
//        Optional<Order> byId = this.orderRepository.findById(orderDto.getId());
//        assertFalse(byId.isPresent());
//    }
}
