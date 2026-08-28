-- Active: 1699567958875@@127.0.0.1@3306
-- CREAZIONE TABELLA: PAID_BOOKINGS
CREATE TABLE IF NOT EXISTS PAID_BOOKINGS (
    id_booking INT AUTO_INCREMENT,
    id_room_category INT NOT NULL,
    id_customer INT NOT NULL,
    id_receptionist INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    old_start_date DATE NULL,
    old_end_date DATE NULL,
    status VARCHAR(50) NOT NULL,
    total_price DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_room_category) REFERENCES ROOM_CATEGORIES(id_room_category),
    FOREIGN KEY (id_customer) REFERENCES CUSTOMERS(id_customer),
    PRIMARY KEY(id_booking)
);
