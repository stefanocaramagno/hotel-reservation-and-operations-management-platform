package hotel.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Controller
public class ViewPagesController {

    // Redirezione alla Home Page all'avvio dell'applicazione
    @GetMapping("/")
    public String redirectToHome() {
        return "home"; 
    }

    // Home Page
    @GetMapping("/home")
    public String homePage() {
        return "home"; 
    }
    
    // **Customer Area**

    // Pagina di Accesso del Customer
    @GetMapping("/customer/customerAccess")
    public String customerAccess() {
        return "customer/customerAccess"; 
    }

    // Area Personale del Customer
    @GetMapping("/customer/customerArea")
    public String customerArea(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        String fullName = authentication.getDetails().toString();
    
        model.addAttribute("fullName", fullName);
    
        return "customer/customerArea";
    }
    
    // Dettagli delle Prenotazioni
    @GetMapping("/customer/viewBookingDetails")
    public String viewBookingDetails() {
        return "customer/viewBookingDetails"; 
    }

    // Modifica delle Prenotazioni
    @GetMapping("/customer/requestBookingModification")
    public String requestBookingModification() {
        return "customer/requestBookingModification";
    }

    // Pagina per i Pagamenti
    @GetMapping("/customer/makePayment")
    public String makePayment() {
        return "customer/makePayment";
    }

    // Visualizzazione delle Prenotazioni Accettate
    @GetMapping("/customer/acceptedBookings")
    public String acceptedBookings() {
        return "customer/acceptedBookings";
    }

    // Check-In Digitale
    @GetMapping("/customer/digitalCheckIn")
    public String digitalCheckIn() {
        return "customer/digitalCheckIn";
    }

    // Check-Out Digitale
    @GetMapping("/customer/digitalCheckOut")
    public String digitalCheckOut() {
        return "customer/digitalCheckOut";
    }

    // Annullamento Prenotazioni
    @GetMapping("/customer/cancelBooking")
    public String cancelBooking() {
        return "customer/cancelBooking";
    }

    // **Receptionist Area**

    // Pagina di Accesso del Receptionist
    @GetMapping("/receptionist/receptionistAccess")
    public String receptionistAccess() {
        return "receptionist/receptionistAccess"; 
    }

    // Area Personale del Receptionist
    @GetMapping("/receptionist/receptionistArea")
    public String receptionistArea() {
        return "receptionist/receptionistArea"; 
    }

    // Gestione delle Richieste di Prenotazione
    @GetMapping("/receptionist/manageBookingRequests")
    public String manageBookingRequests() {
        return "receptionist/manageBookingRequests"; 
    }

    // Modifica dei Dettagli delle Prenotazioni
    @GetMapping("/receptionist/modifyBookingDetails")
    public String modifyBookingDetails() {
        return "receptionist/modifyBookingDetails"; 
    }

    // Annullamento Prenotazioni per il Cliente
    @GetMapping("/receptionist/cancelBookingForHotel")
    public String cancelBookingForHotel() {
        return "receptionist/cancelBookingForHotel"; 
    }

    // Visualizzazione dei Log di Sistema
    @GetMapping("/receptionist/viewLogs")
    public String viewLogs() {
        return "receptionist/viewLogs"; 
    }

    // **Operations Manager Area**

    // Pagina di Accesso dell'Operations Manager
    @GetMapping("/operationsManager/operationsManagerAccess")
    public String operationsManagerAccess() {
        return "operationsManager/operationsManagerAccess"; 
    }

    // Area Personale dell'Operations Manager
    @GetMapping("/operationsManager/operationsManagerArea")
    public String operationsManagerArea() {
        return "operationsManager/operationsManagerArea"; 
    }

    // Assegnazione dei Compiti al Personale
    @GetMapping("/operationsManager/assignTasks")
    public String assignTasks() {
        return "operationsManager/assignTasks"; 
    }

    // **Cleaning Staff Area**

    // Pagina di Accesso dello Staff di Pulizia
    @GetMapping("/cleaningStaff/cleaningStaffAccess")
    public String cleaningStaffAccess() {
        return "cleaningStaff/cleaningStaffAccess"; 
    }
    
    // Area Personale dello Staff di Pulizia
    @GetMapping("/cleaningStaff/cleaningStaffArea")
    public String cleaningStaffArea(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        String fullName = authentication.getDetails().toString();
    
        model.addAttribute("fullName", fullName);
    
        return "cleaningStaff/cleaningStaffArea";
    }

    // Aggiornamento dello Stato delle Camere
    @GetMapping("/cleaningStaff/updateRoomStatus")
    public String updateRoomStatus() {
        return "cleaningStaff/updateRoomStatus"; 
    }

}
