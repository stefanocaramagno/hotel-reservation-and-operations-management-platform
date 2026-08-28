package hotel.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewPagesControllerTest {

    // Attributi
    @InjectMocks
    private ViewPagesController viewPagesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    // Home Page
    @Test
    public void testRedirectToHome() {
        String viewName = viewPagesController.redirectToHome();
        assertEquals("home", viewName);
    }

    // **Customer Area**

    // Pagina di Accesso del Customer
    @Test
    public void testCustomerAccess() {
        String viewName = viewPagesController.customerAccess();
        assertEquals("customer/customerAccess", viewName);
    }

    // Area Personale del Customer
    @Test
    void testCustomerArea() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        Model mockModel = Mockito.mock(Model.class);


        when(mockAuthentication.getDetails()).thenReturn("John Doe");
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        String viewName = viewPagesController.customerArea(mockModel);

        assertEquals("customer/customerArea", viewName);

        verify(mockModel).addAttribute("fullName", "John Doe");

        SecurityContextHolder.clearContext();
    }

    // Dettagli delle Prenotazioni
    @Test
    public void testViewBookingDetails() {
        String viewName = viewPagesController.viewBookingDetails();
        assertEquals("customer/viewBookingDetails", viewName);
    }

    // Modifica delle Prenotazioni
    @Test
    public void testRequestBookingModification() {
        String viewName = viewPagesController.requestBookingModification();
        assertEquals("customer/requestBookingModification", viewName);
    }

    // Pagina per i Pagamenti
    @Test
    public void testMakePayment() {
        String viewName = viewPagesController.makePayment();
        assertEquals("customer/makePayment", viewName);
    }

    // Visualizzazione delle Prenotazioni Accettate
    @Test
    public void testAcceptedBookings() {
        String viewName = viewPagesController.acceptedBookings();
        assertEquals("customer/acceptedBookings", viewName);
    }

    // Check-In Digitale
    @Test
    public void testDigitalCheckIn() {
        String viewName = viewPagesController.digitalCheckIn();
        assertEquals("customer/digitalCheckIn", viewName);
    }

    // Check-Out Digitale
    @Test
    public void testDigitalCheckOut() {
        String viewName = viewPagesController.digitalCheckOut();
        assertEquals("customer/digitalCheckOut", viewName);
    }

    // Annullamento Prenotazioni
    @Test
    public void testCancelBooking() {
        String viewName = viewPagesController.cancelBooking();
        assertEquals("customer/cancelBooking", viewName);
    }

    // **Receptionist Area**

    // Pagina di Accesso del Receptionist
    @Test
    public void testReceptionistAccess() {
        String viewName = viewPagesController.receptionistAccess();
        assertEquals("receptionist/receptionistAccess", viewName);
    }

    // Area Personale del Receptionist
    @Test
    public void testReceptionistArea() {
        String viewName = viewPagesController.receptionistArea();
        assertEquals("receptionist/receptionistArea", viewName);
    }

    // Gestione delle Richieste di Prenotazione
    @Test
    public void testManageBookingRequests() {
        String viewName = viewPagesController.manageBookingRequests();
        assertEquals("receptionist/manageBookingRequests", viewName);
    }

    // Modifica dei Dettagli delle Prenotazioni
    @Test
    public void testModifyBookingDetails() {
        String viewName = viewPagesController.modifyBookingDetails();
        assertEquals("receptionist/modifyBookingDetails", viewName);
    }

    // Annullamento Prenotazioni per il Cliente
    @Test
    public void testCancelBookingForHotel() {
        String viewName = viewPagesController.cancelBookingForHotel();
        assertEquals("receptionist/cancelBookingForHotel", viewName);
    }

    // Visualizzazione dei Log di Sistema
    @Test
    public void testCiewLogs() {
        String viewName = viewPagesController.viewLogs();
        assertEquals("receptionist/viewLogs", viewName);
    }

    // **Operations Manager Area**

    // Pagina di Accesso dell'Operations Manager
    @Test
    public void testOperationsManagerAccess() {
        String viewName = viewPagesController.operationsManagerAccess();
        assertEquals("operationsManager/operationsManagerAccess", viewName);
    }

    // Area Personale dell'Operations Manager
    @Test
    public void testOperationsManagerArea() {
        String viewName = viewPagesController.operationsManagerArea();
        assertEquals("operationsManager/operationsManagerArea", viewName);
    }

    // Assegnazione dei Compiti al Personale
    @Test
    public void testAssignTasks() {
        String viewName = viewPagesController.assignTasks();
        assertEquals("operationsManager/assignTasks", viewName);
    }

    // **Cleaning Staff Area**

    // Pagina di Accesso dello Staff di Pulizia
    @Test
    public void testCleaningStaffAccess() {
        String viewName = viewPagesController.cleaningStaffAccess();
        assertEquals("cleaningStaff/cleaningStaffAccess", viewName);
    }
 
    // Area Personale dello Staff di Pulizia
    @Test
    void testCleaningStaffArea() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        Model mockModel = Mockito.mock(Model.class);

        when(mockAuthentication.getDetails()).thenReturn("John Doe");
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        String viewName = viewPagesController.cleaningStaffArea(mockModel);

        assertEquals("cleaningStaff/cleaningStaffArea", viewName);

        verify(mockModel).addAttribute("fullName", "John Doe");

        SecurityContextHolder.clearContext();
    }
    
    // Aggiornamento dello Stato delle Camere
    @Test
    public void testUpdateRoomStatus() {
        String viewName = viewPagesController.updateRoomStatus();
        assertEquals("cleaningStaff/updateRoomStatus", viewName);
    }

}
