-- 1. Roles jadvali
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users jadvali
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT,
    driver_license_number VARCHAR(50) NOT NULL,
    passport_image_url TEXT,
    license_image_url TEXT,
    profile_image_url TEXT,
    is_active BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. User-Roles bog'lovchi jadval
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 4. Car Companies jadvali
CREATE TABLE car_companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    address TEXT,
    contact_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Car Variants jadvali
CREATE TABLE car_variants (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    model VARCHAR(100) NOT NULL,
    variant_name VARCHAR(100) NOT NULL,
    fuel_type VARCHAR(20),
    transmission VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES car_companies(id)
);

-- 6. Cars jadvali
CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    variant_id BIGINT NOT NULL,
    registration_number VARCHAR(20) NOT NULL UNIQUE,
    color VARCHAR(30),
    hourly_rate DOUBLE PRECISION NOT NULL,
    daily_rate DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    doors INTEGER,
    passenger_capacity INTEGER,
    luggage_capacity INTEGER,
    has_air_conditioning BOOLEAN DEFAULT TRUE,
    additional_features TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_variant FOREIGN KEY (variant_id) REFERENCES car_variants(id)
);

-- 7. Car Images jadvali
CREATE TABLE car_images (
    id BIGSERIAL PRIMARY KEY,
    car_id BIGINT NOT NULL,
    image_url TEXT NOT NULL,
    CONSTRAINT fk_car_images FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE
);

-- 8. Bookings jadvali
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    pick_up_time TIMESTAMP NOT NULL,
    drop_off_time TIMESTAMP NOT NULL,
    pick_up_location TEXT NOT NULL,
    drop_off_location TEXT NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_booking_car FOREIGN KEY (car_id) REFERENCES cars(id)
);

-- 9. Payments jadvali
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    amount DOUBLE PRECISION NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- 10. Refresh Tokens jadvali
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 11. Standart Rollarni va Adminni qo'shish
INSERT INTO roles (name) VALUES ('ROLE_CUSTOMER'), ('ROLE_ADMIN');