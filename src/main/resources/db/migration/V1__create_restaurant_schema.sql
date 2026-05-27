CREATE TABLE tables (
    id BIGSERIAL PRIMARY KEY,
    number INTEGER NOT NULL UNIQUE,
    description VARCHAR(100),
    capacity INTEGER NOT NULL DEFAULT 4,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED', 'INACTIVE'))
);

CREATE TABLE product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    category_id BIGINT NOT NULL REFERENCES product_categories(id),
    description TEXT,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    prep_time_minutes INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category ON products(category_id);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    table_id BIGINT NOT NULL REFERENCES tables(id),
    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    notes TEXT,
    CHECK (status IN ('OPEN', 'IN_PREPARATION', 'READY', 'DELIVERED', 'CLOSED', 'CANCELED'))
);

CREATE INDEX idx_orders_table ON orders(table_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_opened_at ON orders(opened_at);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    notes TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    CHECK (status IN ('PENDING', 'IN_PREPARATION', 'READY', 'DELIVERED', 'CANCELED'))
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_order_items_status ON order_items(status);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    payment_method VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    external_transaction_code VARCHAR(100),
    payment_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (payment_method IN ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'PIX')),
    CHECK (status IN ('PENDING', 'APPROVED', 'DECLINED', 'CANCELED'))
);

CREATE INDEX idx_payments_order ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);

CREATE TABLE bills (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0),
    service_fee NUMERIC(10, 2) NOT NULL DEFAULT 0 CHECK (service_fee >= 0),
    discount NUMERIC(10, 2) DEFAULT 0 CHECK (discount >= 0),
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    closed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);