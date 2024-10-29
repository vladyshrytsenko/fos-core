
create table orders(
    id int auto_increment primary key,
    total_price float not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table drinks(
    id int auto_increment primary key,
    name varchar(64) not null,
    price float not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table cuisines(
    id int auto_increment primary key,
    name varchar(64) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false
);

create table desserts(
    id int auto_increment primary key,
    name varchar(64) not null,
    portion_weight float not null,
    cuisine_id int not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    deleted_at timestamp default current_timestamp,
    is_deleted boolean default false,
    foreign key (cuisine_id) references cuisines(id)
);

create table meals(
    id int auto_increment primary key,
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
