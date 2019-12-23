-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2019-12-18 12:48:38.127

-- tables
-- Table: addresses
CREATE TABLE addresses (
    id int unsigned NOT NULL AUTO_INCREMENT,
    users_id int unsigned NULL,
    buyers_id int unsigned NULL,
    address varchar(255) NOT NULL COMMENT 'Address line 1',
    village varchar(30) NOT NULL,
    district varchar(30) NOT NULL,
    city varchar(30) NOT NULL,
    province varchar(30) NOT NULL,
    postal_code varchar(7) NOT NULL,
    CONSTRAINT addresses_pk PRIMARY KEY (id)
);

-- Table: api_tokens
CREATE TABLE api_tokens (
    id int unsigned NOT NULL AUTO_INCREMENT,
    user_id int unsigned NOT NULL,
    token int unsigned NOT NULL,
    expired_at timestamp NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT api_tokens_pk PRIMARY KEY (id)
);

-- Table: buyers
CREATE TABLE buyers (
    id int unsigned NOT NULL AUTO_INCREMENT,
    user_id int unsigned NOT NULL,
    name varchar(70) NOT NULL,
    phone varchar(20) NOT NULL,
    deleted_at timestamp NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT buyers_pk PRIMARY KEY (id)
);

-- Table: orders
CREATE TABLE orders (
    id int unsigned NOT NULL AUTO_INCREMENT,
    user_id int unsigned NOT NULL,
    buyer_id int unsigned NOT NULL,
    order_number varchar(50) NOT NULL,
    price decimal(12,2) NOT NULL,
    profit_price decimal(12,2) NOT NULL,
    qty int NOT NULL,
    total decimal(12,2) NOT NULL COMMENT '(price+profit_price)*qty',
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX orders_ak_1 (order_number),
    CONSTRAINT orders_pk PRIMARY KEY (id)
);

-- Table: product_images
CREATE TABLE product_images (
    id int unsigned NOT NULL,
    product_id int unsigned NOT NULL,
    name varchar(120) NOT NULL,
    path varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT product_images_pk PRIMARY KEY (id)
);

-- Table: products
CREATE TABLE products (
    id int unsigned NOT NULL AUTO_INCREMENT,
    name varchar(120) NOT NULL,
    slug varchar(120) NOT NULL,
    thumbnail varchar(255) NOT NULL,
    price decimal(12,2) NOT NULL,
    stock int NOT NULL,
    description text NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX products_ak_1 (slug),
    CONSTRAINT products_pk PRIMARY KEY (id)
);

-- Table: user_products
CREATE TABLE user_products (
    id int unsigned NOT NULL AUTO_INCREMENT,
    product_id int unsigned NOT NULL,
    user_id int unsigned NOT NULL,
    profit_price decimal(12,2) NOT NULL,
    qty int NOT NULL DEFAULT 0,
    status varchar(15) NOT NULL COMMENT '''''take'''', ''''sending'''', ''''active'''', ''''non-active'''', ''''returning''''',
    CONSTRAINT user_products_pk PRIMARY KEY (id)
);

-- Table: users
CREATE TABLE users (
    id int unsigned NOT NULL AUTO_INCREMENT,
    username varchar(16) NOT NULL,
    email varchar(120) NOT NULL,
    name varchar(70) NOT NULL,
    password varchar(120) NOT NULL,
    store_name varchar(60) NOT NULL,
    phone varchar(20) NOT NULL,
    avatar varchar(255) NULL,
    is_admin bool NOT NULL DEFAULT false,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX users_ak_1 (username,email,store_name,phone),
    CONSTRAINT users_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: addresses_buyers (table: addresses)
ALTER TABLE addresses ADD CONSTRAINT addresses_buyers FOREIGN KEY addresses_buyers (buyers_id)
    REFERENCES buyers (id);

-- Reference: addresses_users (table: addresses)
ALTER TABLE addresses ADD CONSTRAINT addresses_users FOREIGN KEY addresses_users (users_id)
    REFERENCES users (id);

-- Reference: api_tokens_users (table: api_tokens)
ALTER TABLE api_tokens ADD CONSTRAINT api_tokens_users FOREIGN KEY api_tokens_users (user_id)
    REFERENCES users (id);

-- Reference: buyers_users (table: buyers)
ALTER TABLE buyers ADD CONSTRAINT buyers_users FOREIGN KEY buyers_users (user_id)
    REFERENCES users (id);

-- Reference: orders_buyers (table: orders)
ALTER TABLE orders ADD CONSTRAINT orders_buyers FOREIGN KEY orders_buyers (buyer_id)
    REFERENCES buyers (id);

-- Reference: orders_users (table: orders)
ALTER TABLE orders ADD CONSTRAINT orders_users FOREIGN KEY orders_users (user_id)
    REFERENCES users (id);

-- Reference: product_images_products (table: product_images)
ALTER TABLE product_images ADD CONSTRAINT product_images_products FOREIGN KEY product_images_products (product_id)
    REFERENCES products (id);

-- Reference: user_products_products (table: user_products)
ALTER TABLE user_products ADD CONSTRAINT user_products_products FOREIGN KEY user_products_products (product_id)
    REFERENCES products (id);

-- Reference: user_products_users (table: user_products)
ALTER TABLE user_products ADD CONSTRAINT user_products_users FOREIGN KEY user_products_users (user_id)
    REFERENCES users (id);

-- End of file.

