ALTER TABLE IF EXISTS order_product
DROP CONSTRAINT IF EXISTS FKo6helt0ucmegaeachjpx40xhe;

ALTER TABLE IF EXISTS order_product
DROP CONSTRAINT IF EXISTS FKl5mnj9n0di7k1v90yxnthkc73;

ALTER TABLE IF EXISTS orders
DROP CONSTRAINT IF EXISTS FK32ql8ubntj5uh44ph9659tiih;

ALTER TABLE IF EXISTS products
DROP CONSTRAINT IF EXISTS FK1cf90etcu98x1e6n9aks3tel3;

ALTER TABLE IF EXISTS user_role
DROP CONSTRAINT IF EXISTS FKj345gk1bovqvfame88rcx7yyx;

DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS order_product CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP SEQUENCE IF EXISTS category_id_seq;
DROP SEQUENCE IF EXISTS orders_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS users_id_seq;

CREATE SEQUENCE category_id_seq
START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE orders_seq
START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE product_seq
START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE users_id_seq
START WITH 1 INCREMENT BY 1;

CREATE TABLE category
(
    id bigint NOT NULL,
    description varchar(255),
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE order_product
(
    order_id bigint NOT NULL,
    product_id bigint NOT NULL
);

CREATE TABLE orders
(
    id bigint NOT NULL,
    total_price float(53) NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    user_id bigint, status varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE products
(
    id bigint NOT NULL,
    name varchar(255),
    description varchar(255),
    price float(53),
    date timestamp default current_timestamp,
    city varchar(255),
    participant_count integer,
    available boolean,
    category_id bigint,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id bigint NOT NULL,
    ROLES varchar(255) CHECK (ROLES IN ('USER','ADMIN'))
);

CREATE TABLE users
(
    id bigint NOT NULL,
    email varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    active boolean,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS order_product ADD CONSTRAINT FKo6helt0ucmegaeachjpx40xhe
FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE IF EXISTS order_product ADD CONSTRAINT FKl5mnj9n0di7k1v90yxnthkc73
FOREIGN KEY (order_id) REFERENCES orders;

ALTER TABLE IF EXISTS orders ADD CONSTRAINT FK32ql8ubntj5uh44ph9659tiih
FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS products ADD CONSTRAINT FK1cf90etcu98x1e6n9aks3tel3
FOREIGN KEY (category_id) REFERENCES category;

ALTER TABLE IF EXISTS user_role ADD CONSTRAINT FKj345gk1bovqvfame88rcx7yyx
FOREIGN KEY (user_id) REFERENCES users;