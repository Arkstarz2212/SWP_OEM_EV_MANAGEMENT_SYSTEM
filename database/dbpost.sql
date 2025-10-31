-- AOEM EV Warranty Management - PostgreSQL Database (Schema + Sample Data)
-- Execute this file on PostgreSQL to create schema and seed minimal test data

-- ============================================================================
-- SCHEMA: aoem
-- ============================================================================
CREATE SCHEMA IF NOT EXISTS aoem;

-- ============================================================================
-- 1. OEM MANUFACTURERS
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.oem_manufacturers (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    contact TEXT NULL  -- JSON
);

-- ============================================================================
-- 2. SERVICE CENTERS
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.service_centers (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    region VARCHAR(50) NOT NULL,
    contact_info TEXT NULL,  -- JSON
    oem_id INTEGER NULL REFERENCES aoem.oem_manufacturers(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(20) NULL
);

-- ============================================================================
-- 3. OEM ↔ SERVICE CENTER MAPPING (Many-to-Many)
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.oem_service_centers (
    id SERIAL PRIMARY KEY,
    oem_id INTEGER NOT NULL REFERENCES aoem.oem_manufacturers(id),
    service_center_id INTEGER NOT NULL REFERENCES aoem.service_centers(id)
);

-- ============================================================================
-- 4. ROLES
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.roles (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE, -- e.g. Admin, EVM_Staff, SC_Staff, SC_Technician
    name VARCHAR(100) NOT NULL,
    description TEXT NULL
);

-- ============================================================================
-- 5. USERS
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(200) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    role VARCHAR(20) NOT NULL REFERENCES aoem.roles(code),  -- Admin, EVM_Staff, SC_Staff, SC_Technician
    service_center_id INTEGER NULL REFERENCES aoem.service_centers(id),
    profile_data TEXT NULL, -- JSON
    auth_info TEXT NULL,    -- JSON
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================================
-- 6. PARTS CATALOG
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.parts_catalog (
    id SERIAL PRIMARY KEY,
    oem_id INTEGER NOT NULL REFERENCES aoem.oem_manufacturers(id),
    part_number VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    part_data TEXT NULL  -- JSON
);

-- ============================================================================
-- 7. VEHICLES
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.vehicles (
    id SERIAL PRIMARY KEY,
    vin VARCHAR(20) NOT NULL UNIQUE,
    oem_id INTEGER NOT NULL REFERENCES aoem.oem_manufacturers(id),
    model VARCHAR(100) NOT NULL,
    model_year INTEGER NOT NULL,
    customer_info TEXT NULL, -- JSON field containing customer basic information (fullName, phoneNumber, email, address, city, province, postalCode)
    vehicle_data TEXT NULL, -- JSON
    warranty_info TEXT NULL, -- JSON
    status VARCHAR(20) DEFAULT 'active' -- Vehicle status: active, inactive, deleted. Used for soft delete functionality.
);

-- [REMOVED] VEHICLE COMPONENTS (VIN ↔ Serial mapping)
-- Dropped per scope reduction: no per-vehicle part serial tracking

-- ============================================================================
-- 8. WARRANTY POLICIES
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.warranty_policies (
    id SERIAL PRIMARY KEY,
    oem_id INTEGER NOT NULL REFERENCES aoem.oem_manufacturers(id),
    policy_name VARCHAR(200) NOT NULL,
    policy_code VARCHAR(50) NOT NULL,
    
    -- Thời gian bảo hành
    warranty_months INTEGER NOT NULL DEFAULT 36,
    warranty_km INTEGER NULL,
    
    -- Chi tiết coverage (JSON đơn giản)
    battery_coverage_months INTEGER DEFAULT 36,
    battery_coverage_km INTEGER NULL,
    motor_coverage_months INTEGER DEFAULT 36,
    motor_coverage_km INTEGER NULL,
    inverter_coverage_months INTEGER DEFAULT 24,
    inverter_coverage_km INTEGER NULL,
    
    -- Trạng thái
    is_active BOOLEAN DEFAULT TRUE,
    is_default BOOLEAN DEFAULT FALSE,
    
    -- Thời gian hiệu lực
    effective_from DATE NOT NULL DEFAULT CURRENT_DATE,
    effective_to DATE NULL,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP NULL
);

-- ============================================================================
-- 8b. VEHICLE WARRANTY (Áp dụng chính sách cho xe)
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.vehicle_warranties (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL REFERENCES aoem.vehicles(id),
    warranty_policy_id INTEGER NOT NULL REFERENCES aoem.warranty_policies(id),
    
    -- Thời gian bảo hành của xe này
    warranty_start_date DATE NOT NULL,
    warranty_end_date DATE NOT NULL,
    
    -- Trạng thái
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT NOW(),
    created_by_user_id INTEGER REFERENCES aoem.users(id)
);

-- ============================================================================
-- 9. WARRANTY CLAIMS
-- ============================================================================
CREATE TABLE IF NOT EXISTS aoem.warranty_claims (
    id SERIAL PRIMARY KEY,
    claim_number VARCHAR(50) NOT NULL UNIQUE,
    vehicle_id INTEGER NOT NULL REFERENCES aoem.vehicles(id),
    service_center_id INTEGER NOT NULL REFERENCES aoem.service_centers(id),
    created_by_user_id INTEGER NOT NULL REFERENCES aoem.users(id),
    assigned_technician_id INTEGER NULL REFERENCES aoem.users(id),
    evm_reviewer_user_id INTEGER NULL REFERENCES aoem.users(id),
    status VARCHAR(50) NOT NULL, -- draft, submitted, under_review, approved, completed
    issue_description TEXT NOT NULL,
    dtc_code VARCHAR(50) NULL,
    mileage_km_at_claim INTEGER NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    submitted_at TIMESTAMP NULL,
    approved_at TIMESTAMP NULL
);

-- =========================================================================
-- 10. WARRANTY CLAIM STATUS LOGS
-- =========================================================================
CREATE TABLE IF NOT EXISTS aoem.warranty_claim_status_logs (
    id SERIAL PRIMARY KEY,
    claim_id INTEGER NOT NULL REFERENCES aoem.warranty_claims(id) ON DELETE CASCADE,
    from_status VARCHAR(50) NULL,
    to_status VARCHAR(50) NOT NULL,
    changed_by_user_id INTEGER NOT NULL REFERENCES aoem.users(id),
    note TEXT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================================
-- 11. SERVICE RECORDS (service history & handover)
-- =========================================================================
CREATE TABLE IF NOT EXISTS aoem.service_records (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL REFERENCES aoem.vehicles(id),
    service_center_id INTEGER NOT NULL REFERENCES aoem.service_centers(id),
    claim_id INTEGER NULL REFERENCES aoem.warranty_claims(id) ON DELETE SET NULL,
    technician_id INTEGER NULL REFERENCES aoem.users(id),
    service_type VARCHAR(30) NOT NULL, -- warranty, maintenance
    description TEXT NOT NULL,
    performed_at TIMESTAMP NOT NULL,
    result TEXT NULL,
    handover_at TIMESTAMP NULL
);

-- ============================================================================
-- INDEXES FOR PERFORMANCE
-- ============================================================================

-- Warranty Policies indexes
CREATE INDEX IF NOT EXISTS idx_warranty_policies_oem ON aoem.warranty_policies(oem_id);
CREATE INDEX IF NOT EXISTS idx_warranty_policies_active ON aoem.warranty_policies(is_active);
CREATE INDEX IF NOT EXISTS idx_warranty_policies_default ON aoem.warranty_policies(oem_id, is_default) WHERE is_default = TRUE;
CREATE INDEX IF NOT EXISTS idx_warranty_policies_effective_dates ON aoem.warranty_policies(effective_from, effective_to);

-- Vehicle Warranties indexes
CREATE INDEX IF NOT EXISTS idx_vehicle_warranties_vehicle ON aoem.vehicle_warranties(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_warranties_active ON aoem.vehicle_warranties(is_active);
CREATE INDEX IF NOT EXISTS idx_vehicle_warranties_policy ON aoem.vehicle_warranties(warranty_policy_id);

-- Vehicles indexes
CREATE INDEX IF NOT EXISTS idx_vehicles_status ON aoem.vehicles(status);

-- ============================================================================
-- SAMPLE DATA (minimal, validated for use cases)
-- ============================================================================

-- 1. OEM MANUFACTURERS
INSERT INTO aoem.oem_manufacturers (code, name, contact) VALUES
('VFS', 'VinFast', '{"email":"support@vinfast.vn","phone":"1900-23-23-89","address":"Hanoi, Vietnam"}'),
('BYD', 'BYD Auto', '{"email":"info@byd.com","phone":"400-8888-888","address":"Shenzhen, China"}'),
('TES', 'Tesla Motors', '{"email":"service@tesla.com","phone":"1-877-798-3752","address":"Austin, Texas, USA"}')
ON CONFLICT (code) DO NOTHING;

-- 2. SERVICE CENTERS
INSERT INTO aoem.service_centers (code, name, region, contact_info) VALUES
('VFS-HN01', 'VinFast Service Hanoi', 'North', '{"address":"123 Nguyen Trai, Hanoi","phone":"024-3333-4444","email":"hanoi@vinfast.vn","contact_person":"Nguyen Van A","license":"SC001-HN"}'),
('VFS-HCM01', 'VinFast Service Ho Chi Minh', 'South', '{"address":"456 Le Van Sy, HCM","phone":"028-5555-6666","email":"hcm@vinfast.vn","contact_person":"Tran Thi B","license":"SC002-HCM"}'),
('BYD-HN01', 'BYD Service Hanoi', 'North', '{"address":"789 Cau Giay, Hanoi","phone":"024-7777-8888","email":"hanoi@byd.vn","contact_person":"Le Van C","license":"SC003-HN"}')
ON CONFLICT (code) DO NOTHING;

-- 3. OEM ↔ SERVICE CENTER MAPPING
INSERT INTO aoem.oem_service_centers (oem_id, service_center_id) VALUES
(1, 1),
(1, 2),
(2, 3)
ON CONFLICT DO NOTHING;

-- 4. ROLES (seed first to satisfy FK)
INSERT INTO aoem.roles (code, name, description) VALUES
('Admin', 'Administrator', 'Full system access'),
('EVM_Staff', 'EV Manufacturer Staff', 'OEM side staff managing warranty & campaigns'),
('SC_Staff', 'Service Center Staff', 'Front desk and service advisors'),
('SC_Technician', 'Service Center Technician', 'Technician handling repairs and recalls')
ON CONFLICT (code) DO NOTHING;

-- 5. USERS
INSERT INTO aoem.users (email, password_hash, full_name, role, service_center_id, profile_data, auth_info, is_active) VALUES
('admin@aoem.com', 'hashed_password_1', 'System Administrator', 'Admin', NULL, '{"phone":"0901-111-111","mfa_enabled":true}', '{"session_token":null,"last_login":null}', TRUE),
('admin@aoem1.com', '6G94qKPK8LYNjnTllCqm2G3BUM08AzOK7yW30tfjrMc=', 'Test Administrator', 'Admin', NULL, '{"phone":"0901-111-111","mfa_enabled":true}', '{"session_token":null,"last_login":null}', TRUE),
('admin@example.com', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=', 'Example Administrator', 'Admin', NULL, '{"phone":"0909-999-999","mfa_enabled":true}', '{"session_token":null,"last_login":null}', TRUE),
('evm.staff1@vinfast.vn', 'hashed_password_2', 'Nguyen EVM Staff', 'EVM_Staff', NULL, '{"phone":"0902-222-222","department":"Warranty"}', '{"session_token":null,"last_login":null}', TRUE),
('evm.staff2@byd.com', 'hashed_password_3', 'Li EVM Staff', 'EVM_Staff', NULL, '{"phone":"0903-333-333","department":"Technical"}', '{"session_token":null,"last_login":null}', TRUE),
('sc.staff1@vinfast.vn', 'hashed_password_4', 'Tran SC Staff HN', 'SC_Staff', 1, '{"phone":"0904-444-444","shift":"morning"}', '{"session_token":null,"last_login":null}', TRUE),
('sc.staff2@vinfast.vn', 'hashed_password_5', 'Le SC Staff HCM', 'SC_Staff', 2, '{"phone":"0905-555-555","shift":"afternoon"}', '{"session_token":null,"last_login":null}', TRUE),
('tech1@vinfast.vn', 'hashed_password_6', 'Pham Technician HN', 'SC_Technician', 1, '{"phone":"0906-666-666","specialization":"battery,motor"}', '{"session_token":null,"last_login":null}', TRUE),
('tech2@vinfast.vn', 'hashed_password_7', 'Vo Technician HCM', 'SC_Technician', 2, '{"phone":"0907-777-777","specialization":"inverter,bms"}', '{"session_token":null,"last_login":null}', TRUE),
('customer1@gmail.com', 'hashed_password_8', 'Nguyen Van Customer', 'SC_Staff', NULL, '{"phone":"0908-888-888","address":"123 ABC Street, Hanoi"}', '{"session_token":null,"last_login":null}', TRUE)
ON CONFLICT (email) DO NOTHING;

-- 6. VEHICLES
INSERT INTO aoem.vehicles (vin, oem_id, model, model_year, customer_info, vehicle_data, warranty_info, status) VALUES
('VF8ABC123456789012', 1, 'VF8', 2024, '{"fullName":"Nguyễn Văn An","phoneNumber":"0901234567","email":"nguyenvanan@gmail.com","address":"123 Đường ABC, Phường 1","city":"Hà Nội","province":"Hà Nội","postalCode":"100000"}', '{"variant":"Plus","odometer_km":15000,"color":"White","purchase_date":"2024-01-15"}', '{"start_date":"2024-01-15","end_date":"2032-01-15","km_limit":160000,"policies":[{"component":"battery","months":96,"km":160000,"service_coverage":"free","parts_coverage":"free"},{"component":"motor","months":60,"km":100000,"service_coverage":"free","parts_coverage":"free"},{"component":"inverter","months":36,"km":80000,"service_coverage":"paid","parts_coverage":"free"}],"certificates":[{"number":"WC-VF8-001","status":"active","issue_date":"2024-01-15","expiry_date":"2032-01-15"}],"coverage_rules":{"out_of_warranty":{"service_coverage":"paid","parts_coverage":"paid"},"accident_damage":{"service_coverage":"paid","parts_coverage":"paid"},"normal_wear":{"service_coverage":"partial","parts_coverage":"paid"}}}', 'active'),
('VF9XYZ987654321098', 1, 'VF9', 2024, '{"fullName":"Trần Thị Bình","phoneNumber":"0907654321","email":"tranthibinh@gmail.com","address":"456 Đường XYZ, Phường 2","city":"TP.HCM","province":"TP.HCM","postalCode":"700000"}', '{"variant":"Eco","odometer_km":8000,"color":"Blue","purchase_date":"2024-03-10"}', '{"start_date":"2024-03-10","end_date":"2032-03-10","km_limit":160000,"policies":[{"component":"battery","months":96,"km":160000,"service_coverage":"free","parts_coverage":"free"},{"component":"motor","months":60,"km":100000,"service_coverage":"free","parts_coverage":"free"}],"certificates":[{"number":"WC-VF9-001","status":"active","issue_date":"2024-03-10","expiry_date":"2032-03-10"}],"coverage_rules":{"out_of_warranty":{"service_coverage":"paid","parts_coverage":"paid"}}}', 'active'),
('BYDHAN111222333444', 2, 'Han EV', 2024, '{"fullName":"Lê Minh Cường","phoneNumber":"0909876543","email":"leminhcuong@gmail.com","address":"789 Đường DEF, Phường 3","city":"Đà Nẵng","province":"Đà Nẵng","postalCode":"500000"}', '{"variant":"Standard","odometer_km":12000,"color":"Black","purchase_date":"2024-02-20"}', '{"start_date":"2024-02-20","end_date":"2030-02-20","km_limit":120000,"policies":[{"component":"battery","months":72,"km":120000}],"certificates":[{"number":"WC-BYD-001","status":"active"}]}', 'active')
ON CONFLICT (vin) DO NOTHING;

-- 7. PARTS CATALOG
INSERT INTO aoem.parts_catalog (oem_id, part_number, name, category, part_data) VALUES
(1, 'VF-BAT-8-001', 'VF8 Battery Pack 87.7kWh', 'battery', '{"description":"Main battery pack for VF8","vehicle_models":["VF8"],"unit_cost":15000,"manufacturer":"CATL"}'),
(1, 'VF-MOT-8-001', 'VF8 Front Motor 150kW', 'motor', '{"description":"Front electric motor","vehicle_models":["VF8"],"unit_cost":3000,"manufacturer":"Bosch"}'),
(1, 'VF-BMS-8-001', 'VF8 Battery Management System', 'bms', '{"description":"BMS for VF8 battery","vehicle_models":["VF8"],"unit_cost":500,"manufacturer":"VinFast"}'),
(1, 'VF-INV-8-001', 'VF8 Inverter 150kW', 'inverter', '{"description":"Main inverter","vehicle_models":["VF8"],"unit_cost":2000,"manufacturer":"Continental"}'),
(1, 'VF-BAT-9-001', 'VF9 Battery Pack 123kWh', 'battery', '{"description":"Main battery pack for VF9","vehicle_models":["VF9"],"unit_cost":18000,"manufacturer":"CATL"}'),
(1, 'VF-MOT-9-001', 'VF9 Rear Motor 200kW', 'motor', '{"description":"Rear electric motor","vehicle_models":["VF9"],"unit_cost":4000,"manufacturer":"Bosch"}'),
(2, 'BYD-BAT-HAN-001', 'Han EV Blade Battery 85.4kWh', 'battery', '{"description":"BYD Blade battery for Han","vehicle_models":["Han EV"],"unit_cost":12000,"manufacturer":"BYD"}'),
(2, 'BYD-MOT-HAN-001', 'Han EV Motor 163kW', 'motor', '{"description":"Permanent magnet motor","vehicle_models":["Han EV"],"unit_cost":2800,"manufacturer":"BYD"}')
ON CONFLICT (part_number) DO NOTHING;

-- 8. WARRANTY POLICIES
INSERT INTO aoem.warranty_policies (
    oem_id, policy_name, policy_code,
    warranty_months, warranty_km,
    battery_coverage_months, battery_coverage_km,
    motor_coverage_months, motor_coverage_km,
    inverter_coverage_months, inverter_coverage_km,
    is_default
) VALUES 
-- Chính sách bảo hành VinFast
(1, 'VinFast Standard Warranty', 'VFS-STD-2024',
    96, 160000,
    96, 160000,  -- Battery: 8 năm / 160k km
    60, 100000,  -- Motor: 5 năm / 100k km  
    36, 80000,   -- Inverter: 3 năm / 80k km
    TRUE),
-- Chính sách bảo hành BYD
(2, 'BYD Standard Warranty', 'BYD-STD-2024',
    72, 120000,
    72, 120000,  -- Battery: 6 năm / 120k km
    48, 80000,   -- Motor: 4 năm / 80k km
    36, 60000,   -- Inverter: 3 năm / 60k km
    TRUE);

-- 8b. VEHICLE WARRANTY APPLICATIONS
INSERT INTO aoem.vehicle_warranties (
    vehicle_id, warranty_policy_id, warranty_start_date, warranty_end_date, created_by_user_id
) VALUES 
(1, 1, '2024-01-15', '2032-01-15', 4), -- VF8
(2, 1, '2024-03-10', '2032-03-10', 4), -- VF9
(3, 2, '2024-02-20', '2030-02-20', 4); -- BYD Han

-- [REMOVED] VEHICLE COMPONENTS seed

-- 9. WARRANTY CLAIMS
INSERT INTO aoem.warranty_claims (claim_number, vehicle_id, service_center_id, created_by_user_id, assigned_technician_id, evm_reviewer_user_id, status, issue_description, dtc_code, mileage_km_at_claim, submitted_at, approved_at) VALUES
('WC-2024-001', 1, 1, 4, 6, 2, 'completed', 'Battery cell degradation detected, capacity reduced to 75%', 'P0A80', 15000, '2024-12-01T08:00:00Z', '2024-12-01T14:00:00Z'),
('WC-2024-002', 2, 2, 5, NULL, 2, 'under_review', 'Motor making unusual noise during acceleration', 'P0A1F', 8000, '2024-12-15T10:00:00Z', NULL),
('WC-2024-003', 3, 3, 4, NULL, NULL, 'draft', 'BMS showing error codes intermittently', 'P0AA6', 12000, NULL, NULL)
ON CONFLICT (claim_number) DO NOTHING;

-- [REMOVED] CAMPAIGNS seed

-- 10b. STATUS LOGS seed
INSERT INTO aoem.warranty_claim_status_logs (claim_id, from_status, to_status, changed_by_user_id, note, changed_at) VALUES
(1, NULL, 'draft', 4, 'Initial draft created', '2024-12-01T07:30:00Z'),
(1, 'draft', 'submitted', 4, 'Submitted to OEM', '2024-12-01T08:00:00Z'),
(1, 'submitted', 'approved', 2, 'Approved - within warranty', '2024-12-01T14:00:00Z'),
(1, 'approved', 'completed', 6, 'Work completed and vehicle handed over', '2024-12-02T16:00:00Z'),
(2, NULL, 'draft', 5, 'Created draft for noisy motor', '2024-12-15T09:30:00Z'),
(2, 'draft', 'submitted', 5, 'Submitted to OEM for review', '2024-12-15T10:00:00Z')
ON CONFLICT DO NOTHING;

-- 11b. SERVICE RECORDS seed
INSERT INTO aoem.service_records (vehicle_id, service_center_id, claim_id, technician_id, service_type, description, performed_at, result, handover_at) VALUES
(1, 1, 1, 6, 'warranty', 'Replace main battery under warranty', '2024-12-02T09:00:00Z', 'Battery replaced, BMS reset, test drive OK', '2024-12-02T16:00:00Z')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- COMMENTS AND DOCUMENTATION
-- ============================================================================

COMMENT ON TABLE aoem.warranty_policies IS 'Chính sách bảo hành của các OEM';
COMMENT ON TABLE aoem.vehicle_warranties IS 'Áp dụng chính sách bảo hành cho từng xe cụ thể';
COMMENT ON TABLE aoem.vehicles IS 'Bảng xe - lưu trữ thông tin khách hàng trực tiếp trong customer_info JSON field thay vì mapping với bảng users';
