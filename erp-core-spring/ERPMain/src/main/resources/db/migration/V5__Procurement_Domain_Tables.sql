-- ============================================================================
-- NEXORA ERP - Procurement Domain Tables
-- Procurement entities with multi-tenant and multi-entity support
-- ============================================================================

-- Vendors (Procurement-specific)
CREATE TABLE IF NOT EXISTS procurement_vendors (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    address TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Purchase Orders
CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    vendor_id BIGINT NOT NULL,
    po_number VARCHAR(50) NOT NULL,
    order_date DATE,
    status VARCHAR(50),  -- Draft, Submitted, Approved, Ordered, Received
    total_amount DECIMAL(15,2),
    currency VARCHAR(3),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, po_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(vendor_id) REFERENCES procurement_vendors(id)
);

-- Purchase Order Line Items
CREATE TABLE IF NOT EXISTS purchase_order_items (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    purchase_order_id BIGINT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    quantity DECIMAL(15,2) NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    total_price DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(purchase_order_id) REFERENCES purchase_orders(id)
);

-- RFQs (Request for Quotation)
CREATE TABLE IF NOT EXISTS rfqs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    vendor_id BIGINT,
    rfq_number VARCHAR(50) NOT NULL,
    requested_date DATE,
    status VARCHAR(50),  -- Draft, Sent, Received, Closed
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, rfq_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(vendor_id) REFERENCES procurement_vendors(id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_procurement_vendors_company_id ON procurement_vendors(company_id);
CREATE INDEX IF NOT EXISTS idx_purchase_orders_company_id ON purchase_orders(company_id);
CREATE INDEX IF NOT EXISTS idx_purchase_orders_vendor_id ON purchase_orders(vendor_id);
CREATE INDEX IF NOT EXISTS idx_purchase_order_items_po_id ON purchase_order_items(purchase_order_id);
CREATE INDEX IF NOT EXISTS idx_rfqs_company_id ON rfqs(company_id);
CREATE INDEX IF NOT EXISTS idx_rfqs_vendor_id ON rfqs(vendor_id);
