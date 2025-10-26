-- Flyway V1: Initial schema for Bus Scheduling & Management System
-- SQL dialect: MySQL 8.x
-- Conventions:
--  - snake_case table & column names
--  - bigint primary keys (auto-increment)
--  - created_at defaults
--  - strict FKs with ON DELETE RESTRICT unless noted
--  - ENUM-like via CHECK (validated domain values)

-- Safety
SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------
-- USERS / ROLES / PERMS
-- -------------------------
CREATE TABLE roles (
                       id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name          VARCHAR(64) NOT NULL UNIQUE,
                       description   VARCHAR(255),
                       created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE permissions (
                             id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name          VARCHAR(96) NOT NULL UNIQUE,
                             description   VARCHAR(255),
                             created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE users (
                       id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                       email         VARCHAR(160) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       full_name     VARCHAR(120) NOT NULL,
                       phone         VARCHAR(32),
                       status        VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
                       created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT chk_user_status CHECK (status IN ('ACTIVE','INACTIVE','LOCKED'))
) ENGINE=InnoDB;

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE role_permissions (
                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_role_permissions_perm FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- -------------------------
-- ROUTES / STOPS
-- -------------------------
CREATE TABLE routes (
                        id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name          VARCHAR(120) NOT NULL UNIQUE,
                        origin        VARCHAR(120) NOT NULL,
                        destination   VARCHAR(120) NOT NULL,
                        distance_km   DECIMAL(7,2) NOT NULL DEFAULT 0.00,
                        duration_min  INT NOT NULL CHECK (duration_min >= 0),
                        status        VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
                        created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT chk_route_status CHECK (status IN ('ACTIVE','INACTIVE'))
) ENGINE=InnoDB;

CREATE TABLE stops (
                       id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name          VARCHAR(160) NOT NULL,
                       lat           DECIMAL(9,6) NOT NULL,
                       lng           DECIMAL(9,6) NOT NULL,
                       created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE route_stops (
                             route_id            BIGINT NOT NULL,
                             stop_id             BIGINT NOT NULL,
                             stop_order          INT NOT NULL,
                             arrival_offset_min  INT NOT NULL DEFAULT 0,
                             PRIMARY KEY (route_id, stop_id),
                             CONSTRAINT uq_route_stop_order UNIQUE (route_id, stop_order),
                             CONSTRAINT fk_route_stops_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
                             CONSTRAINT fk_route_stops_stop FOREIGN KEY (stop_id) REFERENCES stops(id) ON DELETE RESTRICT,
                             CONSTRAINT chk_arrival_offset CHECK (arrival_offset_min >= 0)
) ENGINE=InnoDB;

-- -------------------------
-- BUSES / DRIVERS / MAINT
-- -------------------------
CREATE TABLE buses (
                       id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                       plate_number  VARCHAR(32) NOT NULL UNIQUE,
                       capacity      INT NOT NULL CHECK (capacity > 0),
                       type          VARCHAR(40) NOT NULL, -- e.g., AC, NON_AC, LUXURY
                       status        VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
                       created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT chk_bus_status CHECK (status IN ('ACTIVE','INACTIVE','MAINTENANCE'))
) ENGINE=InnoDB;

CREATE TABLE drivers (
                         id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                         first_name    VARCHAR(80) NOT NULL,
                         last_name     VARCHAR(80) NOT NULL,
                         license_no    VARCHAR(64) NOT NULL UNIQUE,
                         phone         VARCHAR(32),
                         status        VARCHAR(24) NOT NULL DEFAULT 'ACTIVE',
                         created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT chk_driver_status CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED'))
) ENGINE=InnoDB;

CREATE TABLE bus_maintenance (
                                 id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 bus_id        BIGINT NOT NULL,
                                 start_time    DATETIME NOT NULL,
                                 end_time      DATETIME,
                                 reason        VARCHAR(255),
                                 status        VARCHAR(24) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, IN_PROGRESS, DONE
                                 created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_maint_bus FOREIGN KEY (bus_id) REFERENCES buses(id) ON DELETE CASCADE,
                                 CONSTRAINT chk_maint_status CHECK (status IN ('SCHEDULED','IN_PROGRESS','DONE')),
                                 CONSTRAINT chk_maint_times CHECK (end_time IS NULL OR end_time >= start_time)
) ENGINE=InnoDB;

-- -------------------------
-- TRIPS / ASSIGNMENTS
-- -------------------------
CREATE TABLE trips (
                       id              BIGINT PRIMARY KEY AUTO_INCREMENT,
                       route_id        BIGINT NOT NULL,
                       departure_time  DATETIME NOT NULL,
                       arrival_time    DATETIME NOT NULL,
                       status          VARCHAR(24) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, CANCELLED, COMPLETED
                       bus_id          BIGINT NULL,
                       driver_id       BIGINT NULL,
                       reserved_seats  INT NOT NULL DEFAULT 0,
                       created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_trip_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE RESTRICT,
                       CONSTRAINT fk_trip_bus FOREIGN KEY (bus_id) REFERENCES buses(id) ON DELETE SET NULL,
                       CONSTRAINT fk_trip_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE SET NULL,
                       CONSTRAINT chk_trip_times CHECK (arrival_time > departure_time),
                       CONSTRAINT chk_trip_status CHECK (status IN ('SCHEDULED','CANCELLED','COMPLETED')),
                       CONSTRAINT chk_trip_reserved CHECK (reserved_seats >= 0)
) ENGINE=InnoDB;

CREATE TABLE trip_assignments (
                                  id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  trip_id              BIGINT NOT NULL,
                                  bus_id               BIGINT NOT NULL,
                                  driver_id            BIGINT NOT NULL,
                                  assigned_by_user_id  BIGINT NOT NULL,
                                  assigned_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_ta_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_ta_bus FOREIGN KEY (bus_id) REFERENCES buses(id) ON DELETE RESTRICT,
                                  CONSTRAINT fk_ta_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE RESTRICT,
                                  CONSTRAINT fk_ta_user FOREIGN KEY (assigned_by_user_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- -------------------------
-- PASSENGERS / BOOKINGS
-- -------------------------
CREATE TABLE passengers (
                            id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                            full_name     VARCHAR(120) NOT NULL,
                            email         VARCHAR(160) UNIQUE,
                            phone         VARCHAR(32) NOT NULL,
                            created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE bookings (
                          id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                          trip_id       BIGINT NOT NULL,
                          passenger_id  BIGINT NOT NULL,
                          booking_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          status        VARCHAR(24) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED, CANCELLED
                          seats_count   INT NOT NULL CHECK (seats_count > 0),
                          total_amount  DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
                          created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_booking_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE RESTRICT,
                          CONSTRAINT fk_booking_pass FOREIGN KEY (passenger_id) REFERENCES passengers(id) ON DELETE RESTRICT,
                          CONSTRAINT chk_booking_status CHECK (status IN ('CONFIRMED','CANCELLED'))
) ENGINE=InnoDB;

CREATE TABLE booking_seats (
                               id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                               booking_id    BIGINT NOT NULL,
                               trip_id       BIGINT NOT NULL,
                               seat_number   INT NOT NULL,
                               CONSTRAINT fk_bs_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
                               CONSTRAINT fk_bs_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE RESTRICT,
                               CONSTRAINT uq_trip_seat UNIQUE (trip_id, seat_number),
                               CONSTRAINT chk_seat_number CHECK (seat_number > 0)
) ENGINE=InnoDB;

-- -------------------------
-- PAYMENTS / TICKETS / REFUNDS
-- -------------------------
CREATE TABLE payments (
                          id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                          booking_id    BIGINT NOT NULL,
                          amount        DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
                          method        VARCHAR(24) NOT NULL, -- CARD, CASH, ONLINE
                          status        VARCHAR(24) NOT NULL DEFAULT 'PAID', -- PAID, FAILED, REFUNDED, PARTIAL
                          paid_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          txn_ref       VARCHAR(64) UNIQUE NULL,
                          CONSTRAINT fk_pay_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
                          CONSTRAINT chk_payment_status CHECK (status IN ('PAID','FAILED','REFUNDED','PARTIAL'))
) ENGINE=InnoDB;

CREATE TABLE tickets (
                         id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                         booking_id    BIGINT NOT NULL,
                         ticket_code   VARCHAR(32) NOT NULL UNIQUE,
                         issued_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         status        VARCHAR(24) NOT NULL DEFAULT 'ISSUED', -- ISSUED, VOID
                         created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_ticket_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
                         CONSTRAINT chk_ticket_status CHECK (status IN ('ISSUED','VOID'))
) ENGINE=InnoDB;

CREATE TABLE refunds (
                         id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                         booking_id    BIGINT NOT NULL,
                         payment_id    BIGINT NOT NULL,
                         amount        DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
                         reason        VARCHAR(255) NOT NULL,
                         status        VARCHAR(24) NOT NULL DEFAULT 'REQUESTED', -- REQUESTED, APPROVED, DECLINED, PAID
                         processed_at  DATETIME NULL,
                         CONSTRAINT fk_ref_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE RESTRICT,
                         CONSTRAINT fk_ref_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE RESTRICT,
                         CONSTRAINT chk_refund_status CHECK (status IN ('REQUESTED','APPROVED','DECLINED','PAID'))
) ENGINE=InnoDB;

CREATE TABLE ticket_verifications (
                                      id                    BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      ticket_id             BIGINT NOT NULL,
                                      verified_by_user_id   BIGINT NOT NULL,
                                      verified_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      location              VARCHAR(160),
                                      CONSTRAINT fk_tv_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_tv_user FOREIGN KEY (verified_by_user_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- -------------------------
-- DRIVER ISSUES / NOTIFICATIONS
-- -------------------------
CREATE TABLE driver_issue_reports (
                                      id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      driver_id     BIGINT NOT NULL,
                                      trip_id       BIGINT NOT NULL,
                                      issue_type    VARCHAR(64) NOT NULL, -- MECHANICAL, DELAY, INCIDENT
                                      description   TEXT,
                                      reported_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      status        VARCHAR(24) NOT NULL DEFAULT 'OPEN', -- OPEN, ACK, RESOLVED
                                      CONSTRAINT fk_issue_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE RESTRICT,
                                      CONSTRAINT fk_issue_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE RESTRICT,
                                      CONSTRAINT chk_issue_status CHECK (status IN ('OPEN','ACK','RESOLVED'))
) ENGINE=InnoDB;

CREATE TABLE notifications (
                               id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
                               type                 VARCHAR(24) NOT NULL, -- EMAIL, SMS, INTERNAL
                               to_contact           VARCHAR(160) NOT NULL,
                               subject              VARCHAR(160),
                               body                 TEXT NOT NULL,
                               related_booking_id   BIGINT NULL,
                               sent_at              DATETIME NULL,
                               status               VARCHAR(24) NOT NULL DEFAULT 'PENDING', -- PENDING, SENT, FAILED
                               CONSTRAINT fk_notif_booking FOREIGN KEY (related_booking_id) REFERENCES bookings(id) ON DELETE SET NULL,
                               CONSTRAINT chk_notif_status CHECK (status IN ('PENDING','SENT','FAILED'))
) ENGINE=InnoDB;

-- Helpful index for name searches (UX requirement)
CREATE INDEX idx_routes_name ON routes(name);
CREATE INDEX idx_stops_name ON stops(name);
CREATE INDEX idx_passengers_name ON passengers(full_name);
CREATE INDEX idx_drivers_name ON drivers(last_name, first_name);
CREATE INDEX idx_users_name ON users(full_name);

-- Booking stability: speed up joins
CREATE INDEX idx_booking_trip ON bookings(trip_id);
CREATE INDEX idx_booking_passenger ON bookings(passenger_id);
CREATE INDEX idx_booking_status ON bookings(status);

-- Seat uniqueness already enforced by uq_trip_seat

SET FOREIGN_KEY_CHECKS = 1;
