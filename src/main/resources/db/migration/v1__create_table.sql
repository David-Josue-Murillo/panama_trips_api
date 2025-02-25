-- Crear la base de datos
--CREATE DATABASE panama_trips;

-- Usar la base de datos
--\c panama_trips;

-- Crear tipos ENUM para estados
CREATE TYPE reservation_status AS ENUM ('pending', 'confirmed', 'cancelled');
CREATE TYPE payment_status AS ENUM ('pending', 'completed', 'failed');

-- Crear tabla de provincias
CREATE TABLE IF NOT EXISTS provinces (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla de distritos
CREATE TABLE IF NOT EXISTS districts (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(50) NOT NULL,
                           province_id INT REFERENCES provinces(id) ON DELETE CASCADE
);

-- Crear tabla de calles
CREATE TABLE IF NOT EXISTS streets (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(50) NOT NULL,
                         district_id INT REFERENCES districts(id) ON DELETE CASCADE
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       dni VARCHAR(13) NOT NULL,
                       name VARCHAR(75) NOT NULL,
                       lastname VARCHAR(75) NOT NULL,
                       email VARCHAR(150) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       profile_image_url VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       created_by INT REFERENCES users(id) ON DELETE SET NULL, -- Auditoría
                       updated_by INT REFERENCES users(id) ON DELETE SET NULL, -- Auditoría
                       deleted_at TIMESTAMP -- Auditoría
);

-- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla de permisos
CREATE TABLE IF NOT EXISTS permissions (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(50) UNIQUE NOT NULL
);

-- Relación de usuarios con roles (1 usuario -> 1 rol)
ALTER TABLE users ADD COLUMN role_id INT REFERENCES roles(id) ON DELETE SET NULL;

-- Relación de roles con permisos (Muchos a Muchos)
CREATE TABLE IF NOT EXISTS roles_permissions (
                                  role_id INT REFERENCES roles(id) ON DELETE CASCADE,
                                  permission_id INT REFERENCES permissions(id) ON DELETE CASCADE,
                                  PRIMARY KEY (role_id, permission_id)
);

-- Crear tabla de proveedores de turismo
CREATE TABLE IF NOT EXISTS providers (
                           id SERIAL PRIMARY KEY,
                           ruc VARCHAR(25) NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) UNIQUE NOT NULL,
                           phone VARCHAR(20),
                           province_id INT REFERENCES provinces(id) ON DELETE SET NULL, -- Normalizado
                           district_id INT REFERENCES districts(id) ON DELETE SET NULL, -- Normalizado
                           street_id INT REFERENCES streets(id) ON DELETE SET NULL, -- Normalizado
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de planes turísticos
CREATE TABLE IF NOT EXISTS tour_plans (
                            id SERIAL PRIMARY KEY,
                            provider_id INT REFERENCES providers(id) ON DELETE CASCADE,
                            title VARCHAR(150) NOT NULL,
                            description TEXT,
                            price DECIMAL(10,2) NOT NULL,
                            duration INT NOT NULL, -- duración en días
                            available_spots INT NOT NULL CHECK (available_spots >= 0),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de regiones
CREATE TABLE IF NOT EXISTS regions (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) UNIQUE NOT NULL,
                         province VARCHAR(50) NOT NULL,
                         comarca VARCHAR(50) -- Hacer opcional
);

-- Relación de planes turísticos con regiones (Muchos a Muchos)
CREATE TABLE IF NOT EXISTS tour_plan_regions (
                                   tour_plan_id INT REFERENCES tour_plans(id) ON DELETE CASCADE,
                                   region_id INT REFERENCES regions(id) ON DELETE CASCADE,
                                   PRIMARY KEY (tour_plan_id, region_id)
);

-- Crear tabla de reservas
CREATE TABLE IF NOT EXISTS reservations (
                              id SERIAL PRIMARY KEY,
                              user_id INT REFERENCES users(id) ON DELETE CASCADE,
                              tour_plan_id INT REFERENCES tour_plans(id) ON DELETE CASCADE,
                              reservation_status reservation_status DEFAULT 'pending', -- Usar ENUM
                              reservation_date DATE NOT NULL,
                              total_price DECIMAL(10,2) NOT NULL CHECK (total_price >= 0),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de pagos
CREATE TABLE IF NOT EXISTS payments (
                          id SERIAL PRIMARY KEY,
                          reservation_id INT REFERENCES reservations(id) ON DELETE CASCADE,
                          transaction_id VARCHAR(100) UNIQUE NOT NULL,
                          amount DECIMAL(10,2) NOT NULL,
                          payment_status payment_status DEFAULT 'pending', -- Usar ENUM
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de reseñas
CREATE TABLE IF NOT EXISTS reviews (
                         id SERIAL PRIMARY KEY,
                         user_id INT REFERENCES users(id) ON DELETE CASCADE,
                         tour_plan_id INT REFERENCES tour_plans(id) ON DELETE CASCADE,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de cupones de descuento
CREATE TABLE IF NOT EXISTS coupons (
                         id SERIAL PRIMARY KEY,
                         code VARCHAR(50) UNIQUE NOT NULL,
                         discount_percentage INT CHECK (discount_percentage BETWEEN 0 AND 99),
                         expiration_date DATE NOT NULL
);

-- Crear tabla de descuentos aplicados a reservas
CREATE TABLE IF NOT EXISTS discounts (
                           id SERIAL PRIMARY KEY,
                           reservation_id INT REFERENCES reservations(id) ON DELETE CASCADE,
                           coupon_id INT REFERENCES coupons(id) ON DELETE SET NULL,
                           discount_amount DECIMAL(10,2) NOT NULL
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_reservations_user_id ON reservations(user_id);
CREATE INDEX idx_payments_transaction ON payments(transaction_id);
CREATE INDEX idx_reservations_reservation_date ON reservations(reservation_date); -- Índice adicional
CREATE INDEX idx_coupons_expiration_date ON coupons(expiration_date); -- Índice adicional