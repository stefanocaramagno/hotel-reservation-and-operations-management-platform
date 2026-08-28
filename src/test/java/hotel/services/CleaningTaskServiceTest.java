package hotel.services;

import hotel.models.CleaningStaff;
import hotel.models.CleaningTask;
import hotel.models.OperationsManager;
import hotel.models.Room;
import hotel.repositories.CleaningStaffRepository;
import hotel.repositories.OperationsManagerRepository;
import hotel.repositories.CleaningTaskRepository;
import hotel.repositories.RoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CleaningTaskServiceTest {

    // Attributi
    @Mock
    private CleaningStaffRepository cleaningStaffRepository;

    @Mock
    private OperationsManagerRepository operationsManagerRepository;
    
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CleaningTaskRepository cleaningTaskRepository;

    @InjectMocks
    private CleaningTaskService cleaningTaskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **CASO D'USO N°11 (UC11): ASSEGNA ATTIVITÀ DEL PERSONALE**

    // Test del Metodo per Visualizzare la Lista delle Stanze Sporche
    @Test
    public void testGetDirtyRooms() {
        Room room1 = new Room();
        room1.setIdRoom(1);
        room1.setStatus("Dirty");

        Room room2 = new Room();
        room2.setIdRoom(2);
        room2.setStatus("Dirty");

        when(roomRepository.findDirtyRooms()).thenReturn(Arrays.asList(room1, room2));

        List<Room> dirtyRooms = cleaningTaskService.getDirtyRooms();

        assertNotNull(dirtyRooms);
        assertEquals(2, dirtyRooms.size());
        verify(roomRepository, times(1)).findDirtyRooms();
    }

    // Test del Metodo per Visualizzare la Lista delle dei Cleaning Staff
    @Test
    public void testGetCleaningStaff() {
        CleaningStaff cleaningStaff1 = new CleaningStaff();
        cleaningStaff1.setIdCleaningStaff(1);
        cleaningStaff1.setFullName("John Doe");
    
        CleaningStaff cleaningStaff2 = new CleaningStaff();
        cleaningStaff2.setIdCleaningStaff(2);
        cleaningStaff2.setFullName("Jane Smith");
    
        when(cleaningStaffRepository.findAll()).thenReturn(Arrays.asList(cleaningStaff1, cleaningStaff2));
    
        List<CleaningStaff> cleaningStaffList = cleaningTaskService.getAllCleaningStaff();
    
        assertNotNull(cleaningStaffList);
        assertEquals(2, cleaningStaffList.size()); 
        assertEquals("John Doe", cleaningStaffList.get(0).getFullName());
        assertEquals("Jane Smith", cleaningStaffList.get(1).getFullName());
        verify(cleaningStaffRepository, times(1)).findAll(); 
    }

    // Test del Metodo per Assegnare un Task di Pulizia
    @Test
    void testAssignCleaningTask() {
        String email = "test@example.com";
        int roomId = 1;
        int cleaningStaffId = 1;

        OperationsManager operationsManager = new OperationsManager();
        operationsManager.setEmail(email);

        Room room = new Room();
        room.setIdRoom(roomId);
        room.setStatus("Dirty");

        CleaningStaff cleaningStaff = new CleaningStaff();
        cleaningStaff.setIdCleaningStaff(cleaningStaffId);

        when(operationsManagerRepository.findByEmail(email)).thenReturn(operationsManager);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(cleaningStaffRepository.findById(cleaningStaffId)).thenReturn(Optional.of(cleaningStaff));

        assertDoesNotThrow(() -> cleaningTaskService.assignCleaningTask(email, cleaningStaffId, roomId));

        assertEquals("Pending Clean", room.getStatus());

        verify(cleaningTaskRepository, times(1)).save(any(CleaningTask.class));

        verify(roomRepository, times(1)).save(room);
    }
    
    // **CASO D'USO N°12 (UC12): GESTISCI STATO DELLE CAMERE **

    // Test per Visualizzare la Lista dei Task di Pulizia
    @Test
    public void testGetRoomsPendingClean() {
        CleaningStaff cleaningStaff = new CleaningStaff();
        cleaningStaff.setIdCleaningStaff(1);
        cleaningStaff.setEmail("staff@example.com");

        CleaningTask task1 = new CleaningTask();
        Room room1 = new Room();
        room1.setIdRoom(1);
        task1.setRoom(room1);

        CleaningTask task2 = new CleaningTask();
        Room room2 = new Room();
        room2.setIdRoom(2);
        task2.setRoom(room2);

        when(cleaningStaffRepository.findByEmail("staff@example.com")).thenReturn(cleaningStaff);
        when(cleaningTaskRepository.findCleaningTasks(1)).thenReturn(Arrays.asList(task1, task2));

        List<Room> rooms = cleaningTaskService.getRoomsPendingClean("staff@example.com");

        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        verify(cleaningStaffRepository, times(1)).findByEmail("staff@example.com");
        verify(cleaningTaskRepository, times(1)).findCleaningTasks(1);
    }

    // Test per Gestire l'assegnazione di un Task di Pulizia
    @Test
    public void testManageCleaningTask() {
        Room room = new Room();
        room.setIdRoom(1);
        room.setStatus("Pending Clean");
    
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
    
        cleaningTaskService.manageCleaningTask(1, "Clean");
    
        assertEquals("Clean", room.getStatus());
        verify(roomRepository, times(1)).save(room);
    }

}
