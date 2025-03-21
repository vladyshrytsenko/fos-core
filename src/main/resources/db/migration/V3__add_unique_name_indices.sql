alter table drinks
    add constraint unique_drink_name unique (name);

alter table desserts
    add constraint unique_dessert_name unique (name);

alter table meals
    add constraint unique_meal_name unique (name);

alter table cuisines
    add constraint unique_cuisine_name unique (name);