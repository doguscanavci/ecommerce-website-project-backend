-- ============================================================
-- REFERENCE SCHEMA — for your understanding only.
-- You do NOT need to run this manually: application.yml has
-- spring.jpa.hibernate.ddl-auto=update, so Hibernate creates/
-- updates these tables automatically from the @Entity classes
-- the first time you run the app (as long as the DATABASE
-- "bandage_ecommerce" itself already exists in PostgreSQL).
-- ============================================================

CREATE DATABASE bandage_ecommerce;

-- \c bandage_ecommerce

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES roles(id),
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    activation_code VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE stores (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    tax_no VARCHAR(255) NOT NULL,
    bank_account VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    img VARCHAR(500) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    code VARCHAR(255)
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    sell_count INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    store_id BIGINT REFERENCES stores(id)
);

CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    image_index INTEGER,
    product_id BIGINT NOT NULL REFERENCES products(id)
);

CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    district VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    address TEXT,
    user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE credit_cards (
    id BIGSERIAL PRIMARY KEY,
    card_no VARCHAR(16) NOT NULL,
    expire_month INTEGER NOT NULL,
    expire_year INTEGER NOT NULL,
    name_on_card VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    address_id BIGINT NOT NULL REFERENCES addresses(id),
    order_date TIMESTAMP NOT NULL,
    card_no VARCHAR(255) NOT NULL,
    card_name VARCHAR(255) NOT NULL,
    card_expire_month INTEGER NOT NULL,
    card_expire_year INTEGER NOT NULL,
    price NUMERIC(10,2) NOT NULL
    -- Note: card_ccv is intentionally NEVER stored (PCI-DSS best practice)
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    count INTEGER NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    detail VARCHAR(500)
);
