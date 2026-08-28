package hotel.services;

import hotel.models.RoomCategory;
import hotel.repositories.PaidBookingRepository;
import hotel.repositories.RequestBookingRepository;
import hotel.repositories.RoomCategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class RoomCategoryServiceTest {

    // Attributi
    @Mock
    private RequestBookingRepository requestBookingRepository;
    
    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @Mock
    private PaidBookingRepository paidBookingRepository;

    @InjectMocks
    private RoomCategoryService roomCategoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **CASO D'USO N°1 (UC1): EFFETTUA RICHIESTA DI PRENOTAZIONE**

    // Test del Metodo per Calcolare il Numero di Stanze Disponibili da RequestBooking
    @Test
    public void testGetAvailableRoomsFromRequestBooking() {
        RoomCategory mockRoomCategory = new RoomCategory();
        mockRoomCategory.setIdRoomCategory(1);
        mockRoomCategory.setRoomAmount(10);

        when(roomCategoryRepository.findByRoomType("Single Room")).thenReturn(mockRoomCategory);
        when(requestBookingRepository.countRequestsBookedRooms(1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"))).thenReturn(3L);

        int availableRooms = roomCategoryService.getAvailableRoomsFromRequestBooking("Single Room", "2024-01-01", "2024-01-10");

        assertEquals(7, availableRooms);
        verify(roomCategoryRepository, times(1)).findByRoomType("Single Room");
        verify(requestBookingRepository, times(1)).countRequestsBookedRooms(1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"));
    }

    // Test del Metodo per Verificare la Disponibilità da RequestBooking
    @Test
    public void testIsRoomAvailableFromRequestBooking() {
        RoomCategory mockRoomCategory = new RoomCategory();
        mockRoomCategory.setIdRoomCategory(1);
        mockRoomCategory.setRoomAmount(10);

        when(roomCategoryRepository.findByRoomType("Single Room")).thenReturn(mockRoomCategory);
        when(requestBookingRepository.countRequestsBookedRooms(1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"))).thenReturn(9L);

        boolean isAvailable = roomCategoryService.isRoomAvailableFromRequestBooking("Single Room", "2024-01-01", "2024-01-10");

        assertTrue(isAvailable);
        verify(roomCategoryRepository, times(1)).findByRoomType("Single Room");
        verify(requestBookingRepository, times(1)).countRequestsBookedRooms(1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"));
    }

    // **CASO D'USO N°5 (UC5): RICHIEDI MODIFICA DELLA PRENOTAZIONE**

    // Test del Metodo per Calcolare il Numero di Stanze Disponibili da PaidBooking
    @Test
    public void testGetAvailableRoomsFromPaidBooking() {
        RoomCategory mockRoomCategory = new RoomCategory();
        mockRoomCategory.setIdRoomCategory(2);
        mockRoomCategory.setRoomAmount(15);

        when(roomCategoryRepository.findByRoomType("Double Room")).thenReturn(mockRoomCategory);
        when(paidBookingRepository.countPaidBookedRooms(2, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"))).thenReturn(5L);

        int availableRooms = roomCategoryService.getAvailableRoomsFromPaidBooking("Double Room", "2024-01-01", "2024-01-10");

        assertEquals(10, availableRooms);
        verify(roomCategoryRepository, times(1)).findByRoomType("Double Room");
        verify(paidBookingRepository, times(1)).countPaidBookedRooms(2, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"));
    }

    // Test del Metodo per Verificare la Disponibilità da PaidBooking
    @Test
    public void testIsRoomAvailableFromPaidBooking() {
        RoomCategory mockRoomCategory = new RoomCategory();
        mockRoomCategory.setIdRoomCategory(2);
        mockRoomCategory.setRoomAmount(15);

        when(roomCategoryRepository.findByRoomType("Double Room")).thenReturn(mockRoomCategory);
        when(paidBookingRepository.countPaidBookedRooms(2, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"))).thenReturn(15L);

        boolean isAvailable = roomCategoryService.isRoomAvailableFromPaidBooking("Double Room", "2024-01-01", "2024-01-10");

        assertFalse(isAvailable);
        verify(roomCategoryRepository, times(1)).findByRoomType("Double Room");
        verify(paidBookingRepository, times(1)).countPaidBookedRooms(2, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-10"));
    }
    
}
