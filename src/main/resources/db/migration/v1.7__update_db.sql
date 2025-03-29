-- Update of the tour_plans table with additional important fields

-- First, add additional basic fields
ALTER TABLE tour_plans
    ADD COLUMN slug VARCHAR(200) UNIQUE,
    ADD COLUMN short_description VARCHAR(255),
    ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE',
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN created_by INTEGER REFERENCES users(id) ON DELETE SET NULL,
    ADD COLUMN updated_by INTEGER REFERENCES users(id) ON DELETE SET NULL;

-- Schedule and availability information.
ALTER TABLE tour_plans
    ADD COLUMN start_time VARCHAR(8),
    ADD COLUMN end_time VARCHAR(8),
    ADD COLUMN available_days JSONB DEFAULT '["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"]'::jsonb,
    ADD COLUMN is_seasonal BOOLEAN DEFAULT FALSE,
    ADD COLUMN season_start_date DATE,
    ADD COLUMN season_end_date DATE,
    ADD COLUMN max_capacity_per_day INTEGER;

-- Specific location information.
ALTER TABLE tour_plans
    ADD COLUMN meeting_point VARCHAR(255),
    ADD COLUMN meeting_point_coordinates VARCHAR(100),
    ADD COLUMN tour_route TEXT;

-- Additional commercial information.
ALTER TABLE tour_plans
    ADD COLUMN child_price NUMERIC(10, 2),
    ADD COLUMN min_participants INTEGER DEFAULT 1,
    ADD COLUMN max_participants INTEGER DEFAULT 10,
    ADD COLUMN currency CHAR(3) DEFAULT 'USD',
    ADD COLUMN discount_percentage DECIMAL(5, 2) DEFAULT 0,
    ADD COLUMN tax_percentage DECIMAL(5, 2) DEFAULT 0;

-- Booking configuration
ALTER TABLE tour_plans
    ADD COLUMN booking_deadline_hours INTEGER DEFAULT 24,
    ADD COLUMN min_advance_booking_days INTEGER DEFAULT 1,
    ADD COLUMN max_advance_booking_days INTEGER DEFAULT 90,
    ADD COLUMN requires_approval BOOLEAN DEFAULT FALSE,
    ADD COLUMN allow_instant_booking BOOLEAN DEFAULT TRUE;

-- Tour features
ALTER TABLE tour_plans
    ADD COLUMN difficulty_level VARCHAR(20) DEFAULT 'EASY',
    ADD COLUMN recommended_age VARCHAR(20) DEFAULT 'All ages',
    ADD COLUMN wheelchair_accessible BOOLEAN DEFAULT FALSE,
    ADD COLUMN included_services JSONB,
    ADD COLUMN excluded_services JSONB,
    ADD COLUMN what_to_bring JSONB,
    ADD COLUMN tags JSONB,
    ADD COLUMN language_options JSONB DEFAULT '["es", "en"]'::jsonb;

-- Multimedia
ALTER TABLE tour_plans
    ADD COLUMN main_image_url VARCHAR(255),
    ADD COLUMN thumbnail_url VARCHAR(255),
    ADD COLUMN image_gallery JSONB,
    ADD COLUMN video_url VARCHAR(255);

-- SEO and marketing
ALTER TABLE tour_plans
    ADD COLUMN seo_title VARCHAR(100),
    ADD COLUMN seo_description VARCHAR(255),
    ADD COLUMN seo_keywords VARCHAR(255),
    ADD COLUMN featured BOOLEAN DEFAULT FALSE,
    ADD COLUMN featured_order INTEGER;

-- Fields for analysis and integrations
ALTER TABLE tour_plans
    ADD COLUMN average_rating DECIMAL(3, 2) DEFAULT 0,
    ADD COLUMN total_reviews INTEGER DEFAULT 0,
    ADD COLUMN total_bookings INTEGER DEFAULT 0,
    ADD COLUMN external_id VARCHAR(100);

-- Create indexes to optimize queries
CREATE INDEX idx_tour_plans_provider ON tour_plans(provider_id);
CREATE INDEX idx_tour_plans_status ON tour_plans(status);
CREATE INDEX idx_tour_plans_featured ON tour_plans(featured) WHERE featured = TRUE;
CREATE INDEX idx_tour_plans_price ON tour_plans(price);
CREATE INDEX idx_tour_plans_created_at ON tour_plans(created_at);

-- Create a table for specific data availability
CREATE TABLE tour_plan_availability (
        id SERIAL PRIMARY KEY,
        tour_plan_id INTEGER NOT NULL REFERENCES tour_plans(id) ON DELETE CASCADE,
        available_date DATE NOT NULL,
        available_spots INTEGER NOT NULL CHECK (available_spots >= 0),
        is_available BOOLEAN DEFAULT TRUE,
        price_override NUMERIC(10, 2),
        UNIQUE (tour_plan_id, available_date)
);

-- Create table to store images related to the tour plans
CREATE TABLE tour_plan_images (
        id SERIAL PRIMARY KEY,
        tour_plan_id INTEGER NOT NULL REFERENCES tour_plans(id) ON DELETE CASCADE,
        image_url VARCHAR(255) NOT NULL,
        alt_text VARCHAR(100),
        is_main BOOLEAN DEFAULT FALSE,
        display_order INTEGER DEFAULT 0
);

-- Create table for special prices
CREATE TABLE tour_plan_special_prices (
        id SERIAL PRIMARY KEY,
        tour_plan_id INTEGER NOT NULL REFERENCES tour_plans(id) ON DELETE CASCADE,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        price NUMERIC(10, 2) NOT NULL,
        description VARCHAR(100),
        CHECK (end_date >= start_date)
);

-- Improve the reservations table
ALTER TABLE reservations
        ADD COLUMN number_of_persons INTEGER DEFAULT 1,
        ADD COLUMN number_of_children INTEGER DEFAULT 0,
        ADD COLUMN special_requests TEXT,
        ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        ADD COLUMN contact_phone VARCHAR(20),
        ADD COLUMN contact_email VARCHAR(100),
        ADD COLUMN last_modified_by INTEGER REFERENCES users(id) ON DELETE SET NULL;

-- Create function to update the updated_at field automatically
CREATE OR REPLACE FUNCTION update_timestamp()
        RETURNS TRIGGER AS $$
        BEGIN
           NEW.updated_at = CURRENT_TIMESTAMP;
        RETURN NEW;
        END;
        $$ language 'plpgsql';

-- Create trigger to update the updated_at field automatically
CREATE TRIGGER update_tour_plans_timestamp
        BEFORE UPDATE ON tour_plans
        FOR EACH ROW
        EXECUTE PROCEDURE update_timestamp();

-- Create trigger to update the updated_at field automatically for reservations
CREATE TRIGGER update_reservations_timestamp
        BEFORE UPDATE ON reservations
        FOR EACH ROW
        EXECUTE PROCEDURE update_timestamp();

-- Create a view to show tours with availability
CREATE VIEW available_tours AS
        SELECT t.*,
               COUNT(DISTINCT r.id) as active_bookings,
               (t.available_spots - COALESCE(SUM(r.number_of_persons), 0)) as remaining_spots
        FROM tour_plans t
                 LEFT JOIN reservations r ON t.id = r.tour_plan_id
            AND r.reservation_status != 'cancelled'
        WHERE t.status = 'ACTIVE'
        GROUP BY t.id, t.available_spots
        HAVING (t.available_spots - COALESCE(SUM(r.number_of_persons), 0)) > 0;

-- Create table for cancellation policies
CREATE TABLE cancellation_policies (
       id SERIAL PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       description TEXT,
       refund_percentage INTEGER NOT NULL CHECK (refund_percentage >= 0 AND refund_percentage <= 100),
       days_before_tour INTEGER NOT NULL
);

-- Add a reference to the cancellation policy in the tour_plans table
ALTER TABLE tour_plans
    ADD COLUMN cancellation_policy_id INTEGER REFERENCES cancellation_policies(id) ON DELETE SET NULL;

-- Table for tour guides
CREATE TABLE guides (
        id SERIAL PRIMARY KEY,
        user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
        provider_id INTEGER REFERENCES providers(id) ON DELETE CASCADE,
        bio TEXT,
        specialties JSONB,
        languages JSONB,
        years_experience INTEGER,
        certification_details TEXT,
        is_active BOOLEAN DEFAULT TRUE
);

-- Assignment of guides to specific tours
CREATE TABLE tour_assignments (
        id SERIAL PRIMARY KEY,
        guide_id INTEGER REFERENCES guides(id) ON DELETE CASCADE,
        tour_plan_id INTEGER REFERENCES tour_plans(id) ON DELETE CASCADE,
        reservation_date DATE NOT NULL,
        status VARCHAR(20) DEFAULT 'ASSIGNED',
        notes TEXT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for notification templates
CREATE TABLE notification_templates (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        subject VARCHAR(255),
        body TEXT NOT NULL,
        type VARCHAR(50) NOT NULL, -- email, sms, push, etc.
        variables JSONB -- variables que se pueden usar en la plantilla
);

--  Table for history of sent notifications
CREATE TABLE notification_history (
        id SERIAL PRIMARY KEY,
        template_id INTEGER REFERENCES notification_templates(id) ON DELETE SET NULL,
        user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
        reservation_id INTEGER REFERENCES reservations(id) ON DELETE SET NULL,
        sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        delivery_status VARCHAR(20),
        content TEXT, -- contenido final enviado
        channel VARCHAR(20) -- email, sms, etc.
);

-- Improve the table of payments to handle refunds
ALTER TABLE payments
        ADD COLUMN refund_amount NUMERIC(10, 2) DEFAULT 0,
        ADD COLUMN refund_reason TEXT,
        ADD COLUMN refund_date TIMESTAMP,
        ADD COLUMN payment_method VARCHAR(50),
        ADD COLUMN payment_details JSONB;

-- Table for payment installments
CREATE TABLE payment_installments (
        id SERIAL PRIMARY KEY,
        reservation_id INTEGER REFERENCES reservations(id) ON DELETE CASCADE,
        amount NUMERIC(10, 2) NOT NULL,
        due_date DATE NOT NULL,
        payment_id INTEGER REFERENCES payments(id) ON DELETE SET NULL,
        status VARCHAR(20) DEFAULT 'PENDING',
        reminder_sent BOOLEAN DEFAULT FALSE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for FAQs specific to tours
CREATE TABLE tour_faqs (
        id SERIAL PRIMARY KEY,
        tour_plan_id INTEGER REFERENCES tour_plans(id) ON DELETE CASCADE,
        question TEXT NOT NULL,
        answer TEXT NOT NULL,
        display_order INTEGER DEFAULT 0,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for tracking price changes
CREATE TABLE tour_price_history (
        id SERIAL PRIMARY KEY,
        tour_plan_id INTEGER REFERENCES tour_plans(id) ON DELETE CASCADE,
        previous_price NUMERIC(10, 2) NOT NULL,
        new_price NUMERIC(10, 2) NOT NULL,
        changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        changed_by INTEGER REFERENCES users(id) ON DELETE SET NULL,
        reason TEXT
);

-- Trigger for logging price changes
CREATE OR REPLACE FUNCTION log_price_change()
        RETURNS TRIGGER AS $$
        BEGIN
            IF OLD.price IS DISTINCT FROM NEW.price THEN
                INSERT INTO tour_price_history (
                    tour_plan_id, previous_price, new_price, changed_by
                ) VALUES (
                    NEW.id, OLD.price, NEW.price, NEW.updated_by
                );
        END IF;
        RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;

-- Trigger to log price changes
CREATE TRIGGER track_tour_price_changes
            BEFORE UPDATE ON tour_plans
            FOR EACH ROW
            WHEN (OLD.price IS DISTINCT FROM NEW.price)
        EXECUTE FUNCTION log_price_change();

-- Table for logging critical actions
CREATE TABLE audit_log (
        id SERIAL PRIMARY KEY,
        entity_type VARCHAR(50) NOT NULL,
        entity_id INTEGER NOT NULL,
        action VARCHAR(20) NOT NULL, -- CREATE, UPDATE, DELETE
        user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
        action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        old_values JSONB,
        new_values JSONB,
        ip_address VARCHAR(45),
        user_agent TEXT
);

-- Expand the reviews table to include more details
ALTER TABLE reviews
        ADD COLUMN title VARCHAR(100),
        ADD COLUMN verified_purchase BOOLEAN DEFAULT FALSE,
        ADD COLUMN response_by_provider TEXT,
        ADD COLUMN response_date TIMESTAMP,
        ADD COLUMN helpful_votes INTEGER DEFAULT 0,
        ADD COLUMN reported BOOLEAN DEFAULT FALSE,
        ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE'; -- ACTIVE, PENDING, REMOVED

-- Table for specific rating categories
CREATE TABLE review_categories (
        id SERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        description TEXT
);

-- Ratings for categories in reviews
CREATE TABLE review_category_ratings (
        review_id INTEGER REFERENCES reviews(id) ON DELETE CASCADE,
        category_id INTEGER REFERENCES review_categories(id) ON DELETE CASCADE,
        rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
        PRIMARY KEY (review_id, category_id)
);

-- Table of supported languages
CREATE TABLE languages (
        code CHAR(2) PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        is_active BOOLEAN DEFAULT TRUE
);

-- Content translated for tours
CREATE TABLE tour_translations (
        tour_plan_id INTEGER REFERENCES tour_plans(id) ON DELETE CASCADE,
        language_code CHAR(2) REFERENCES languages(code) ON DELETE CASCADE,
        title VARCHAR(150),
        short_description VARCHAR(255),
        description TEXT,
        included_services JSONB,
        excluded_services JSONB,
        what_to_bring JSONB,
        meeting_point VARCHAR(255),
        PRIMARY KEY (tour_plan_id, language_code)
);

-- Table for daily aggregated statistics
CREATE TABLE daily_statistics (
        date DATE PRIMARY KEY,
        total_reservations INTEGER DEFAULT 0,
        completed_reservations INTEGER DEFAULT 0,
        cancelled_reservations INTEGER DEFAULT 0,
        total_revenue NUMERIC(12, 2) DEFAULT 0,
        new_users INTEGER DEFAULT 0,
        top_tour_id INTEGER REFERENCES tour_plans(id) ON DELETE SET NULL,
        top_region_id INTEGER REFERENCES regions(id) ON DELETE SET NULL
);

-- View for performance reports by provider
CREATE VIEW provider_performance AS
        SELECT
            p.id AS provider_id,
            p.name AS provider_name,
            COUNT(tp.id) AS total_tours,
            COUNT(r.id) AS total_reservations,
            SUM(r.total_price) AS total_revenue,
            AVG(rev.rating) AS average_rating,
            COUNT(DISTINCT r.user_id) AS unique_customers
        FROM providers p
                 LEFT JOIN tour_plans tp ON p.id = tp.provider_id
                 LEFT JOIN reservations r ON tp.id = r.tour_plan_id AND r.reservation_status = 'confirmed'
                 LEFT JOIN reviews rev ON tp.id = rev.tour_plan_id
        GROUP BY p.id, p.name;

-- Table for marketing campaigns
CREATE TABLE marketing_campaigns (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        description TEXT,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        discount_code VARCHAR(50) REFERENCES coupons(code) ON DELETE SET NULL,
        target_audience VARCHAR(50), -- ALL, NEW_USERS, RETURNING, etc.
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        created_by INTEGER REFERENCES users(id) ON DELETE SET NULL
);

-- Relations between campaigns and tours
CREATE TABLE campaign_tours (
        campaign_id INTEGER REFERENCES marketing_campaigns(id) ON DELETE CASCADE,
        tour_plan_id INTEGER REFERENCES tour_plans(id) ON DELETE CASCADE,
        featured_order INTEGER DEFAULT 0,
        special_price NUMERIC(10, 2),
        PRIMARY KEY (campaign_id, tour_plan_id)
);
CREATE SEQUENCE IF NOT EXISTS address_address_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS comarcas_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS coupons_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS discounts_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS districts_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS payments_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS permissions_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS providers_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS provinces_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS regions_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS reservations_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS reviews_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS roles_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS tour_plans_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE address
(
    address_id      INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    street          VARCHAR(255)                             NOT NULL,
    postal_code     VARCHAR(20),
    district_id     INTEGER                                  NOT NULL,
    additional_info TEXT,
    CONSTRAINT address_pkey PRIMARY KEY (address_id)
);

CREATE TABLE comarcas
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100)                             NOT NULL,
    CONSTRAINT comarcas_pkey PRIMARY KEY (id)
);

CREATE TABLE coupons
(
    id                  INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    code                VARCHAR(50)                              NOT NULL,
    discount_percentage INTEGER,
    expiration_date     date                                     NOT NULL,
    CONSTRAINT coupons_pkey PRIMARY KEY (id)
);

CREATE TABLE discounts
(
    id              INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    reservation_id  INTEGER,
    coupon_id       INTEGER,
    discount_amount numeric(10, 2)                           NOT NULL,
    CONSTRAINT discounts_pkey PRIMARY KEY (id)
);

CREATE TABLE districts
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(50)                              NOT NULL,
    province_id INTEGER,
    CONSTRAINT districts_pkey PRIMARY KEY (id)
);

CREATE TABLE flyway_schema_history
(
    installed_rank INTEGER                                   NOT NULL,
    version        VARCHAR(50),
    description    VARCHAR(200)                              NOT NULL,
    type           VARCHAR(20)                               NOT NULL,
    script         VARCHAR(1000)                             NOT NULL,
    checksum       INTEGER,
    installed_by   VARCHAR(100)                              NOT NULL,
    installed_on   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    execution_time INTEGER                                   NOT NULL,
    success        BOOLEAN                                   NOT NULL,
    CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
);

CREATE TABLE payments
(
    id             INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    reservation_id INTEGER,
    transaction_id VARCHAR(100)                             NOT NULL,
    amount         numeric(10, 2)                           NOT NULL,
    payment_status PAYMENT_STATUS              DEFAULT 'pending',
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                              NOT NULL,
    CONSTRAINT permissions_pkey PRIMARY KEY (id)
);

CREATE TABLE providers
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ruc         VARCHAR(25)                              NOT NULL,
    name        VARCHAR(100)                             NOT NULL,
    email       VARCHAR(100)                             NOT NULL,
    phone       VARCHAR(20),
    province_id INTEGER,
    district_id INTEGER,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    address_id  INTEGER,
    CONSTRAINT providers_pkey PRIMARY KEY (id)
);

CREATE TABLE provinces
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                              NOT NULL,
    CONSTRAINT provinces_pkey PRIMARY KEY (id)
);

CREATE TABLE regions
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(100)                             NOT NULL,
    province_id INTEGER                                  NOT NULL,
    comarca_id  INTEGER                                  NOT NULL,
    CONSTRAINT regions_pkey PRIMARY KEY (id)
);

CREATE TABLE reservations
(
    id                 INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id            INTEGER,
    tour_plan_id       INTEGER,
    reservation_status RESERVATION_STATUS          DEFAULT 'pending',
    reservation_date   date                                     NOT NULL,
    total_price        numeric(10, 2)                           NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT reservations_pkey PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id      INTEGER,
    tour_plan_id INTEGER,
    rating       INTEGER,
    comment      TEXT,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT reviews_pkey PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                              NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE roles_permissions
(
    role_id       INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE tour_plan_regions
(
    tour_plan_id INTEGER NOT NULL,
    region_id    INTEGER NOT NULL,
    CONSTRAINT tour_plan_regions_pkey PRIMARY KEY (tour_plan_id, region_id)
);

CREATE TABLE tour_plans
(
    id              INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    provider_id     INTEGER,
    title           VARCHAR(150)                             NOT NULL,
    description     TEXT,
    price           numeric(10, 2)                           NOT NULL,
    duration        INTEGER                                  NOT NULL,
    available_spots INTEGER                                  NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT tour_plans_pkey PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    dni               VARCHAR(13)                              NOT NULL,
    name              VARCHAR(75)                              NOT NULL,
    lastname          VARCHAR(75)                              NOT NULL,
    email             VARCHAR(150)                             NOT NULL,
    password_hash     TEXT                                     NOT NULL,
    profile_image_url VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    created_by        INTEGER,
    updated_by        INTEGER,
    deleted_at        TIMESTAMP WITHOUT TIME ZONE,
    role_id           INTEGER,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

ALTER TABLE comarcas
    ADD CONSTRAINT comarcas_name_key UNIQUE (name);

ALTER TABLE coupons
    ADD CONSTRAINT coupons_code_key UNIQUE (code);

ALTER TABLE payments
    ADD CONSTRAINT payments_transaction_id_key UNIQUE (transaction_id);

ALTER TABLE permissions
    ADD CONSTRAINT permissions_name_key UNIQUE (name);

ALTER TABLE providers
    ADD CONSTRAINT providers_email_key UNIQUE (email);

ALTER TABLE provinces
    ADD CONSTRAINT provinces_name_key UNIQUE (name);

ALTER TABLE regions
    ADD CONSTRAINT regions_name_key UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT users_email_key UNIQUE (email);

CREATE INDEX flyway_schema_history_s_idx ON flyway_schema_history (success);

CREATE INDEX idx_coupons_expiration_date ON coupons (expiration_date);

CREATE INDEX idx_reservations_reservation_date ON reservations (reservation_date);

ALTER TABLE address
    ADD CONSTRAINT address_district_id_fkey FOREIGN KEY (district_id) REFERENCES districts (id) ON DELETE CASCADE;

CREATE INDEX idx_address_district ON address (district_id);

ALTER TABLE discounts
    ADD CONSTRAINT discounts_coupon_id_fkey FOREIGN KEY (coupon_id) REFERENCES coupons (id) ON DELETE SET NULL;

ALTER TABLE discounts
    ADD CONSTRAINT discounts_reservation_id_fkey FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE;

ALTER TABLE districts
    ADD CONSTRAINT districts_province_id_fkey FOREIGN KEY (province_id) REFERENCES provinces (id) ON DELETE CASCADE;

ALTER TABLE payments
    ADD CONSTRAINT payments_reservation_id_fkey FOREIGN KEY (reservation_id) REFERENCES reservations (id) ON DELETE CASCADE;

ALTER TABLE providers
    ADD CONSTRAINT providers_address_id_fkey FOREIGN KEY (address_id) REFERENCES address (address_id) ON DELETE SET NULL;

ALTER TABLE providers
    ADD CONSTRAINT providers_district_id_fkey FOREIGN KEY (district_id) REFERENCES districts (id) ON DELETE SET NULL;

ALTER TABLE providers
    ADD CONSTRAINT providers_province_id_fkey FOREIGN KEY (province_id) REFERENCES provinces (id) ON DELETE SET NULL;

ALTER TABLE regions
    ADD CONSTRAINT regions_comarca_id_fkey FOREIGN KEY (comarca_id) REFERENCES comarcas (id) ON DELETE CASCADE;

ALTER TABLE regions
    ADD CONSTRAINT regions_province_id_fkey FOREIGN KEY (province_id) REFERENCES provinces (id) ON DELETE CASCADE;

ALTER TABLE reservations
    ADD CONSTRAINT reservations_tour_plan_id_fkey FOREIGN KEY (tour_plan_id) REFERENCES tour_plans (id) ON DELETE CASCADE;

ALTER TABLE reservations
    ADD CONSTRAINT reservations_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE INDEX idx_reservations_user_id ON reservations (user_id);

ALTER TABLE reviews
    ADD CONSTRAINT reviews_tour_plan_id_fkey FOREIGN KEY (tour_plan_id) REFERENCES tour_plans (id) ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT reviews_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE roles_permissions
    ADD CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE;

ALTER TABLE roles_permissions
    ADD CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE;

ALTER TABLE tour_plan_regions
    ADD CONSTRAINT tour_plan_regions_region_id_fkey FOREIGN KEY (region_id) REFERENCES regions (id) ON DELETE CASCADE;

ALTER TABLE tour_plan_regions
    ADD CONSTRAINT tour_plan_regions_tour_plan_id_fkey FOREIGN KEY (tour_plan_id) REFERENCES tour_plans (id) ON DELETE CASCADE;

ALTER TABLE tour_plans
    ADD CONSTRAINT tour_plans_provider_id_fkey FOREIGN KEY (provider_id) REFERENCES providers (id) ON DELETE CASCADE;

ALTER TABLE users
    ADD CONSTRAINT users_created_by_fkey FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL;

ALTER TABLE users
    ADD CONSTRAINT users_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE SET NULL;

ALTER TABLE users
    ADD CONSTRAINT users_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES users (id) ON DELETE SET NULL;