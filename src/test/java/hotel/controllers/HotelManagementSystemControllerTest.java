package hotel.controllers;

import hotel.services.RoomCategoryService;
import hotel.services.BookingService;
import hotel.services.CustomerService;
import hotel.services.ReceptionistService;
import hotel.services.OperationsManagerService;
import hotel.services.CleaningTaskService;
import hotel.services.CleaningStaffService;
import hotel.models.Customer;
import hotel.models.Receptionist;
import hotel.models.RequestBooking;
import hotel.models.Room;
import hotel.models.OperationsManager;
import hotel.models.PaidBooking;
import hotel.models.CleaningStaff;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

class HotelManagementSystemControllerTest {

    // Attributi
    @Mock
    private CustomerService customerService;

    @Mock
    private ReceptionistService receptionistService;

    @Mock
    private OperationsManagerService operationsManagerService;

    @Mock
    private CleaningStaffService cleaningStaffService;

    @Mock
    private BookingService bookingService;

    @Mock
    private RoomCategoryService roomCategoryService;

    @Mock
    private CleaningTaskService cleaningTaskService;

    @InjectMocks
    private HotelManagementSystemController hotelManagementSystemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **REGISTRAZIONI UTENTI DEL SISTEMA**

    // Test per Endpoint per la Registrazione del Customer
    @Test
    void testRegisterCustomer() {
        Customer customer = new Customer();

        doNothing().when(customerService).registerCustomer(customer);

        ResponseEntity<String> response = hotelManagementSystemController.registerCustomer(customer);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Customer registered successfully!", response.getBody());
    }

    // Test per Endpoint per la Registrazione del Receptionist
    @Test
    void testRegisterReceptionist() {
        Receptionist receptionist = new Receptionist();

        doNothing().when(receptionistService).registerReceptionist(any(Receptionist.class));

        ResponseEntity<String> response = hotelManagementSystemController.registerReceptionist(receptionist);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Receptionist registered successfully!", response.getBody());
    }

    // Test per Endpoint per la Registrazione del Operations Manager
    @Test
    void testRegisterOperationsManager() {
        OperationsManager operationsManager = new OperationsManager();

        doNothing().when(operationsManagerService).registerOperationsManager(operationsManager);

        ResponseEntity<String> response = hotelManagementSystemController.registerOperationsManager(operationsManager);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Operations Manager registered successfully!", response.getBody());
    }

    // Test per Endpoint per la Registrazione del Cleaning Staff
    @Test
    void testRegisterCleaningStaff() {
        CleaningStaff cleaningStaff = new CleaningStaff();

        doNothing().when(cleaningStaffService).registerCleaningStaff(cleaningStaff);

        ResponseEntity<String> response = hotelManagementSystemController.registerCleaningStaff(cleaningStaff);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Cleaning Staff registered successfully!", response.getBody());
    }

    // **CASO D'USO N°1 (UC1): EFFETTUA RICHIESTA DI PRENOTAZIONE**

    // Test per Endpoint per Calcolare il Numero di Stanza Disponibili
    @Test
    void testCheckRoomAvailabilityForRequestBooking() {
        when(roomCategoryService.getAvailableRoomsFromRequestBooking(anyString(), anyString(), anyString())).thenReturn(5);

        ResponseEntity<?> response = hotelManagementSystemController.checkRoomAvailabilityforRequestBooking("Single", "2025-01-01", "2025-01-10");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Map.of("available", 5), response.getBody());
    }

    // Test per Endpoint per Creare una Richiesta di Prenotazione
    @Test
    void testRequestBooking() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
    
        doNothing().when(bookingService).requestBooking("test@example.com", "Single", "2025-01-01", "2025-01-10");
    
        ResponseEntity<?> response = hotelManagementSystemController.requestBooking("Single", "2025-01-01", "2025-01-10");
    
        assertEquals(200, response.getStatusCode().value(), "Expected status 200 but got " + response.getStatusCode().value());
        assertEquals("Booking request submitted successfully!", response.getBody());
    }    

    // **CASO D'USO N°2 (UC2): GESTISCI RICHIESTA DI PRENOTAZIONE**

    // Test per Endpoint per Visualizzare la Lista delle Richieste di Prenotazione 
    @Test
    void testGetRequestsBooking() {
        when(bookingService.getRequestsBooking()).thenReturn(List.of(new RequestBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getRequestsBooking();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    // Test per Endpoint per Gestire una Richiesta di Prenotazione
    @Test
    void testManageRequestBooking() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
    
        doNothing().when(bookingService).manageRequestBooking("test@example.com", "Approved", 1);
    
        ResponseEntity<?> response = hotelManagementSystemController.manageRequestBooking("Approved", 1);
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Status updated successfully for booking ID: 1", response.getBody());
    }

    // **CASO D'USO N°3 (UC3): EFFETTUA PAGAMENTO**

    // Test per Endpoint per Visualizzare la Lista delle Richieste di Prenotazione valide per il Pagamento
    @Test
    void testGetPayments() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(bookingService.getPayments("test@example.com")).thenReturn(List.of(new RequestBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getPayments();

        assertEquals(200, response.getStatusCode().value(), "Expected status 200 but got " + response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }


    // Test per Endpoint per Effettuare il Pagamento di una Richiesta di Prenotazione
    @Test
    void testSetPayment() {
        doNothing().when(bookingService).setPayment(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt());
    
        ResponseEntity<String> response = hotelManagementSystemController.setPayment("Credit Card", "1234567890123456", "12/2025", 123, "John Doe", 1);
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Payment successfully processed for booking ID: 1", response.getBody());
    
        verify(bookingService, times(1)).setPayment(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt());
    }

    // **CASO D'USO N°4 (UC4): VISUALIZZA DETTAGLI DELLA PRENOTAZIONE**

    // Test per Endpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
    @Test
    void testGetPaidBookingsForCustomer() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("customer@example.com");
        SecurityContextHolder.setContext(securityContext);
    
        when(bookingService.getPaidBookingsForCustomer("customer@example.com")).thenReturn(List.of(new PaidBooking()));
    
        ResponseEntity<?> response = hotelManagementSystemController.getPaidBookingsForCustomer();
    
        assertEquals(200, response.getStatusCode().value(), "Expected status 200 but got " + response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }
    
    // **CASO D'USO N°5 (UC5): RICHIEDI MODIFICA DELLA PRENOTAZIONE**

    // Test per Endpoint per Calcolare il Numero di Stanze Disponibili per una Modifica di Prenotazione
    @Test
    void testCheckRoomAvailabilityForModificationRequests() {
        when(roomCategoryService.getAvailableRoomsFromPaidBooking(anyString(), anyString(), anyString())).thenReturn(3);

        ResponseEntity<?> response = hotelManagementSystemController.checkRoomAvailabilityForModificationRequests("Double", "2025-02-01", "2025-02-07");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Map.of("available", 3), response.getBody());
    }

    // Test per Endpoint per Richiedere una Modifica di una Prenotazione Confermata
    @Test
    void testRequestModifyBooking() {
        doNothing().when(bookingService).requestModifyBooking(anyInt(), anyString(), anyString());

        ResponseEntity<?> response = hotelManagementSystemController.requestModifyBooking(1, "2025-02-01", "2025-02-07");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Booking modification request submitted successfully!", response.getBody());
    }

    // **CASO D'USO N°6 (UC6): MODIFICA DETTAGLI DELLA PRENOTAZIONE**

    // Test per Endpoint per Ottenere la Lista delle Richieste di Modifica delle Prenotazioni
    @Test
    void testGetModificationRequests() {
        when(bookingService.getModificationRequests()).thenReturn(List.of(new PaidBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getModificationRequests();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    // Test per Endpoint per Gestire una Richiesta di Modifica di una Prenotazione Confermata
    @Test
    void testManageRequestModifyBooking() {
        doNothing().when(bookingService).manageRequestModifyBooking(anyInt(), anyBoolean());

        ResponseEntity<?> response = hotelManagementSystemController.manageRequestModifyBooking(1, true);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Booking modification reviewed successfully!", response.getBody());
    }

    // **CASO D'USO N°7 (UC7): ANNULLA PRENOTAZIONE**

    // Test per Endpoint per Annullare una Prenotazione Confermata da un Pagamento Precedente
    @Test
    void testCancelBookingForCustomer() {
        doNothing().when(bookingService).cancelBookingForCustomer(anyInt());

        ResponseEntity<?> response = hotelManagementSystemController.cancelBookingForCustomer(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Booking successfully cancelled.", response.getBody());
    }

    // **CASO D'USO N°8 (UC8): CANCELLA PRENOTAZIONE**

    // Test per Endpoint per Visualizzare la Lista delle Prenotazioni Confermate da un Pagamento Precedente
    @Test
    void testGetPaidBookingsForReceptionist() {
        when(bookingService.getPaidBookingsForReceptionist()).thenReturn(List.of(new PaidBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getPaidBookingsForReceptionist();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    // Test per Endpoint per Cancellare una Prenotazione Confermata da un Pagamento Precedente
    @Test
    void testCancelBookingForReceptionist() {
        doNothing().when(bookingService).cancelBookingForReceptionist(anyInt());

        ResponseEntity<?> response = hotelManagementSystemController.cancelBookingForReceptionist(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Booking and associated payment cancelled successfully!", response.getBody());
    }

    // **CASO D'USO N°9 (UC9): CHECK-IN IN DIGITALE**

    // Test per Endpoint per Visualizzare la Lista delle Prenotazioni Confermate e Abilitate al Check-in
    @Test
    void testGetPaidBookingsReadyForCheckIn() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("customer@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(bookingService.getPaidBookingsReadyForCheckIn("customer@example.com")).thenReturn(List.of(new PaidBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getPaidBookingsReadyForCheckIn();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    // Test per Endpoint per Effettuare il Check-in di una Prenotazione Confermata
    @Test
    void testConfirmCheckIn() {
        doNothing().when(bookingService).confirmCheckIn(anyInt());
    
        ResponseEntity<?> response = hotelManagementSystemController.confirmCheckIn(1);
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Check-in confirmed successfully.", response.getBody());
    
        verify(bookingService, times(1)).confirmCheckIn(1);
    }

    // **CASO D'USO N°10 (UC10): CHECK-OUT IN DIGITALE**

    // Test per Endpoint per Visualizzare la Lista delle Prenotazioni Confermate e Abilitate al Check-out
    @Test
    void testGetPaidBookingsReadyForCheckOut() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("customer@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(bookingService.getPaidBookingsReadyForCheckOut("customer@example.com")).thenReturn(List.of(new PaidBooking()));

        ResponseEntity<?> response = hotelManagementSystemController.getPaidBookingsReadyForCheckOut();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    // Test per Endpoint per Effettuare il Check-out di una Prenotazione Confermata
    @Test
    void testConfirmCheckOut() {
        doNothing().when(bookingService).confirmCheckOut(anyInt());
    
        ResponseEntity<?> response = hotelManagementSystemController.confirmCheckOut(1);
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Check-out confirmed successfully.", response.getBody());

        verify(bookingService, times(1)).confirmCheckOut(1);
    }

    // **CASO D'USO N°11 (UC11): ASSEGNA ATTIVITÀ DEL PERSONALE**

    // Test per Endpoint per Visualizzare la Lista delle Stanze Sporche
    @Test
    void testGetDirtyRooms() {
        when(cleaningTaskService.getDirtyRooms()).thenReturn(List.of(new Room()));

        ResponseEntity<?> response = hotelManagementSystemController.getDirtyRooms();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    // Test per Endpoint per Visualizzare la Lista dei Cleaning Staff
    @Test
    void testGetAllCleaningStaff() {
        when(cleaningTaskService.getAllCleaningStaff()).thenReturn(List.of(new CleaningStaff()));

        ResponseEntity<?> response = hotelManagementSystemController.getAllCleaningStaff();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    // Test per Endpoint per Assegnare un Task di Pulizia
    @Test
    void testAssignCleaningTask() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
    
        doNothing().when(cleaningTaskService).assignCleaningTask(anyString(), anyInt(), anyInt());
    
        ResponseEntity<?> response = hotelManagementSystemController.assignCleaningTask(1, 101);
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Task assigned successfully.", response.getBody());
    
        verify(cleaningTaskService, times(1)).assignCleaningTask("test@example.com", 1, 101);
    }
    
    
    // **CASO D'USO N°12 (UC12): GESTISCI STATO DELLE CAMERE**

    // Test per Endpoint per Visualizzare la Lista dei Task di Pulizia
    @Test
    void testGetRoomsPendingClean() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("cleaner@example.com");
        SecurityContextHolder.setContext(securityContext);
    
        when(cleaningTaskService.getRoomsPendingClean("cleaner@example.com")).thenReturn(List.of(new Room()));
    
        ResponseEntity<?> response = hotelManagementSystemController.getRoomsPendingClean();

        assertEquals(200, response.getStatusCode().value(), "Expected status 200 but got " + response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }
    
    // Test per Endpoint per Gestire l'Assegnazione di un Task di Pulizia
    @Test
    void testManageCleaningTask() {
        doNothing().when(cleaningTaskService).manageCleaningTask(anyInt(), anyString());
    
        ResponseEntity<?> response = hotelManagementSystemController.manageCleaningTask(101, "Clean");
    
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Room status updated successfully.", response.getBody());
    
        verify(cleaningTaskService, times(1)).manageCleaningTask(101, "Clean");
    }

}
