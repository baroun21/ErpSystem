-- ============================================================================
-- NEXORA ERP - Inventory Domain Tables
-- Inventory entities with multi-tenant and multi-entity support
-- ============================================================================

-- Products
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    sku VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    unit_price DECIMAL(15,2),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, sku),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Warehouses
CREATE TABLE IF NOT EXISTS warehouses (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    capacity DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Stock Items
CREATE TABLE IF NOT EXISTS stock_items (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    quantity_on_hand DECIMAL(15,2) NOT NULL,
    reorder_level DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, product_id, warehouse_id),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(product_id) REFERENCES products(id),
    FOREIGN KEY(warehouse_id) REFERENCES warehouses(id)
);

-- Stock Movements
CREATE TABLE IF NOT EXISTS stock_movements (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    movement_type VARCHAR(50),  -- IN, OUT, ADJUSTMENT, TRANSFER
    quantity DECIMAL(15,2) NOT NULL,
    reference VARCHAR(100),
    movement_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(product_id) REFERENCES products(id),
    FOREIGN KEY(warehouse_id) REFERENCES warehouses(id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_products_company_id ON products(company_id);
CREATE INDEX IF NOT EXISTS idx_warehouses_company_id ON warehouses(company_id);
CREATE INDEX IF NOT EXISTS idx_stock_items_company_id ON stock_items(company_id);
CREATE INDEX IF NOT EXISTS idx_stock_items_product_id ON stock_items(product_id);
CREATE INDEX IF NOT EXISTS idx_stock_movements_company_id ON stock_movements(company_id);
CREATE INDEX IF NOT EXISTS idx_stock_movements_product_id ON stock_movements(product_id);
