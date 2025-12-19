CREATE TABLE addresses (
    address_id SERIAL PRIMARY KEY,
    street_name VARCHAR(128) NOT NULL,
    house_number VARCHAR(20) NOT NULL,
    city VARCHAR(20) NOT NULL,
    country VARCHAR(20) NOT NULL,
    zipcode INT NOT NULL
);

CREATE TABLE staff (
    staff_id SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    date_of_birth DATE,
    salary NUMERIC(15,2) NOT NULL,
    commission NUMERIC(3,3),
    branch_id INT,
    address_id INT,
    CONSTRAINT fk_staff_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

CREATE TABLE customer (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    date_of_birth DATE NOT NULL,
    license_number VARCHAR(6) NOT NULL,
    address_id INT,
    CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

CREATE TABLE branch (
    branch_id SERIAL PRIMARY KEY,
    manager_id INT,
    parking_spaces INT,
    address_id INT,
    CONSTRAINT fk_branch_address FOREIGN KEY (address_id) REFERENCES addresses(address_id),
    CONSTRAINT fk_branch_manager FOREIGN KEY (manager_id) REFERENCES staff(staff_id)
);

ALTER TABLE staff
ADD CONSTRAINT fk_staff_branch FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

CREATE TABLE cost (
    cost_id SERIAL PRIMARY KEY,
    cost_class VARCHAR(128) NOT NULL,
    cost_per_day NUMERIC(15,2),
    cost_per_kilometer NUMERIC(15,2),
    security_deposit NUMERIC(15,2)
);

CREATE TABLE vehicle (
    vehicle_id SERIAL PRIMARY KEY,
    brand VARCHAR(50),
    mileage INT,
    date_bought DATE,
    is_available BOOLEAN NOT NULL,
    cost_id INT NOT NULL,
    branch_id INT NOT NULL,
    CONSTRAINT fk_vehicle_cost FOREIGN KEY (cost_id) REFERENCES cost(cost_id),
    CONSTRAINT fk_vehicle_branch FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);

CREATE TABLE manages (
    manage_id SERIAL PRIMARY KEY,
    staff_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    vehicle_condition VARCHAR(128),
    vehicle_managed_date DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_manage_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
    CONSTRAINT fk_manage_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

CREATE TABLE rent (
    rent_id SERIAL PRIMARY KEY,
    vehicle_id INT NOT NULL,
    trip_duration INT NOT NULL,
    free_kilometers INT DEFAULT 100,
    customer_id INT NOT NULL,
    staff_id INT NOT NULL,
    is_returned BOOLEAN DEFAULT FALSE,
    date_rented DATE DEFAULT CURRENT_DATE,
    date_returned DATE,
    mileage_returned INT,
    CONSTRAINT fk_rent_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),
    CONSTRAINT fk_rent_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT fk_rent_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

CREATE UNIQUE INDEX ux_customer_active_rent
ON rent(customer_id)
WHERE is_returned = FALSE;

CREATE TABLE payment (
    rent_id INT NOT NULL,
    payment_amount NUMERIC(15,2) NOT NULL,
    payment_date DATE NOT NULL,
    PRIMARY KEY (rent_id, payment_date),
    CONSTRAINT fk_payment_rent FOREIGN KEY (rent_id) REFERENCES rent(rent_id)
);