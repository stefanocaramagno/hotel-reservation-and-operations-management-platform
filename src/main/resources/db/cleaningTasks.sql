CREATE TABLE IF NOT EXISTS CLEANING_TASKS (
    id_task INT AUTO_INCREMENT,
    id_room INT NOT NULL,
    id_cleaning_staff INT NOT NULL,
    id_operations_manager INT NOT NULL,
    status ENUM('Assigned', 'Completed') DEFAULT 'Assigned',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_room) REFERENCES ROOMS(id_room),
    FOREIGN KEY (id_cleaning_staff) REFERENCES CLEANING_STAFF(id_cleaning_staff),
    PRIMARY KEY (id_task)
);
