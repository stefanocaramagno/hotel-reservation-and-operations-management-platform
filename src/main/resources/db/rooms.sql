-- CREAZIONE TABELLA: ROOMS
CREATE TABLE IF NOT EXISTS ROOMS (
    id_room INT AUTO_INCREMENT,
    id_room_category INT NOT NULL,
    status ENUM('Clean', 'Dirty') DEFAULT 'Dirty',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_room_category) REFERENCES ROOM_CATEGORIES(id_room_category),
    PRIMARY KEY(id_room)
);

-- DATI PER TABELLA: ROOMS
INSERT INTO ROOMS (id_room, id_room_category, status) 
VALUES 
(101, 1, 'Dirty'),
(102, 1, 'Dirty'),
(103, 1, 'Dirty'),
(104, 1, 'Dirty'),
(105, 1, 'Dirty'),

(201, 2, 'Dirty'),
(202, 2, 'Dirty'),
(203, 2, 'Dirty'),
(204, 2, 'Dirty'),
(205, 2, 'Dirty'),

(301, 3, 'Dirty'),
(302, 3, 'Dirty'),
(303, 3, 'Dirty'),
(304, 3, 'Dirty'),
(305, 3, 'Dirty'),

(401, 4, 'Dirty'),
(402, 4, 'Dirty'),
(403, 4, 'Dirty'),
(404, 4, 'Dirty'),
(405, 4, 'Dirty'),

(501, 5, 'Dirty'),
(502, 5, 'Dirty'),
(503, 5, 'Dirty'),

(601, 6, 'Dirty'),
(602, 6, 'Dirty'),
(603, 6, 'Dirty'),

(701, 7, 'Dirty'),
(702, 7, 'Dirty'),
(703, 7, 'Dirty'),

(801, 8, 'Dirty'),
(802, 8, 'Dirty'),
(803, 8, 'Dirty');
