package hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;

@Entity
@Table(name = "PAID_BOOKINGS")
public class PaidBooking extends Booking {

    // Attributi
    @Column(name = "old_start_date", nullable = true)
    private LocalDate oldStartDate;

    @Column(name = "old_end_date", nullable = true)
    private LocalDate oldEndDate;

    // Costruttore Predefinito
    public PaidBooking() {
        super();
    }

    // Costruttore con Parametri
    public PaidBooking(RoomCategory roomCategory, Customer customer, Receptionist receptionist, LocalDate startDate, LocalDate endDate, 
                       String status, double totalPrice, LocalDate oldStartDate, LocalDate oldEndDate) {
        super(roomCategory, customer, receptionist, startDate, endDate, status, totalPrice);
        this.oldStartDate = oldStartDate;
        this.oldEndDate = oldEndDate;
    }

    // Getters and Setters
    public LocalDate getOldStartDate() {
        return oldStartDate;
    }

    public void setOldStartDate(LocalDate oldStartDate) {
        this.oldStartDate = oldStartDate;
    }

    public LocalDate getOldEndDate() {
        return oldEndDate;
    }

    public void setOldEndDate(LocalDate oldEndDate) {
        this.oldEndDate = oldEndDate;
    }

    // Getters personalizzati per il JSON
    @Transient
    public String getCustomerName() {
        return getCustomer() != null ? getCustomer().getFullName() : null;
    }

    @Transient
    public String getRoomType() {
        return getRoomCategory() != null ? getRoomCategory().getRoomType() : null;
    }
    
}
