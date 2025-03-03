package com.example.foscore;

import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Cuisine;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.entity.Payment;
import com.example.foscore.model.enums.PaymentStatus;
import com.example.foscore.model.request.CuisineRequest;
import com.example.foscore.model.request.DessertRequest;
import com.example.foscore.model.request.DrinkRequest;
import com.example.foscore.model.request.MealRequest;
import com.example.foscore.model.request.OrderRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MockData {

    public static Cuisine cuisine() {
        Cuisine cuisine = new Cuisine();
        cuisine.setId(1L);
        cuisine.setName("Mexican");
        cuisine.setCreatedAt(LocalDateTime.now());
        return cuisine;
    }

    public static Cuisine updatedCuisine() {
        Cuisine cuisine = cuisine();
        cuisine.setName("Polish");
        cuisine.setUpdatedAt(LocalDateTime.now());
        return cuisine;
    }

    public static List<Cuisine> cuisineList() {
        Cuisine cuisine1 = new Cuisine();
        cuisine1.setId(1L);
        cuisine1.setName("Mexican");
        cuisine1.setCreatedAt(LocalDateTime.now());

        Cuisine cuisine2 = new Cuisine();
        cuisine2.setId(2L);
        cuisine2.setName("Mexican");
        cuisine2.setCreatedAt(LocalDateTime.now());
        return List.of(cuisine1, cuisine2);
    }

    public static CuisineRequest cuisineRequest() {
        return new CuisineRequest("Mexican");
    }

    public static DessertRequest dessertRequest() {
        return new DessertRequest() {{
            setName("dessert_mock");
            setPrice(11f);
            setPortionWeight(123);
       }};
    }

    public static Dessert dessert() {
        return new Dessert() {{
            setId(1L);
            setName("dessert_mock");
            setPrice(11.0f);
            setPortionWeight(123);
            setCreatedAt(LocalDateTime.now());
        }};
    }

    public static Dessert updatedDessert() {
        return new Dessert() {{
            setId(1L);
            setName("updated_dessert_mock");
            setPrice(11.0f);
            setPortionWeight(123);
            setCreatedAt(LocalDateTime.now());
        }};
    }

    public static List<Dessert> dessertList() {
        Dessert dessert1 = new Dessert() {{
            setId(1L);
            setName("dessert_mock1");
            setPrice(11.0f);
            setPortionWeight(123);
            setCreatedAt(LocalDateTime.now());
        }};

        Dessert dessert2 = new Dessert() {{
            setId(2L);
            setName("dessert_mock2");
            setPrice(6.0f);
            setPortionWeight(100);
            setCreatedAt(LocalDateTime.now());
        }};

        return List.of(dessert1, dessert2);
    }

    public static DrinkRequest drinkRequest() {
        return new DrinkRequest() {{
            setName("lemonade");
            setPrice(2f);
        }};
    }

    public static Drink drink() {
        return new Drink() {{
            setId(1L);
            setName("lemonade");
            setPrice(2f);
            setCreatedAt(LocalDateTime.now());
        }};
    }

    public static Drink updatedDrink() {
        Drink drink = drink();
        drink.setName("lemonade2");
        drink.setPrice(1f);

        return drink;
    }

    public static List<Drink> drinkList() {
        Drink drink1 = new Drink() {{
            setId(1L);
            setName("lemonade");
            setPrice(2f);
            setCreatedAt(LocalDateTime.now());
        }};

        Drink drink2 = new Drink() {{
            setId(2L);
            setName("water");
            setPrice(0.7f);
            setCreatedAt(LocalDateTime.now());
        }};

        return List.of(drink1, drink2);
    }

    public static MealRequest mealRequest() {
        return new MealRequest() {{
            setName("meal_mock");
            setPrice(11.0f);
            setCuisineName(cuisine().getName());
            setPortionWeight(123);
        }};
    }

    public static Meal meal() {
        return new Meal() {{
            setId(1L);
            setName("meal_mock");
            setPrice(11.0f);
            setCuisine(cuisine());
            setPortionWeight(123);
            setCreatedAt(LocalDateTime.now());
        }};
    }

    public static Meal updatedMeal() {
        return new Meal() {{
            setId(1L);
            setName("meal_updated");
            setPrice(14.0f);
            setCuisine(cuisine());
            setPortionWeight(250);
            setCreatedAt(LocalDateTime.now());
            setUpdatedAt(LocalDateTime.now());
        }};
    }

    public static List<Meal> mealList() {
        Meal meal1 = new Meal() {{
            setId(1L);
            setName("meal_mock1");
            setPortionWeight(111);
            setCuisine(cuisineList().getFirst());
            setPrice(7.0f);
            setCreatedAt(LocalDateTime.now());
        }};

        Meal meal2 = new Meal() {{
            setId(2L);
            setName("meal_mock2");
            setPortionWeight(245);
            setCuisine(cuisineList().getLast());
            setPrice(5.4f);
            setCreatedAt(LocalDateTime.now());
        }};

        return List.of(meal1, meal2);
    }

    public static OrderRequest orderRequest() {
        return new OrderRequest() {{
            setMeals(Set.of( MealDto.toDto(meal()) ));
            setDesserts(Set.of(DessertDto.toDto(dessert()) ));
            setDrinks(Set.of( DrinkDto.toDto(drink()) ));
            setIceCubes(true);
        }};
    }

    public static Order order() {
        return new Order() {{
            setId(1L);
            setMeals(List.of(meal()));
            setDesserts(List.of(dessert()));
            setDrinks(List.of(drink()));
            setIceCubes(true);
            setCreatedAt(LocalDateTime.now());
            setPayment(payment());
        }};
    }

    public static List<Order> orderList() {
        Order order1 = new Order() {{
            setId(1L);
            setMeals(mealList());
            setDesserts(dessertList());
            setDrinks(drinkList());
            setIceCubes(true);
            setCreatedAt(LocalDateTime.now());
        }};

        Order order2 = new Order() {{
            setId(2L);
            setMeals(mealList());
            setDesserts(dessertList());
            setDrinks(drinkList());
            setIceCubes(false);
            setCreatedAt(LocalDateTime.now());
        }};

        return List.of(order1, order2);
    }

    public static Payment payment() {
        return Payment.builder()
            .id("1")
            .totalPrice(10f)
            .status(PaymentStatus.PAID)
            .build();
    }
}
