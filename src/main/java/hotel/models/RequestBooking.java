package hotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "REQUEST_BOOKINGS")
public class RequestBooking extends Booking {

    // Costruttore Predefinito
    public RequestBooking() {
        super();
    }

    // Costruttore con Parametri
    public RequestBooking(RoomCategory roomCategory, Customer customer, Receptionist receptionist, LocalDate startDate, LocalDate endDate, 
                       String status, double totalPrice, LocalDate oldStartDate, LocalDate oldEndDate) {
        super(roomCategory, customer, receptionist, startDate, endDate, status, totalPrice);
    }
    
}
