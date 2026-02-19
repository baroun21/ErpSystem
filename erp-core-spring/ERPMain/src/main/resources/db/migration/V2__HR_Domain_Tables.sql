-- ============================================================================
-- NEXORA ERP - HR Domain Tables
-- All HR-related entities with multi-tenant support (company_id)
-- ============================================================================

-- Departments
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50),
    manager_id BIGINT,
    location VARCHAR(255),
    budget DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Positions/Jobs
CREATE TABLE IF NOT EXISTS positions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    department_id BIGINT,
    level VARCHAR(50),
    salary_range_min DECIMAL(15,2),
    salary_range_max DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(department_id) REFERENCES departments(id)
);

-- Locations
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    country VARCHAR(100),
    address TEXT,
    zip_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Employees
CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    position_id BIGINT,
    department_id BIGINT,
    location_id BIGINT,
    hire_date DATE,
    salary DECIMAL(15,2),
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(position_id) REFERENCES positions(id),
    FOREIGN KEY(department_id) REFERENCES departments(id),
    FOREIGN KEY(location_id) REFERENCES locations(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

-- Attendance
CREATE TABLE IF NOT EXISTS attendance (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(50),  -- Present, Absent, Late, Half Day
    check_in_time TIME,
    check_out_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id),
    UNIQUE(company_id, employee_id, attendance_date)
);

-- Leave Requests
CREATE TABLE IF NOT EXISTS leave_requests (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(50),  -- Vacation, Sick, Personal, etc.
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(50),  -- Pending, Approved, Rejected
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id)
);

-- Holidays
CREATE TABLE IF NOT EXISTS holidays (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    name VARCHAR(255) NOT NULL,
    holiday_date DATE NOT NULL,
    holiday_type VARCHAR(50),
    is_paid BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Salary
CREATE TABLE IF NOT EXISTS salary (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    base_salary DECIMAL(15,2),
    currency VARCHAR(3),
    effective_date DATE,
    salary_structure TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id)
);

-- Payroll
CREATE TABLE IF NOT EXISTS payroll (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    payroll_period VARCHAR(50),
    gross_salary DECIMAL(15,2),
    net_salary DECIMAL(15,2),
    status VARCHAR(50),  -- Pending, Processed, Paid
    payment_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id)
);

-- Deductions
CREATE TABLE IF NOT EXISTS deductions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    deduction_type VARCHAR(50),  -- Tax, Insurance, Loan, etc.
    amount DECIMAL(15,2),
    frequency VARCHAR(50),  -- Monthly, Quarterly, Annually, One-time
    start_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id)
);

-- Performance Reviews
CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    reviewer_id BIGINT,
    rating INTEGER,  -- 1-5
    feedback TEXT,
    review_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id),
    FOREIGN KEY(reviewer_id) REFERENCES employees(id)
);

-- Goals
CREATE TABLE IF NOT EXISTS goals (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    employee_id BIGINT NOT NULL,
    goal_title VARCHAR(255),
    description TEXT,
    progress INTEGER,  -- 0-100
    status VARCHAR(50),
    target_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id),
    FOREIGN KEY(employee_id) REFERENCES employees(id)
);

-- HR Roles & Permissions
CREATE TABLE IF NOT EXISTS hr_roles (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    legal_entity_id BIGINT,
    role_name VARCHAR(255),
    description TEXT,
    permissions TEXT,  -- JSON array of permissions
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(legal_entity_id) REFERENCES legal_entities(id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_employees_company_id ON employees(company_id);
CREATE INDEX IF NOT EXISTS idx_employees_department_id ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_attendance_company_id ON attendance(company_id);
CREATE INDEX IF NOT EXISTS idx_attendance_employee_id ON attendance(employee_id);
CREATE INDEX IF NOT EXISTS idx_leave_requests_company_id ON leave_requests(company_id);
CREATE INDEX IF NOT EXISTS idx_leave_requests_employee_id ON leave_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_payroll_company_id ON payroll(company_id);
CREATE INDEX IF NOT EXISTS idx_payroll_employee_id ON payroll(employee_id);
