package hotel.runner;

import hotel.models.Room;
import hotel.models.RoomCategory;
import hotel.repositories.RoomCategoryRepository;
import hotel.repositories.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    // Attributi
    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomRepository roomRepository;

    // Costruttori
    public DataInitializer(RoomCategoryRepository roomCategoryRepository, RoomRepository roomRepository) {
        this.roomCategoryRepository = roomCategoryRepository;
        this.roomRepository = roomRepository;
    }

    // Metodo per Popolare la Lista delle Categorie di Stanza e delle Stanze
    @Override
    public void run(String... args) {

        // Popolamento della Lista delle Categorie di Stanza
        List<RoomCategory> roomCategories = Arrays.asList(
            new RoomCategory(1, "Single Room", 1, 50.0, "Wi-Fi, TV, Air Conditioning", 5),
            new RoomCategory(2, "Double Room", 2, 100.0, "Wi-Fi, TV, Air Conditioning", 5),
            new RoomCategory(3, "Triple Room", 3, 150.0, "Wi-Fi, TV, Air Conditioning", 5),
            new RoomCategory(4, "Quadruple Room", 4, 200.0, "Wi-Fi, TV, Air Conditioning", 5),
            new RoomCategory(5, "Single Suite", 1, 100.0, "Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi", 3),
            new RoomCategory(6, "Double Suite", 2, 150.0, "Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi", 3),
            new RoomCategory(7, "Triple Suite", 3, 200.0, "Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi", 3),
            new RoomCategory(8, "Quadruple Suite", 4, 250.0, "Wi-Fi, TV, Air Conditioning, Minibar, Jacuzzi", 3)
        );

        // Salvataggio delle Categorie Di Stanza
        roomCategoryRepository.saveAll(roomCategories);

        // Popolamento della Lista delle Stanze
        List<Room> rooms = Arrays.asList(

            // Single Room
            new Room(101, roomCategories.get(0), "Dirty"),
            new Room(102, roomCategories.get(0), "Dirty"),
            new Room(103, roomCategories.get(0), "Dirty"),
            new Room(104, roomCategories.get(0), "Dirty"),
            new Room(105, roomCategories.get(0), "Dirty"),

            // Double Room
            new Room(201, roomCategories.get(1), "Dirty"),
            new Room(202, roomCategories.get(1), "Dirty"),
            new Room(203, roomCategories.get(1), "Dirty"),
            new Room(204, roomCategories.get(1), "Dirty"),
            new Room(205, roomCategories.get(1), "Dirty"),

            // Triple Room
            new Room(301, roomCategories.get(2), "Dirty"),
            new Room(302, roomCategories.get(2), "Dirty"),
            new Room(303, roomCategories.get(2), "Dirty"),
            new Room(304, roomCategories.get(2), "Dirty"),
            new Room(305, roomCategories.get(2), "Dirty"),

            // Quadruple Room
            new Room(401, roomCategories.get(3), "Dirty"),
            new Room(402, roomCategories.get(3), "Dirty"),
            new Room(403, roomCategories.get(3), "Dirty"),
            new Room(404, roomCategories.get(3), "Dirty"),
            new Room(405, roomCategories.get(3), "Dirty"),

            // Single Suite
            new Room(501, roomCategories.get(4), "Dirty"),
            new Room(502, roomCategories.get(4), "Dirty"),
            new Room(503, roomCategories.get(4), "Dirty"),

            // Double Suite
            new Room(601, roomCategories.get(5), "Dirty"),
            new Room(602, roomCategories.get(5), "Dirty"),
            new Room(603, roomCategories.get(5), "Dirty"),

            // Triple Suite
            new Room(701, roomCategories.get(6), "Dirty"),
            new Room(702, roomCategories.get(6), "Dirty"),
            new Room(703, roomCategories.get(6), "Dirty"),

            // Quadruple Suite
            new Room(801, roomCategories.get(7), "Dirty"),
            new Room(802, roomCategories.get(7), "Dirty"),
            new Room(803, roomCategories.get(7), "Dirty")
        );

        // Salvataggio delle Stanze
        roomRepository.saveAll(rooms);

        System.out.println("Room Category and Room Data and Successfully Entered!");
    }
    
}
