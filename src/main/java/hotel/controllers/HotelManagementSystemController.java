package hotel.controllers;

import hotel.models.Customer;
import hotel.models.Receptionist;
import hotel.models.RequestBooking;
import hotel.models.Room;
import hotel.models.OperationsManager;
import hotel.models.PaidBooking;
import hotel.models.CleaningStaff;
import hotel.services.BookingService;
import hotel.services.RoomCategoryService;
import hotel.services.CleaningTaskService;
import hotel.services.CustomerService;
import hotel.services.ReceptionistService;
import hotel.services.OperationsManagerService;
import hotel.services.CleaningStaffService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotelManagementSystem")
public class HotelManagementSystemController {

    // Attributi
    @Autowired 
    private CustomerService customerService;

    @Autowired 
    private ReceptionistService receptionistService;

    @Autowired 
    private OperationsManagerService operationsManagerService;

    @Autowired 
    private CleaningStaffService cleaningStaffService;

    @Autowired 
    private BookingService bookingService;

    @Autowired 
    private RoomCategoryService roomCategoryService;

    @Autowired 
    private CleaningTaskService cleaningTaskService;

    // **REGISTRAZIONI UTENTI DEL SISTEMA**

    // Endpoint per la Registrazione del Receptionist
    @PostMapping("/registerCustomer")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        try {
            customerService.registerCustomer(customer);
            return ResponseEntity.ok("Customer registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering customer: " + e.getMessage());
        }
    }

    // Endpoint per la Registrazione del Receptionist
    @PostMapping("/registerReceptionist")
    public ResponseEntity<String> registerReceptionist(@RequestBody Receptionist receptionist) {
        try {
            receptionistService.registerReceptionist(receptionist);
            return ResponseEntity.ok("Receptionist registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering receptionist: " + e.getMessage());
        }
    }

    // Endpoint per la Registrazione del Operations Manager
    @PostMapping("/registerOperationsManager")
    public ResponseEntity<String> registerOperationsManager(@RequestBody OperationsManager operationsManager) {
        try {
            operationsManagerService.registerOperationsManager(operationsManager);
            return ResponseEntity.ok("Operations Manager registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering operations manager: " + e.getMessage());
        }
    }

    // Endpoint per la Registrazione del Cleaning Staff
    @PostMapping("/registerCleaningStaff")
    public ResponseEntity<String> registerCleaningStaff(@RequestBody CleaningStaff cleaningStaff) {
        try {
            cleaningStaffService.registerCleaningStaff(cleaningStaff);
            return ResponseEntity.ok("Cleaning Staff registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering cleaning staff: " + e.getMessage());
        }
    }

    // **CASO D'USO N°1 (UC1): EFFETTUA RICHIESTA DI PRENOTAZIONE**

    // Endpoint per Calcolare il Numero di Stanza Disponibili per un determinato Tipo di Stanza e un Intervallo di Date
    @GetMapping("/checkRoomAvailabilityforRequestBooking")
    public ResponseEntity<?> checkRoomAvailabilityforRequestBooking(@RequestParam String roomType,@RequestParam String startDate,@RequestParam String endDate) {
        try {
            int availableRooms = roomCategoryService.getAvailableRoomsFromRequestBooking(roomType, startDate, endDate);
            return ResponseEntity.ok().body(Map.of("available", availableRooms));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    // Endpoint per Creare una Richiesta di Prenotazione
    @PostMapping("/requestBooking")
    public ResponseEntity<?> requestBooking(@RequestParam String roomType, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String customerEmail = authentication.getName();
    
            bookingService.requestBooking(customerEmail, roomType, startDate, endDate);
            return ResponseEntity.ok("Booking request submitted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // **CASO D'USO N°2 (UC2): GESTISCI RICHIESTA DI PRENOTAZIONE**

    // Endpoint per Visualizzare la Lista delle Richieste di Prenotazione
    @GetMapping("/getRequestsBooking")
    public ResponseEntity<?> getRequestsBooking() {
        try {
            List<RequestBooking> bookingRequests = bookingService.getRequestsBooking();
            return ResponseEntity.ok(bookingRequests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // Endpoint per Gestire una Richiesta di Prenotazione
    @PostMapping("/manageRequestBooking")
    public ResponseEntity<?> manageRequestBooking(@RequestParam String status, @RequestParam int idBooking) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String receptionistEmail = authentication.getName();

            bookingService.manageRequestBooking(receptionistEmail, status, idBooking);
            return ResponseEntity.ok("Status updated successfully for booking ID: " + idBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }    

    // **CASO D'USO N°3 (UC3): EFFETTUA PAGAMENTO**

    // Endopoint per Visualizzare la Lista delle Richieste di Prenotazione valide per il Pagamento
    @GetMapping("/getPayments")
    public ResponseEntity<?> getPayments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String customerEmail = authentication.getName();
    
            List<RequestBooking> payments = bookingService.getPayments(customerEmail);
            return ResponseEntity.ok(payments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }   

    // Endopoint per Effettuare il Pagamento di una Richiesta di Prenotazione
    @GetMapping("/setPayment")
    public ResponseEntity<String> setPayment(@RequestParam String paymentMethod, @RequestParam String cardNumber, @RequestParam String expiryDate, 
                                             @RequestParam int cvv, @RequestParam String cardHolder, @RequestParam int idBooking) {
        try {
            bookingService.setPayment(paymentMethod, cardNumber, expiryDate, cvv, cardHolder, idBooking);
            return ResponseEntity.ok("Payment successfully processed for booking ID: " + idBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    } 

    // **CASO D'USO N°4 (UC4): VISUALIZZA DETTAGLI DELLA PRENOTAZIONE**

    // Endopoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
    @GetMapping("/getPaidBookingsForCustomer")
    public ResponseEntity<?> getPaidBookingsForCustomer() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String customerEmail = authentication.getName();  
    
            List<PaidBooking> bookings = bookingService.getPaidBookingsForCustomer(customerEmail);
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // **CASO D'USO N°5 (UC5): RICHIEDI MODIFICA DELLA PRENOTAZIONE**

    // Endpoint per Calcolare il Numero di Stanze Disponibili per un determinato Tipo di Stanza e un Intervallo di Date 
    @GetMapping("/checkRoomAvailabilityForModificationRequests")
    public ResponseEntity<?> checkRoomAvailabilityForModificationRequests(@RequestParam String roomType, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            int availableRooms = roomCategoryService.getAvailableRoomsFromPaidBooking(roomType, startDate, endDate);
            return ResponseEntity.ok().body(Map.of("available", availableRooms));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred."));
        }
    }   

    // Endopoint per Richiedere una Modifica di una Prenotazione confermata da un Pagamento Precedente
    @PostMapping("/requestModifyBooking")
    public ResponseEntity<?> requestModifyBooking(@RequestParam int idBooking, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            bookingService.requestModifyBooking(idBooking, startDate, endDate);
            return ResponseEntity.ok("Booking modification request submitted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // **CASO D'USO N°6 (UC6): MODIFICA DETTAGLI DELLA PRENOTAZIONE**

    // Endpoint per Ottenere la Lista delle Richieste di Modifica delle Prenotazioni
    @GetMapping("/getModificationRequests")
    public ResponseEntity<?> getModificationRequests() {
        try {
            List<PaidBooking> response = bookingService.getModificationRequests();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // Endopoint per Gestire una Richiesta di Modifica di una Prenotazione confermata da un Pagamento Precedente
    @PostMapping("/manageRequestModifyBooking")
    public ResponseEntity<?> manageRequestModifyBooking(@RequestParam int idBooking, @RequestParam boolean approved) {
        try {
            bookingService.manageRequestModifyBooking(idBooking, approved);
            return ResponseEntity.ok("Booking modification reviewed successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }  

    // **CASO D'USO N°7 (UC7): ANNULLA PRENOTAZIONE**

    // Endpoint per Annullare una Prenotazione confermata da un Pagamento Precedente
    @PostMapping("/cancelBookingForCustomer")
    public ResponseEntity<?> cancelBookingForCustomer(@RequestParam int idBooking) {
        try {
            bookingService.cancelBookingForCustomer(idBooking);
            return ResponseEntity.ok("Booking successfully cancelled.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // **CASO D'USO N°8 (UC8): CANCELLA PRENOTAZIONE**

    // Endopoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
    @GetMapping("/getPaidBookingsForReceptionist")
    public ResponseEntity<?> getPaidBookingsForReceptionist() {
        try {
            List<PaidBooking> paidBookings = bookingService.getPaidBookingsForReceptionist();
            return ResponseEntity.ok(paidBookings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }      

    // Endpoint per Cancellare una Prenotazione confermata da un Pagamento Precedente   
    @PostMapping("/cancelBookingForReceptionist")
    public ResponseEntity<?> cancelBookingForReceptionist(@RequestParam int idBooking) {
        try {
            bookingService.cancelBookingForReceptionist(idBooking);
            return ResponseEntity.ok("Booking and associated payment cancelled successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // **CASO D'USO N°9 (UC9): CHECK-IN IN DIGITALE**

    // Endpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-In 
    @GetMapping("/getPaidBookingsReadyForCheckIn")
    public ResponseEntity<?> getPaidBookingsReadyForCheckIn() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String customerEmail = authentication.getName();  

            List<PaidBooking> paidBookingsForCheckIn = bookingService.getPaidBookingsReadyForCheckIn(customerEmail);
            return ResponseEntity.ok(paidBookingsForCheckIn);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }   

    // Endpoint per Effettuare il Check-In di una Prenotazione confermata da un Pagamento Precedente
    @PostMapping("/confirmCheckIn")
    public ResponseEntity<?> confirmCheckIn(@RequestParam int idBooking) {
        try {
            bookingService.confirmCheckIn(idBooking);
            return ResponseEntity.ok("Check-in confirmed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // **CASO D'USO N°10 (UC10): CHECK-OUT IN DIGITALE**

    // Endpoint per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-Out 
    @GetMapping("/getPaidBookingsReadyForCheckOut")
    public ResponseEntity<?> getPaidBookingsReadyForCheckOut() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String customerEmail = authentication.getName();  

            List<PaidBooking> paidBookingsForCheckOut = bookingService.getPaidBookingsReadyForCheckOut(customerEmail);
            return ResponseEntity.ok(paidBookingsForCheckOut);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    } 

    // Endpoint per Effettuare il Check-Out di una Prenotazione confermata da un Pagamento Precedente
    @PostMapping("/confirmCheckOut")
    public ResponseEntity<?> confirmCheckOut(@RequestParam int idBooking) {
        try {
            bookingService.confirmCheckOut(idBooking);
            return ResponseEntity.ok("Check-out confirmed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }    

    // **CASO D'USO N°11 (UC11): ASSEGNA ATTIVITÀ DEL PERSONALE**

    // Endpoint per Visualizzare la Lista delle Stanze Sporche
    @GetMapping("/getDirtyRooms")
    public ResponseEntity<?> getDirtyRooms() {
        try {
            List<Room> dirtyRooms = cleaningTaskService.getDirtyRooms();
            return ResponseEntity.ok(dirtyRooms);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // Endpoint per Visualizzare la Lista dei Cleaning Staff
    @GetMapping("/getAllCleaningStaff")
    public ResponseEntity<?> getAllCleaningStaff() {
        try {
            List<CleaningStaff> cleaningStaff = cleaningTaskService.getAllCleaningStaff();
            return ResponseEntity.ok(cleaningStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    } 

    // Endpoint per Assegnare un Task di Pulizia 
    @PostMapping("/assignCleaningTask")
    public ResponseEntity<?> assignCleaningTask(@RequestParam int idCleaningStaff, @RequestParam int idRoom) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String operationsManagerEmail = authentication.getName();

            cleaningTaskService.assignCleaningTask(operationsManagerEmail, idCleaningStaff, idRoom);
            return ResponseEntity.ok("Task assigned successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
    // **CASO D'USO N°12 (UC12): GESTISCI STATO DELLE CAMERE**

    // Endpoint per Visualizzare la Lista dei Task di Pulizia
    @GetMapping("/getRoomsPendingClean")
    public ResponseEntity<?> getRoomsPendingClean() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String cleaningStaffEmail = authentication.getName();

            List<Room> rooms = cleaningTaskService.getRoomsPendingClean(cleaningStaffEmail);
            return ResponseEntity.ok(rooms);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    // Endpoint per Gestire l'assegnazione di un Task di Pulizia
    @PostMapping("/manageCleaningTask")
    public ResponseEntity<?> manageCleaningTask(@RequestParam int idRoom, @RequestParam String newStatus) {
        try {
            cleaningTaskService.manageCleaningTask(idRoom, newStatus);
            return ResponseEntity.ok("Room status updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
    
}
