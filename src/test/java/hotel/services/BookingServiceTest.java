package hotel.services;

import hotel.models.*;
import hotel.repositories.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {

    // Attributi
    @Mock
    private RequestBookingRepository requestBookingRepository;

    @Mock
    private PaidBookingRepository paidBookingRepository;

    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ReceptionistRepository recepetinistRepository;

    @Mock
    private RoomCategoryService roomCategoryService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **CASO D'USO N°1 (UC1): EFFETTUA RICHIESTA DI PRENOTAZIONE**

    // Test del Metodo per Aggiungere una Richiesta di Prenotazione di una specifica tipologia di camera in un determinato intervallo di date
    @Test
    void testRequestBooking() {
        String email = "customer@example.com";
        String roomType = "Single";
        String startDate = LocalDate.now().plusDays(2).toString();
        String endDate = LocalDate.now().plusDays(5).toString();

        Customer customer = new Customer();
        RoomCategory roomCategory = new RoomCategory();
        roomCategory.setPriceForNight(100.0);

        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(roomCategoryRepository.findByRoomType(roomType)).thenReturn(roomCategory);
        when(roomCategoryService.isRoomAvailableFromRequestBooking(roomType, startDate, endDate)).thenReturn(true);

        assertDoesNotThrow(() -> bookingService.requestBooking(email, roomType, startDate, endDate));

        verify(requestBookingRepository, times(1)).save(any(RequestBooking.class));
    }

    // **CASO D'USO N°2 (UC2): GESTISCI RICHIESTA DI PRENOTAZIONE**

    // Test del Metodo per Visualizzare la Lista delle Richieste di Prenotazione
    @Test
    void testGetRequestsBooking() {
        List<RequestBooking> mockRequests = List.of(new RequestBooking(), new RequestBooking());
    
        when(requestBookingRepository.findPendingRequests()).thenReturn(mockRequests);
    
        List<RequestBooking> result = bookingService.getRequestsBooking();
    
        assertNotNull(result);
        assertEquals(2, result.size(), "Expected 2 pending booking requests but got " + result.size());
        verify(requestBookingRepository, times(1)).findPendingRequests();
    }
    
    // Test del Metodo per Gestire una Richiesta di Prenotazione
    @Test
    void testManageRequestBooking() {
        int bookingId = 1;
        String status = "Approved";
        String email = "test@example.com";

        // Mock Receptionist
        Receptionist mockReceptionist = new Receptionist();
        mockReceptionist.setEmail(email);

        // Mock RequestBooking
        RequestBooking mockBooking = new RequestBooking();
        mockBooking.setStatus("Pending");

        // Definisco i comportamenti dei mock
        when(recepetinistRepository.findByEmail(email)).thenReturn(mockReceptionist);
        when(requestBookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        // Eseguo il metodo e verifico che non lanci eccezioni
        assertDoesNotThrow(() -> bookingService.manageRequestBooking(email, status, bookingId));

        // Verifico che lo stato sia stato aggiornato correttamente
        assertEquals("Approved", mockBooking.getStatus());
        assertEquals(mockReceptionist, mockBooking.getReceptionist());

        // Verifico che il metodo save sia stato chiamato una volta
        verify(requestBookingRepository, times(1)).save(mockBooking);
    }
    
    // **CASO D'USO N°3 (UC3): EFFETTUA PAGAMENTO**

    // Test del Metodo per Visualizzare la Lista delle Richieste di Prenotazione valide per il Pagamento
    @Test
    public void testGetPayments() {
        Customer mockCustomer = new Customer();
        mockCustomer.setIdCustomer(1);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(mockCustomer);
        when(requestBookingRepository.findApprovedBookings(1)).thenReturn(Collections.singletonList(new RequestBooking()));

        List<RequestBooking> payments = bookingService.getPayments("test@example.com");

        assertNotNull(payments);
        verify(requestBookingRepository, times(1)).findApprovedBookings(1);
    }

    // Test del Metodo per Effettuare il Pagamento di una Richiesta di Prenotazione
    @Test
    public void testSetPayment() {
        RequestBooking mockRequest = new RequestBooking();
        mockRequest.setIdBooking(1);
        mockRequest.setRoomCategory(new RoomCategory());
        mockRequest.setCustomer(new Customer());
        mockRequest.setTotalPrice(500.0);

        when(requestBookingRepository.findById(1)).thenReturn(Optional.of(mockRequest));

        bookingService.setPayment("Credit Card", "1234 5678 9012 3424", "12/2025", 123, "John Doe", 1);

        verify(paidBookingRepository, times(1)).save(any(PaidBooking.class));
        verify(requestBookingRepository, times(1)).delete(mockRequest);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    // **CASO D'USO N°4 (UC4): VISUALIZZA DETTAGLI DELLA PRENOTAZIONE**

    // Test del Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
    @Test
    public void testGetPaidBookingsForCustomer() {
        Customer mockCustomer = new Customer();
        mockCustomer.setIdCustomer(1);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(mockCustomer);
        when(paidBookingRepository.findPaidBookingsForCustomer(mockCustomer.getIdCustomer())).thenReturn(Collections.singletonList(new PaidBooking()));

        List<PaidBooking> bookings = bookingService.getPaidBookingsForCustomer("test@example.com");

        assertNotNull(bookings);
        verify(paidBookingRepository, times(1)).findPaidBookingsForCustomer(mockCustomer.getIdCustomer());
    }

    // **CASO D'USO N°5 (UC5): RICHIEDI MODIFICA DELLA PRENOTAZIONE**

    // Test del Metodo per Richiedere una Modifica di una Prenotazione confermata da un Pagamento Precedente
    @Test
    void testRequestModifyBooking() {
        int bookingId = 1;
        String newStartDate = LocalDate.now().plusDays(2).toString();
        String newEndDate = LocalDate.now().plusDays(5).toString();

        PaidBooking paidBooking = new PaidBooking();
        RoomCategory roomCategory = new RoomCategory();
        roomCategory.setRoomType("Single");
        paidBooking.setRoomCategory(roomCategory);
        paidBooking.setStartDate(LocalDate.now().plusDays(1));
        paidBooking.setEndDate(LocalDate.now().plusDays(4));

        when(paidBookingRepository.findById(bookingId)).thenReturn(Optional.of(paidBooking));
        when(roomCategoryService.isRoomAvailableFromPaidBooking(anyString(), anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> bookingService.requestModifyBooking(bookingId, newStartDate, newEndDate));

        verify(paidBookingRepository, times(1)).save(any(PaidBooking.class));
    }

    // **CASO D'USO N°6 (UC6): MODIFICA DETTAGLI DELLA PRENOTAZIONE**

    // Test del Metodo per Ottenere la Lista delle Richieste di Modifica delle Prenotazioni
    @Test
    void testGetModificationRequests() {
        List<PaidBooking> mockRequests = List.of(new PaidBooking(), new PaidBooking());
    
        when(paidBookingRepository.findPaidBookingsInPendingModification()).thenReturn(mockRequests);
    
        List<PaidBooking> result = bookingService.getModificationRequests();
    
        assertNotNull(result);
        assertEquals(2, result.size(), "Expected 2 modification requests but got " + result.size());
        verify(paidBookingRepository, times(1)).findPaidBookingsInPendingModification();
    }
    
    // Test del Metodo per Gestire una Richiesta di Modifica di una Prenotazione confermata da un Pagamento Precedente
    @Test
    public void testManageRequestModifyBooking() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        mockPaidBooking.setStatus("Pending Modification");

        when(paidBookingRepository.findById(1)).thenReturn(Optional.of(mockPaidBooking));

        bookingService.manageRequestModifyBooking(1, true);

        assertEquals("Paid", mockPaidBooking.getStatus());
        verify(paidBookingRepository, times(1)).save(mockPaidBooking);
    }

    // **CASO D'USO N°7 (UC7): ANNULLA PRENOTAZIONE**

    // Test del Metodo per Annullare una Prenotazione confermata da un Pagamento Precedente
    @Test
    public void testCancelBookingForCustomer() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        mockPaidBooking.setStatus("Paid");

        when(paidBookingRepository.findById(1)).thenReturn(Optional.of(mockPaidBooking));

        bookingService.cancelBookingForCustomer(1);

        assertEquals("Cancelled", mockPaidBooking.getStatus());
        verify(paidBookingRepository, times(1)).save(mockPaidBooking);
    }

    // **CASO D'USO N°8 (UC8): CANCELLA PRENOTAZIONE**

    // Test del Metodo per Visualizzare Lista delle Prenotazioni confermate da un Pagamento Precedente   
    @Test
    public void testGetPaidBookingsForReceptionist() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        when(paidBookingRepository.findPaidBookingsForReceptionists()).thenReturn(Collections.singletonList(mockPaidBooking));

        List<PaidBooking> bookings = bookingService.getPaidBookingsForReceptionist();

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        verify(paidBookingRepository, times(1)).findPaidBookingsForReceptionists();
    }

    // Test del Metodo per Cancellare una Prenotazione confermata da un Pagamento Precedente
    @Test
    public void testCancelBookingForReceptionist() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        when(paidBookingRepository.findById(1)).thenReturn(Optional.of(mockPaidBooking));

        bookingService.cancelBookingForReceptionist(1);

        verify(paymentRepository, times(1)).deleteByIdBooking(1);
        verify(paidBookingRepository, times(1)).delete(mockPaidBooking);
    }

    // **CASO D'USO N°9 (UC9): CHECK-IN IN DIGITALE**

    // Test del Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-In
    @Test
    void testGetPaidBookingsReadyForCheckIn() {
        String email = "customer@example.com";
        Customer customer = new Customer();
        customer.setIdCustomer(1);
    
        List<PaidBooking> mockBookings = List.of(new PaidBooking(), new PaidBooking());
    
        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(paidBookingRepository.findPaidBookingsReadyForCheckIn(customer.getIdCustomer())).thenReturn(mockBookings);
    
        List<PaidBooking> result = bookingService.getPaidBookingsReadyForCheckIn(email);
    
        assertNotNull(result);
        assertEquals(2, result.size(), "Expected 2 bookings ready for check-in but got " + result.size());
        verify(customerRepository, times(1)).findByEmail(email);
        verify(paidBookingRepository, times(1)).findPaidBookingsReadyForCheckIn(customer.getIdCustomer());
    }    

    // Test del Metodo per Effettuare il Check-In di una Prenotazione confermata da un Pagamento Precedente
    @Test
    public void testConfirmCheckIn() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        mockPaidBooking.setStatus("Paid");
    
        when(paidBookingRepository.findById(1)).thenReturn(Optional.of(mockPaidBooking));
    
        bookingService.confirmCheckIn(1);
    
        assertEquals("Check-In Confirmed", mockPaidBooking.getStatus());
    
        verify(paidBookingRepository, times(1)).save(mockPaidBooking);
    }

    // **CASO D'USO N°10 (UC10): CHECK-OUT IN DIGITALE**

    // Test del Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-In
    @Test
    void testGetPaidBookingsReadyForCheckOut() {
        String email = "customer@example.com";
        Customer customer = new Customer();
        customer.setIdCustomer(1);
    
        List<PaidBooking> mockBookings = List.of(new PaidBooking(), new PaidBooking());
    
        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(paidBookingRepository.findPaidBookingsReadyForCheckOut(customer.getIdCustomer())).thenReturn(mockBookings);
    
        List<PaidBooking> result = bookingService.getPaidBookingsReadyForCheckOut(email);
    
        assertNotNull(result);
        assertEquals(2, result.size(), "Expected 2 bookings ready for check-out but got " + result.size());
        verify(customerRepository, times(1)).findByEmail(email);
        verify(paidBookingRepository, times(1)).findPaidBookingsReadyForCheckOut(customer.getIdCustomer());
    }
     
    // Test del Metodo per Effettuare il Check-Out di una Prenotazione confermata da un Pagamento Precedente
    @Test
    public void testConfirmCheckOut() {
        PaidBooking mockPaidBooking = new PaidBooking();
        mockPaidBooking.setIdBooking(1);
        mockPaidBooking.setStatus("Paid");

        when(paidBookingRepository.findById(1)).thenReturn(Optional.of(mockPaidBooking));

        bookingService.confirmCheckOut(1);

        assertEquals("Check-Out Confirmed", mockPaidBooking.getStatus());
        verify(paidBookingRepository, times(1)).save(mockPaidBooking);
    }

}
