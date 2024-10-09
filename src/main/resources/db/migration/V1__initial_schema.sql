
create table orders(
    id int auto_increment primary key,
    total_price float not null
);

create table drinks(
    id int auto_increment primary key,
    name varchar(64) not null,
    price float not null
);

create table cuisines(
    id int auto_increment primary key,
    name varchar(64) not null
);

create table lunches(
    id int auto_increment primary key,
    name varchar(64) not null,
    price float not null,
    cuisine_id bigserial,
    foreign key (cuisine_id) references cuisines(id)
);

create table desserts(
    id int auto_increment primary key,
    name varchar(64) not null,
    portion_weight float not null,
    lunch_id int,
    foreign key (lunch_id) references lunches(id)
);

create table meals(
    id int auto_increment primary key,
    name varchar(64) not null,
    portion_weight float not null,
    lunch_id int,
    foreign key (lunch_id) references lunches(id)
);
