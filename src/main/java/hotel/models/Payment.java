package hotel.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENTS")
public class Payment {

    // Attributi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private int idPayment;

    @ManyToOne
    @JoinColumn(name = "id_booking", nullable = false)
    private PaidBooking paidBooking;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @Column(name = "cvv", nullable = false)
    private int cvv;

    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Costruttore Predefinito (necessario per JPA)
    public Payment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Costruttore con Parametri
    public Payment(PaidBooking paidBooking, String paymentMethod, String cardNumber, String expiryDate, int cvv, String cardholderName) {
        this.paidBooking = paidBooking;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardholderName = cardholderName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters e Setters
    public int getIdPayment() { 
        return idPayment; 
    }

    public void setIdPayment(int idPayment) { 
        this.idPayment = idPayment; 
    }

    public PaidBooking getPaidBooking() { 
        return paidBooking; 
    }

    public void setBooking(PaidBooking paidBooking) { 
        this.paidBooking = paidBooking; 
    }

    public String getPaymentMethod() { 
        return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }

    public String getCardNumber() { 
        return cardNumber; 
    }

    public void setCardNumber(String cardNumber) { 
        this.cardNumber = cardNumber; 
    }

    public String getExpiryDate() { 
        return expiryDate; 
    }

    public void setExpiryDate(String expiryDate) { 
        this.expiryDate = expiryDate; 
    }

    public int getCvv() { 
        return cvv; 
    }

    public void setCvv(int cvv) { 
        this.cvv = cvv; 
    }

    public String getCardholderName() { 
        return cardholderName; 
    }
    
    public void setCardholderName(String cardholderName) { 
        this.cardholderName = cardholderName; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }

    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
    
}
