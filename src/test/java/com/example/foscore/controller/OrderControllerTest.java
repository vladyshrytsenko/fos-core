package com.example.foscore.controller;

import com.example.foscore.MockData;
import com.example.foscore.StartupApplicationCore;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.enums.DishType;
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
import com.example.foscore.security.SecurityTestConfig;
import com.example.foscore.service.CuisineService;
import com.example.foscore.service.DessertService;
import com.example.foscore.service.DrinkService;
import com.example.foscore.service.MealService;
import com.example.foscore.service.OrderService;
import com.example.foscore.service.rabbitmq.PopularDishesProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { StartupApplicationCore.class, SecurityTestConfig.class })
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerTest {

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
            setName("lemonade");
            setPrice(2f);
        }};
        this.drinkService.create(drinkRequest);

        DessertRequest dessertRequest = new DessertRequest() {{
            setName("dessert_mock");
            setPortionWeight(123);
            setPrice(11f);
        }};
        this.dessertService.create(dessertRequest);

        MealRequest mealRequest = new MealRequest() {{
            setName("meal_mock");
            setCuisineName("Mexican");
            setPortionWeight(123);
            setPrice(11f);
        }};
        this.mealService.create(mealRequest);

        Jwt mockJwt = Mockito.mock(Jwt.class);

        Mockito.when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
    }

    @Test
    public void testCreateOrder() throws Exception {
        DrinkDto drinkDto = DrinkDto.toDto(MockData.drink());
        DessertDto dessertDto = DessertDto.toDto(MockData.dessert());
        MealDto mealDto = MealDto.toDto(MockData.meal());

        OrderRequest request = new OrderRequest() {{
            setDrinks(Set.of(drinkDto));
            setDesserts(Set.of(dessertDto));
            setMeals(Set.of(mealDto));
        }};

        String orderRequestAsString = this.objectMapper.writeValueAsString(request);

        doNothing().when(this.popularDishesProducer).sendOrderEvent(any(DishType.class), anyString());

        this.mockMvc.perform(
            post("/api/orders")
                .header("Authorization-Test", "X-Authorization-test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestAsString)
            )
            .andExpect(status().isCreated());

        List<Order> orders = this.orderRepository.findAll();
        assertEquals(1, orders.size());
        assertEquals(24f, orders.getFirst().getTotalPrice());
    }

//    @Test
//    public void testFindAllOrders() throws Exception {
//        DrinkDto drinkDto = DrinkDto.toDto(MockData.drink());
//        DessertDto dessertDto = DessertDto.toDto(MockData.dessert());
//        MealDto mealDto = MealDto.toDto(MockData.meal());
//
//        OrderRequest request = new OrderRequest() {{
//            setDrinks(Set.of(drinkDto));
//            setDesserts(Set.of(dessertDto));
//            setMeals(Set.of(mealDto));
//        }};
//
//        this.orderService.create(1L, request);
//
//        this.mockMvc.perform(
//            get("/api/orders")
//                .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpectAll(
//                status().isOk(),
//                jsonPath("$.content[0].totalPrice").value(24f)
//            );
//    }

//    @Test
//    public void testGetOrderById() throws Exception {
//        DrinkDto drinkDto = DrinkDto.toDto(MockData.drink());
//        DessertDto dessertDto = DessertDto.toDto(MockData.dessert());
//        MealDto mealDto = MealDto.toDto(MockData.meal());
//
//        OrderRequest request = new OrderRequest() {{
//            setDrinks(Set.of(drinkDto));
//            setDesserts(Set.of(dessertDto));
//            setMeals(Set.of(mealDto));
//        }};
//        OrderDto orderDto = this.orderService.create(1L, request);
//
//        this.mockMvc.perform(
//            get("/api/orders/{id}", orderDto.getId())
//                .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpectAll(
//                status().isOk(),
//                jsonPath("$.totalPrice").value(24f)
//            );
//    }

//    @Test
//    public void testDeleteOrder() throws Exception {
//        DrinkDto drinkDto = DrinkDto.toDto(MockData.drink());
//        DessertDto dessertDto = DessertDto.toDto(MockData.dessert());
//        MealDto mealDto = MealDto.toDto(MockData.meal());
//
//        OrderRequest request = new OrderRequest() {{
//            setDrinks(Set.of(drinkDto));
//            setDesserts(Set.of(dessertDto));
//            setMeals(Set.of(mealDto));
//        }};
//
//        OrderDto orderDto = this.orderService.create(1L, request);
//
//        this.mockMvc.perform(
//                delete("/api/orders/{id}", orderDto.getId())
//                    .header("Authorization-Test", "X-Authorization-test")
//            )
//            .andExpect(status().isNoContent());
//
//        Optional<Order> byId = this.orderRepository.findById(orderDto.getId());
//        assertFalse(byId.isPresent());
//    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private PopularDishesProducer popularDishesProducer;

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
}
