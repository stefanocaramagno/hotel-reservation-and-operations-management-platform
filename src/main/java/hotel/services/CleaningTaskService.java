package hotel.services;

import hotel.models.CleaningTask;
import hotel.models.OperationsManager;
import hotel.models.Room;
import hotel.models.CleaningStaff;
import hotel.repositories.CleaningTaskRepository;
import hotel.repositories.RoomRepository;
import hotel.repositories.CleaningStaffRepository;
import hotel.repositories.OperationsManagerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CleaningTaskService {

    // Attributi
    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private OperationsManagerRepository operationsManagerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CleaningStaffRepository cleaningStaffRepository;

    // **CASO D'USO N°11 (UC11): ASSEGNA ATTIVITÀ DEL PERSONALE **

    // Metodo per Visualizzare la Lista delle Stanze Sporche
    public List<Room> getDirtyRooms() {
        return roomRepository.findDirtyRooms();
    }

    // Metodo per Visualizzare la Lista dei Cleaning Staff
    public List<CleaningStaff> getAllCleaningStaff() {
        return cleaningStaffRepository.findAll();
    }
    
    // Metodo per Assegnare un Task di Pulizia
    public void assignCleaningTask(String operationsManagerEmail, int idCleaningStaff, int idRoom) {
        OperationsManager operationsManager = operationsManagerRepository.findByEmail(operationsManagerEmail);
        if (operationsManager == null) {
            throw new RuntimeException("Operations Manager not found with email: " + operationsManagerEmail);
        }

        Room room = roomRepository.findById(idRoom)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));
        
        CleaningStaff cleaningStaff = cleaningStaffRepository.findById(idCleaningStaff)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found."));
    
        if (!"Dirty".equals(room.getStatus())) {
            throw new IllegalStateException("Room is not dirty.");
        }
    
        CleaningTask cleaningTask = new CleaningTask();
        cleaningTask.setOperationsManager(operationsManager);
        cleaningTask.setRoom(room);
        cleaningTask.setCleaningStaff(cleaningStaff);
        cleaningTaskRepository.save(cleaningTask);
    
        room.setStatus("Pending Clean");
        roomRepository.save(room);
    }    

    // **CASO D'USO N°12 (UC12): GESTISCI STATO DELLE CAMERE **

    // Metodo per Visualizzare la Lista dei Task di Pulizia
    public List<Room> getRoomsPendingClean(String cleaningStaffEmail) {
        CleaningStaff cleaningStaff = cleaningStaffRepository.findByEmail(cleaningStaffEmail);
        if (cleaningStaff == null) {
            throw new RuntimeException("Staff not found with email: " + cleaningStaffEmail);
        }

        int idCleaningStaff = cleaningStaff.getIdCleaningStaff();

        List<CleaningTask> assignedTasks = cleaningTaskRepository.findCleaningTasks(idCleaningStaff);
    
        List<Room> rooms = new ArrayList<>();
        for (CleaningTask task : assignedTasks) {
            rooms.add(task.getRoom());
        }

        return rooms;
    }
    
    // Metodo per Gestire l'assegnazione di un Task di Pulizia
    public void manageCleaningTask(int idRoom, String newStatus) {
        Room room = roomRepository.findById(idRoom)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));
    
        if (!newStatus.equalsIgnoreCase("Clean") && !newStatus.equalsIgnoreCase("Dirty")) {
            throw new IllegalArgumentException("Invalid room status. Valid statuses are 'Clean' or 'Dirty'.");
        }
    
        room.setStatus(newStatus);
        roomRepository.save(room);
    }
    
}
