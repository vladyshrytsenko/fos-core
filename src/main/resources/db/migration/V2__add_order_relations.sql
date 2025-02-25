create table order_desserts(
    order_id serial not null,
    dessert_id serial not null,

    primary key (order_id, dessert_id),
    foreign key (order_id) references orders(id) on delete cascade,
    foreign key (dessert_id) references desserts(id) on delete cascade
);

create table order_drinks(
    order_id serial not null,
    drink_id serial not null,

    primary key (order_id, drink_id),
    foreign key (order_id) references orders(id) on delete cascade,
    foreign key (drink_id) references drinks(id) on delete cascade
);

create table order_meals(
    order_id serial not null,
    meal_id serial not null,

    primary key (order_id, meal_id),
    foreign key (order_id) references orders(id) on delete cascade,
    foreign key (meal_id) references meals(id) on delete cascade
);