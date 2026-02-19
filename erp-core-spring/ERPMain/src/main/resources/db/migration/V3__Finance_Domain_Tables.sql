-- ============================================================================
-- NEXORA ERP - Finance Domain Tables
-- All accounting and financial entities with multi-tenant support
-- ============================================================================

-- Chart of Accounts
CREATE TABLE IF NOT EXISTS chart_of_accounts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    account_code VARCHAR(20) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    account_type VARCHAR(50),  -- Asset, Liability, Equity, Revenue, Expense
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, account_code),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Cost Centers
CREATE TABLE IF NOT EXISTS cost_centers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    manager VARCHAR(255),
    budget DECIMAL(15,2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, code),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Customers
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    credit_limit DECIMAL(15,2),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Suppliers
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    payment_terms VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Invoices
CREATE TABLE IF NOT EXISTS invoices (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    customer_id BIGINT,
    invoice_number VARCHAR(50) NOT NULL,
    invoice_date DATE,
    due_date DATE,
    amount DECIMAL(15,2),
    status VARCHAR(50),  -- Draft, Sent, Paid, Overdue
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, invoice_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

-- Bills
CREATE TABLE IF NOT EXISTS bills (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    supplier_id BIGINT,
    bill_number VARCHAR(50) NOT NULL,
    bill_date DATE,
    due_date DATE,
    amount DECIMAL(15,2),
    status VARCHAR(50),  -- Draft, Submitted, Approved, Paid
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, bill_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(supplier_id) REFERENCES suppliers(id)
);

-- Bank Accounts
CREATE TABLE IF NOT EXISTS bank_accounts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    account_number VARCHAR(50) NOT NULL,
    bank_name VARCHAR(255),
    account_holder VARCHAR(255),
    balance DECIMAL(15,2),
    currency VARCHAR(3),
    account_type VARCHAR(50),  -- Checking, Savings, etc.
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, account_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Bank Transactions
CREATE TABLE IF NOT EXISTS bank_transactions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    bank_account_id BIGINT,
    transaction_date DATE,
    type VARCHAR(50),  -- Debit, Credit, Cheque, Transfer
    amount DECIMAL(15,2),
    description TEXT,
    reference VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(bank_account_id) REFERENCES bank_accounts(id)
);

-- Journal Entries
CREATE TABLE IF NOT EXISTS journal_entries (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    entry_date DATE NOT NULL,
    description TEXT,
    reference VARCHAR(100),
    status VARCHAR(50),  -- Draft, Posted, Reversed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Journal Entry Line Items
CREATE TABLE IF NOT EXISTS journal_entry_lines (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    journal_entry_id BIGINT,
    account_id BIGINT,
    debit DECIMAL(15,2),
    credit DECIMAL(15,2),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(journal_entry_id) REFERENCES journal_entries(id),
    FOREIGN KEY(account_id) REFERENCES chart_of_accounts(id)
);

-- Customer Payments
CREATE TABLE IF NOT EXISTS customer_payments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    customer_id BIGINT NOT NULL,
    invoice_id BIGINT,
    payment_date DATE,
    amount DECIMAL(15,2),
    payment_method VARCHAR(50),  -- Check, Wire, CC, ACH, etc.
    reference VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id),
    FOREIGN KEY(invoice_id) REFERENCES invoices(id)
);

-- Supplier Payments
CREATE TABLE IF NOT EXISTS supplier_payments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    supplier_id BIGINT NOT NULL,
    bill_id BIGINT,
    payment_date DATE,
    amount DECIMAL(15,2),
    payment_method VARCHAR(50),  -- Check, Wire, CC, ACH, etc.
    reference VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY(bill_id) REFERENCES bills(id)
);

-- AR Aging (Accounts Receivable aging analysis)
CREATE TABLE IF NOT EXISTS ar_aging (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    customer_id BIGINT,
    report_date DATE,
    current_0_30 DECIMAL(15,2),
    days_31_60 DECIMAL(15,2),
    days_61_90 DECIMAL(15,2),
    days_90_plus DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

-- Trial Balance
CREATE TABLE IF NOT EXISTS trial_balance (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    account_id BIGINT,
    report_date DATE,
    debit_balance DECIMAL(15,2),
    credit_balance DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(account_id) REFERENCES chart_of_accounts(id)
);

-- Unified Payments (replaces customer_payments + supplier_payments)
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    payment_number VARCHAR(50) NOT NULL,
    payment_type VARCHAR(50),  -- CUSTOMER_PAYMENT, SUPPLIER_PAYMENT, INTERNAL
    customer_id BIGINT,
    supplier_id BIGINT,
    invoice_id BIGINT,
    bill_id BIGINT,
    bank_account_id BIGINT,
    amount DECIMAL(15,2),
    currency VARCHAR(3),
    status VARCHAR(50),  -- DRAFT, RECEIVED, CLEARED, CANCELLED
    payment_date DATE,
    cleared_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, payment_number),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id),
    FOREIGN KEY(supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY(invoice_id) REFERENCES invoices(id),
    FOREIGN KEY(bill_id) REFERENCES bills(id),
    FOREIGN KEY(bank_account_id) REFERENCES bank_accounts(id)
);

-- Invoice Line Items
CREATE TABLE IF NOT EXISTS invoice_lines (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    invoice_id BIGINT NOT NULL,
    line_number INT,
    account_code VARCHAR(20),
    description VARCHAR(500),
    quantity DECIMAL(12,4),
    unit_price DECIMAL(15,2),
    tax_amount DECIMAL(15,2),
    line_total DECIMAL(15,2),
    cost_center_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(invoice_id) REFERENCES invoices(id),
    FOREIGN KEY(cost_center_id) REFERENCES cost_centers(id)
);

-- Bill Line Items
CREATE TABLE IF NOT EXISTS bill_lines (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    bill_id BIGINT NOT NULL,
    line_number INT,
    account_code VARCHAR(20),
    description VARCHAR(500),
    quantity DECIMAL(12,4),
    unit_price DECIMAL(15,2),
    tax_amount DECIMAL(15,2),
    line_total DECIMAL(15,2),
    cost_center_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(bill_id) REFERENCES bills(id),
    FOREIGN KEY(cost_center_id) REFERENCES cost_centers(id)
);

-- Currencies (multi-currency support)
CREATE TABLE IF NOT EXISTS currencies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    currency_code VARCHAR(3) NOT NULL,
    currency_name VARCHAR(100),
    symbol VARCHAR(10),
    is_base_currency BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    UNIQUE(company_id, currency_code),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Exchange Rates
CREATE TABLE IF NOT EXISTS exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    from_currency_id BIGINT NOT NULL,
    to_currency_id BIGINT NOT NULL,
    rate DECIMAL(18,8),
    inverse_rate DECIMAL(18,8),
    rate_date DATE,
    is_manual BOOLEAN DEFAULT FALSE,
    source VARCHAR(100),  -- ECB, Fed, Manual, etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(from_currency_id) REFERENCES currencies(id),
    FOREIGN KEY(to_currency_id) REFERENCES currencies(id)
);

-- Trial Balance Reports
CREATE TABLE IF NOT EXISTS trial_balance_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    report_date DATE NOT NULL,
    total_debits DECIMAL(15,2),
    total_credits DECIMAL(15,2),
    status VARCHAR(50),  -- BALANCED, OUT_OF_BALANCE
    difference VARCHAR(500),
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Income Statement Reports (P&L)
CREATE TABLE IF NOT EXISTS income_statement_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    total_revenue DECIMAL(15,2),
    cost_of_goods_sold DECIMAL(15,2),
    gross_profit DECIMAL(15,2),
    operating_expenses DECIMAL(15,2),
    operating_income DECIMAL(15,2),
    interest_expense DECIMAL(15,2),
    other_income DECIMAL(15,2),
    income_before_tax DECIMAL(15,2),
    income_tax_expense DECIMAL(15,2),
    net_income DECIMAL(15,2),
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Balance Sheet Reports
CREATE TABLE IF NOT EXISTS balance_sheet_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    report_date DATE NOT NULL,
    current_assets DECIMAL(15,2),
    fixed_assets DECIMAL(15,2),
    other_assets DECIMAL(15,2),
    total_assets DECIMAL(15,2),
    current_liabilities DECIMAL(15,2),
    longterm_liabilities DECIMAL(15,2),
    total_liabilities DECIMAL(15,2),
    common_stock DECIMAL(15,2),
    retained_earnings DECIMAL(15,2),
    total_equity DECIMAL(15,2),
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Cash Flow Reports
CREATE TABLE IF NOT EXISTS cash_flow_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    net_income DECIMAL(15,2),
    depreciation DECIMAL(15,2),
    amortization DECIMAL(15,2),
    change_in_working_capital DECIMAL(15,2),
    operating_cash_flow DECIMAL(15,2),
    capital_expenditures DECIMAL(15,2),
    investment_purchases DECIMAL(15,2),
    investing_cash_flow DECIMAL(15,2),
    debt_proceeds DECIMAL(15,2),
    debt_repayment DECIMAL(15,2),
    dividends_paid DECIMAL(15,2),
    financing_cash_flow DECIMAL(15,2),
    net_cash_flow DECIMAL(15,2),
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- AR Ageing Reports (Accounts Receivable aging analysis)
CREATE TABLE IF NOT EXISTS ar_ageing_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    report_date DATE NOT NULL,
    customer_id BIGINT,
    customer_name VARCHAR(255),
    current_030 DECIMAL(15,2),
    days_3160 DECIMAL(15,2),
    days_6190 DECIMAL(15,2),
    days_90_plus DECIMAL(15,2),
    total_outstanding DECIMAL(15,2),
    credit_status VARCHAR(50),  -- CURRENT, AT_RISK, PAST_DUE, COLLECTION
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

-- AP Ageing Reports (Accounts Payable aging analysis)
CREATE TABLE IF NOT EXISTS ap_ageing_reports (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    report_date DATE NOT NULL,
    supplier_id BIGINT,
    supplier_name VARCHAR(255),
    current_030 DECIMAL(15,2),
    days_3160 DECIMAL(15,2),
    days_6190 DECIMAL(15,2),
    days_90_plus DECIMAL(15,2),
    total_outstanding DECIMAL(15,2),
    payment_status VARCHAR(50),  -- ON_TIME, UPCOMING, OVERDUE, SEVERELY_OVERDUE
    notes VARCHAR(2000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(supplier_id) REFERENCES suppliers(id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_chart_of_accounts_company_id ON chart_of_accounts(company_id);
CREATE INDEX IF NOT EXISTS idx_cost_centers_company_id ON cost_centers(company_id);
CREATE INDEX IF NOT EXISTS idx_customers_company_id ON customers(company_id);
CREATE INDEX IF NOT EXISTS idx_suppliers_company_id ON suppliers(company_id);
CREATE INDEX IF NOT EXISTS idx_invoices_company_id ON invoices(company_id);
CREATE INDEX IF NOT EXISTS idx_invoices_customer_id ON invoices(customer_id);
CREATE INDEX IF NOT EXISTS idx_invoices_date ON invoices(invoice_date);
CREATE INDEX IF NOT EXISTS idx_bills_company_id ON bills(company_id);
CREATE INDEX IF NOT EXISTS idx_bills_supplier_id ON bills(supplier_id);
CREATE INDEX IF NOT EXISTS idx_bills_date ON bills(bill_date);
CREATE INDEX IF NOT EXISTS idx_bank_accounts_company_id ON bank_accounts(company_id);
CREATE INDEX IF NOT EXISTS idx_bank_transactions_company_id ON bank_transactions(company_id);
CREATE INDEX IF NOT EXISTS idx_bank_transactions_account_id ON bank_transactions(bank_account_id);
CREATE INDEX IF NOT EXISTS idx_bank_transactions_date ON bank_transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_journal_entries_company_id ON journal_entries(company_id);
CREATE INDEX IF NOT EXISTS idx_journal_entries_date ON journal_entries(entry_date);
CREATE INDEX IF NOT EXISTS idx_journal_entry_lines_company_id ON journal_entry_lines(company_id);
CREATE INDEX IF NOT EXISTS idx_journal_entry_lines_entry_id ON journal_entry_lines(journal_entry_id);
CREATE INDEX IF NOT EXISTS idx_customer_payments_company_id ON customer_payments(company_id);
CREATE INDEX IF NOT EXISTS idx_supplier_payments_company_id ON supplier_payments(company_id);
CREATE INDEX IF NOT EXISTS idx_payments_company_id ON payments(company_id);
CREATE INDEX IF NOT EXISTS idx_payments_number ON payments(payment_number);
CREATE INDEX IF NOT EXISTS idx_payments_date ON payments(payment_date);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
CREATE INDEX IF NOT EXISTS idx_invoice_lines_company_id ON invoice_lines(company_id);
CREATE INDEX IF NOT EXISTS idx_invoice_lines_invoice_id ON invoice_lines(invoice_id);
CREATE INDEX IF NOT EXISTS idx_bill_lines_company_id ON bill_lines(company_id);
CREATE INDEX IF NOT EXISTS idx_bill_lines_bill_id ON bill_lines(bill_id);
CREATE INDEX IF NOT EXISTS idx_currencies_company_id ON currencies(company_id);
CREATE INDEX IF NOT EXISTS idx_currencies_code ON currencies(currency_code);
CREATE INDEX IF NOT EXISTS idx_exchange_rates_company_id ON exchange_rates(company_id);
CREATE INDEX IF NOT EXISTS idx_exchange_rates_date ON exchange_rates(rate_date);
CREATE INDEX IF NOT EXISTS idx_trial_balance_reports_company_date ON trial_balance_reports(company_id, report_date);
CREATE INDEX IF NOT EXISTS idx_income_statement_reports_company_period ON income_statement_reports(company_id, from_date, to_date);
CREATE INDEX IF NOT EXISTS idx_balance_sheet_reports_company_date ON balance_sheet_reports(company_id, report_date);
CREATE INDEX IF NOT EXISTS idx_cash_flow_reports_company_period ON cash_flow_reports(company_id, from_date, to_date);
CREATE INDEX IF NOT EXISTS idx_ar_ageing_reports_company_date ON ar_ageing_reports(company_id, report_date);
CREATE INDEX IF NOT EXISTS idx_ap_ageing_reports_company_date ON ap_ageing_reports(company_id, report_date);
