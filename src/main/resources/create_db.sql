-- AI generated from ER-diagram image
-- Таблица products
CREATE TABLE products (
    id BIGINT NOT NULL PRIMARY KEY,
    title character varying(255),
    description character varying(255),
    time_insert TIMESTAMP(6) without time zone,
    time_update TIMESTAMP(6) without time zone,
    article BIGINT,
    sale INTEGER,
    price DOUBLE PRECISION,
    inventory INTEGER NOT NULL,
    in_stock BOOLEAN
);

-- Таблица carts
CREATE TABLE carts (
    id BIGINT NOT NULL PRIMARY KEY,
    total_amount DOUBLE PRECISION
);

-- Таблица cart_items
CREATE TABLE cart_items (
    id BIGINT NOT NULL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    quantity INTEGER NOT NULL,

    -- Внешние ключи
    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id) REFERENCES carts(id),
    CONSTRAINT fk_cart_items_product
        FOREIGN KEY (product_id) REFERENCES products(id)
);