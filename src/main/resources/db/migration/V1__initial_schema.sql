
create table orders(
    id serial primary key,
    total_price float not null,
    created_at timestamp default current_timestamp,
    created_by int not null,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table drinks(
    id serial primary key,
    name varchar(64) not null,
    price float not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table cuisines(
    id serial primary key,
    name varchar(64) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table desserts(
    id serial primary key,
    name varchar(64) not null,
    portion_weight float not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table meals(
    id serial primary key,
    name varchar(64) not null,
    portion_weight float not null,
    cuisine_id int not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false,
    foreign key (cuisine_id) references cuisines(id)
);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    order_id BIGINT,
    status VARCHAR(20) NOT NULL
);

create table subscriptions (
    id serial primary key,
    customer_id varchar(64),
    type varchar(20) not null
);
