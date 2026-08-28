-- CREAZIONE TABELLA: ROOM_CATEGORIES
CREATE TABLE IF NOT EXISTS ROOM_CATEGORIES (
    id_room_category INT AUTO_INCREMENT,
    room_types VARCHAR(50) NOT NULL,
    price_for_night DOUBLE NOT NULL,
    capacity INT NOT NULL,
    accessories TEXT,
    room_amount INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id_room_category)
);

-- DATI PER TABELLA: ROOM_CATEGORIES
INSERT INTO ROOM_CATEGORIES (room_types, price_for_night, capacity, accessories, room_amount) 
VALUES 
('Single Room', '50', '1', 'Wi-Fi, TV, Air Conditioning', '5'),
('Double Room', '100', '2', 'Wi-Fi, TV, Air Conditioning', '5'),
('Triple Room', '150', '3', 'Wi-Fi, TV, Air Conditioning', '5'),
('Quadruple Room', '200', '4', 'Wi-Fi, TV, Air Conditioning', '5'),
('Single Suite', '100', '1', 'Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi', '3'),
('Double Suite', '150', '2', 'Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi', '3'),
('Triple Suite', '200', '3', 'Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi', '3'),
('Quadruple Suite', '250', '4', 'Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi', '3')