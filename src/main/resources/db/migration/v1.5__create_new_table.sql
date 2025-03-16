CREATE TABLE address (
     address_id SERIAL PRIMARY KEY,
     street VARCHAR(255) NOT NULL,
     postal_code VARCHAR(20),
     district_id INT NOT NULL,
     additional_info TEXT,
     FOREIGN KEY (district_id) REFERENCES districts(id) ON DELETE CASCADE
);

CREATE INDEX idx_address_district ON address(district_id);