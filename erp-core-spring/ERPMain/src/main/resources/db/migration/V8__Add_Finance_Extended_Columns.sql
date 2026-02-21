-- ============================================================================
-- NEXORA ERP - Finance Domain Extended Columns
-- Adding detailed financial tracking columns to invoices and bills
-- ============================================================================

-- Add extended columns to customers table
ALTER TABLE customers ADD COLUMN IF NOT EXISTS code VARCHAR(255);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS street VARCHAR(255);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS state VARCHAR(100);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS postal_code VARCHAR(20);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS zip_code VARCHAR(20);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS tax_id VARCHAR(50);

-- Rename active to is_active and ensure it's properly defined
ALTER TABLE customers RENAME COLUMN active TO is_active;

-- Add extended columns to suppliers table
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS code VARCHAR(255);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS street VARCHAR(255);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS state VARCHAR(100);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS postal_code VARCHAR(20);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS zip_code VARCHAR(20);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS tax_id VARCHAR(50);

-- Rename active to is_active for suppliers
ALTER TABLE suppliers RENAME COLUMN active TO is_active;

-- Add extended columns to invoices table
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS subtotal DECIMAL(15,2);
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS tax_amount DECIMAL(15,2);
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS total_amount DECIMAL(15,2);
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15,2) DEFAULT 0;
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS notes VARCHAR(2000);

-- Rename amount to total_amount if amount exists  
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS amount_old DECIMAL(15,2);
UPDATE invoices SET subtotal = COALESCE(amount, 0), amount_old = amount WHERE subtotal IS NULL;
UPDATE invoices SET total_amount = COALESCE(amount, 0) WHERE total_amount IS NULL;
UPDATE invoices SET paid_amount = 0 WHERE paid_amount IS NULL;

-- Add extended columns to bills table
ALTER TABLE bills ADD COLUMN IF NOT EXISTS subtotal DECIMAL(15,2);
ALTER TABLE bills ADD COLUMN IF NOT EXISTS tax_amount DECIMAL(15,2);
ALTER TABLE bills ADD COLUMN IF NOT EXISTS total_amount DECIMAL(15,2);
ALTER TABLE bills ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15,2) DEFAULT 0;
ALTER TABLE bills ADD COLUMN IF NOT EXISTS notes VARCHAR(2000);

-- Rename description to notes for bills if needed and set values
UPDATE bills SET notes = description WHERE notes IS NULL;

-- Add journal entry extended columns
ALTER TABLE journal_entries ADD COLUMN IF NOT EXISTS posted_by VARCHAR(255);
ALTER TABLE journal_entries ADD COLUMN IF NOT EXISTS is_balanced BOOLEAN DEFAULT FALSE;
ALTER TABLE journal_entries ADD COLUMN IF NOT EXISTS reversal_of_id BIGINT;

-- Add constraint for reversal_of_id
ALTER TABLE journal_entries ADD CONSTRAINT fk_journal_entries_reversal 
  FOREIGN KEY (reversal_of_id) REFERENCES journal_entries(id) ON DELETE SET NULL;

-- Add extended columns to journal_entry_lines table
ALTER TABLE journal_entry_lines ADD COLUMN IF NOT EXISTS debit_amount DECIMAL(15,2);
ALTER TABLE journal_entry_lines ADD COLUMN IF NOT EXISTS credit_amount DECIMAL(15,2);
ALTER TABLE journal_entry_lines ADD COLUMN IF NOT EXISTS cost_center_id BIGINT;

-- Add constraint for cost_center_id
ALTER TABLE journal_entry_lines ADD CONSTRAINT fk_journal_entry_lines_cost_center 
  FOREIGN KEY (cost_center_id) REFERENCES cost_centers(id) ON DELETE SET NULL;

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_customers_code ON customers(company_id, code);
CREATE INDEX IF NOT EXISTS idx_customers_is_active ON customers(company_id, is_active);
CREATE INDEX IF NOT EXISTS idx_suppliers_code ON suppliers(company_id, code);
CREATE INDEX IF NOT EXISTS idx_suppliers_is_active ON suppliers(company_id, is_active);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(company_id, status);
CREATE INDEX IF NOT EXISTS idx_bills_status ON bills(company_id, status);
CREATE INDEX IF NOT EXISTS idx_journal_entries_status ON journal_entries(company_id, status);
