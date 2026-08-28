package hotel.services;

import hotel.models.PaidBooking;
import hotel.models.Payment;
import hotel.models.Customer;
import hotel.models.Receptionist;
import hotel.models.RequestBooking;
import hotel.models.RoomCategory;
import hotel.repositories.RequestBookingRepository;
import hotel.repositories.PaidBookingRepository;
import hotel.repositories.CustomerRepository;
import hotel.repositories.ReceptionistRepository;
import hotel.repositories.PaymentRepository;
import hotel.repositories.RoomCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    // Attributi
    @Autowired
    private RequestBookingRepository requestBoookingRepository;

    @Autowired
    private PaidBookingRepository paidBookingRepository;

    @Autowired
    private RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private RoomCategoryService roomCategoryService;

    // **CASO D'USO N°1 (UC1): EFFETTUA RICHIESTA DI PRENOTAZIONE**

    // Metodo per Creare una Richiesta di Prenotazione
    public void requestBooking(String customerEmail, String roomType, String startDate, String endDate) {
        if (roomType == null || roomType.trim().isEmpty() || 
            startDate == null || startDate.trim().isEmpty() || 
            endDate == null || endDate.trim().isEmpty()) {
            throw new RuntimeException("Invalid booking request: missing required fields.");
        }
    
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new RuntimeException("Customer not found with email: " + customerEmail);
        }
            
        RoomCategory roomCategory = roomCategoryRepository.findByRoomType(roomType);
        if (roomCategory == null) {
            throw new RuntimeException("Room type not found: " + roomType);
        }
    
        LocalDate startDateParsed = LocalDate.parse(startDate);
        LocalDate endDateParsed = LocalDate.parse(endDate);
        LocalDate today = LocalDate.now();
    
        if (startDateParsed.isBefore(today)) {
            throw new RuntimeException("Start date cannot be earlier than today.");
        }

        if (startDateParsed.isEqual(endDateParsed)) {
            throw new RuntimeException("Start date and end date cannot be the same.");
        }
    
        if (!startDateParsed.isBefore(endDateParsed)) {
            throw new RuntimeException("Start date must be before end date.");
        }
    
        if (!roomCategoryService.isRoomAvailableFromRequestBooking(roomType, startDate, endDate)) {
            throw new RuntimeException("No rooms available for the selected dates.");
        }
    
        RequestBooking requestBooking = new RequestBooking();
        requestBooking.setCustomer(customer);
        requestBooking.setRoomCategory(roomCategory);
        requestBooking.setStartDate(startDateParsed);
        requestBooking.setEndDate(endDateParsed);
    
        long days = ChronoUnit.DAYS.between(startDateParsed, endDateParsed);
        double priceForNight = roomCategory.getPriceForNight();
        double totalPrice = priceForNight * days;
        requestBooking.setTotalPrice(totalPrice);
        requestBooking.setStatus("Pending Approval");
    
        requestBoookingRepository.save(requestBooking);
    }

    // **CASO D'USO N°2 (UC2): GESTISCI RICHIESTA DI PRENOTAZIONE**

    // Metodo per Visualizzare la Lista delle Richieste di Prenotazione
    public List<RequestBooking> getRequestsBooking() {
        return requestBoookingRepository.findPendingRequests();
    }

    // Metodo per Gestire una Richiesta di Prenotazione
    public void manageRequestBooking(String receptionistEmail, String status, int idBooking) {
        Receptionist receptionist = receptionistRepository.findByEmail(receptionistEmail);
        if (receptionist == null) {
            throw new RuntimeException("Recepetionist not found with email: " + receptionistEmail);
        }

        RequestBooking requestBooking = requestBoookingRepository.findById(idBooking)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: " + idBooking));
        
        requestBooking.setReceptionist(receptionist);
        requestBooking.setStatus(status);
        requestBoookingRepository.save(requestBooking);
    }

    // **CASO D'USO N°3 (UC3): EFFETTUA PAGAMENTO**

    // Metodo per Visualizzare la Lista delle Richieste di Prenotazione valide per il Pagamento
    public List<RequestBooking> getPayments(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new RuntimeException("Customer not found with email: " + customerEmail);
        }

        int idCustomer = customer.getIdCustomer();
        return requestBoookingRepository.findApprovedBookings(idCustomer);
    }
    
    // Metodo per Effettuare il Pagamento di una Richiesta di Prenotazione
    @Transactional
    public void setPayment(String paymentMethod, String cardNumber, String expiryDate, int cvv, String cardHolder, int idBooking) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new RuntimeException("Payment method cannot be null or empty.");
        }

        if (cardNumber == null || !cardNumber.matches("^\\d{4} \\d{4} \\d{4} \\d{4}$")) {
            throw new RuntimeException("Card number must be numeric and follow the format 'XXXX XXXX XXXX XXXX'.");
        }
        
        if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{4}$")) {
            throw new RuntimeException("Expiry date must be in the format MM/YYYY and a valid date.");
        }

        String[] expiryParts = expiryDate.split("/");
        int month = Integer.parseInt(expiryParts[0]);
        int year = Integer.parseInt(expiryParts[1]);
        LocalDate today = LocalDate.now();
        LocalDate expiry = LocalDate.of(year, month, 1).withDayOfMonth(1);
        
        if (expiry.isBefore(today.withDayOfMonth(1))) {
            throw new RuntimeException("Expiry date cannot be in the past.");
        }

        if (String.valueOf(cvv).length() != 3 || cvv <= 0) {
            throw new RuntimeException("CVV must be exactly 3 numeric digits.");
        }

        if (cardHolder == null || !cardHolder.matches("^[a-zA-Z ]+$")) {
            throw new RuntimeException("Cardholder name must only contain letters and spaces.");
        }

        RequestBooking requestBooking = requestBoookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Request booking not found"));

        PaidBooking paidBooking = new PaidBooking();
        paidBooking.setRoomCategory(requestBooking.getRoomCategory());
        paidBooking.setCustomer(requestBooking.getCustomer());
        paidBooking.setReceptionist(requestBooking.getReceptionist()); 
        paidBooking.setStartDate(requestBooking.getStartDate());
        paidBooking.setEndDate(requestBooking.getEndDate());
        paidBooking.setStatus("Paid");
        paidBooking.setTotalPrice(requestBooking.getTotalPrice());

        paidBookingRepository.save(paidBooking);

        requestBoookingRepository.delete(requestBooking);

        Payment payment = new Payment();
        payment.setBooking(paidBooking);
        payment.setPaymentMethod(paymentMethod);
        payment.setCardNumber(cardNumber);
        payment.setExpiryDate(expiryDate);
        payment.setCvv(cvv);
        payment.setCardholderName(cardHolder);

        paymentRepository.save(payment);
    }
    
    // **CASO D'USO N°4 (UC4): VISUALIZZA DETTAGLI DELLA PRENOTAZIONE**
    
    // Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente
    public List<PaidBooking> getPaidBookingsForCustomer(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new RuntimeException("Customer not found for email: " + customerEmail);
        }

        int idCustomer = customer.getIdCustomer();
        return paidBookingRepository.findPaidBookingsForCustomer(idCustomer);
    }

    // **CASO D'USO N°5 (UC5): RICHIEDI MODIFICA DELLA PRENOTAZIONE**

    // Metodo per Richiedere una Modifica di una Prenotazione confermata da un Pagamento Precedente
    @Transactional
    public void requestModifyBooking(int idBooking, String newStartDate, String newEndDate) {
        if (newStartDate == null || newStartDate.trim().isEmpty()) {
            throw new RuntimeException("New start date cannot be null or empty.");
        }
        if (newEndDate == null || newEndDate.trim().isEmpty()) {
            throw new RuntimeException("New end date cannot be null or empty.");
        }
    
        LocalDate startDate = LocalDate.parse(newStartDate);
        LocalDate endDate = LocalDate.parse(newEndDate);
        LocalDate today = LocalDate.now();
    
        if (startDate.isBefore(today)) {
            throw new RuntimeException("New start date cannot be earlier than today.");
        }
    
        if (startDate.isEqual(endDate)) {
            throw new RuntimeException("New start date and end date cannot be the same.");
        }
    
        if (!startDate.isBefore(endDate)) {
            throw new RuntimeException("New start date must be before end date.");
        }
    
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Booking not found."));
                            
        boolean isAvailable = roomCategoryService.isRoomAvailableFromPaidBooking(
            paidBooking.getRoomCategory().getRoomType(),
            newStartDate,
            newEndDate
        );
        
        if (!isAvailable) {
            throw new RuntimeException("No rooms available for the selected dates.");
        }
    
        paidBooking.setOldStartDate(paidBooking.getStartDate());
        paidBooking.setOldEndDate(paidBooking.getEndDate());
    
        paidBooking.setStartDate(startDate);
        paidBooking.setEndDate(endDate);
    
        paidBooking.setStatus("Pending Modification");
    
        paidBookingRepository.save(paidBooking);
    }
    
    // **CASO D'USO N°6 (UC6): MODIFICA DETTAGLI DELLA PRENOTAZIONE**

    // Metodo per Ottenere la Lista delle Richieste di Modifica delle Prenotazioni
    public List<PaidBooking> getModificationRequests() {
        return paidBookingRepository.findPaidBookingsInPendingModification();
    }

    // Metodo per Gestire una Richiesta di Modifica di una Prenotazione confermata da un Pagamento Precedente
    @Transactional
    public void manageRequestModifyBooking(int idBooking, boolean approved) {
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    
        String status = paidBooking.getStatus();
        if (!"Pending Modification".equals(status)) {
            throw new RuntimeException("Booking is not pending modification.");
        }
    
        if (approved) {
            paidBooking.setStatus("Paid");
        } else {
            if (paidBooking.getOldStartDate() != null && paidBooking.getOldEndDate() != null) {
                paidBooking.setStartDate(paidBooking.getOldStartDate());
                paidBooking.setEndDate(paidBooking.getOldEndDate());
            } else {
                throw new RuntimeException("Original booking dates are missing.");
            }
    
            paidBooking.setOldStartDate(null);
            paidBooking.setOldEndDate(null);
    
            paidBooking.setStatus("Paid");
        }
    
        paidBookingRepository.save(paidBooking);
    }
    
    // **CASO D'USO N°7 (UC7): ANNULLA PRENOTAZIONE**

    // Metodo per Annullare una Prenotazione confermata da un Pagamento Precedente
    @Transactional
    public void cancelBookingForCustomer(int idBooking) {
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Booking not found."));
    
        paidBooking.setStatus("Cancelled");
    
        paidBookingRepository.save(paidBooking);
    }
    
    // **CASO D'USO N°8 (UC8): CANCELLA PRENOTAZIONE**

    // Metodo per Visualizzare Lista delle Prenotazioni confermate da un Pagamento Precedente
    public List<PaidBooking> getPaidBookingsForReceptionist() {
        return paidBookingRepository.findPaidBookingsForReceptionists();
    }

    // Metodo per Cancellare una Prenotazione confermata da un Pagamento Precedente   
    @Transactional
    public void cancelBookingForReceptionist(int idBooking) {
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
            .orElseThrow(() -> new RuntimeException("Booking not found."));

        paymentRepository.deleteByIdBooking(idBooking);

        paidBookingRepository.delete(paidBooking);
    }

    // **CASO D'USO N°9 (UC9): CHECK-IN IN DIGITALE**

    // Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-In (MODIFICATA)
    public List<PaidBooking> getPaidBookingsReadyForCheckIn(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new RuntimeException("Customer not found for email: " + customerEmail);
        }

        int idCustomer = customer.getIdCustomer();
        return paidBookingRepository.findPaidBookingsReadyForCheckIn(idCustomer);
    }

    // Metodo per Effettuare il Check-In di una Prenotazione confermata da un Pagamento Precedente
    public void confirmCheckIn(int idBooking) {
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    
        paidBooking.setStatus("Check-In Confirmed");
        paidBookingRepository.save(paidBooking);
    }
     
    // **CASO D'USO N°10 (UC10): CHECK-OUT IN DIGITALE**

    // Metodo per Visualizzare la Lista delle Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-Out (MODIFICATA)
    public List<PaidBooking> getPaidBookingsReadyForCheckOut(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new RuntimeException("Customer not found for email: " + customerEmail);
        }

        int idCustomer = customer.getIdCustomer();
        return paidBookingRepository.findPaidBookingsReadyForCheckOut(idCustomer);
    }

    // Metodo per Effettuare il Check-Out di una Prenotazione confermata da un Pagamento Precedente
    public void confirmCheckOut(int idBooking) {
        PaidBooking paidBooking = paidBookingRepository.findById(idBooking)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        paidBooking.setStatus("Check-Out Confirmed");
        paidBookingRepository.save(paidBooking);
    }

}
