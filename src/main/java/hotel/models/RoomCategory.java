package hotel.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ROOM_CATEGORIES")
public class RoomCategory {

    // Attributi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_category")
    private int idRoomCategory;

    @Column(name = "room_types", nullable = false)
    private String roomType;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "price_for_night", nullable = false)
    private double priceForNight;

    @Column(name = "accessories", nullable = false)
    private String accessories;

    @Column(name = "room_amount", nullable = false)
    private int roomAmount;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Costruttore Predefinito (obbligatorio per JPA)
    public RoomCategory() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Costruttore con Parametri
    public RoomCategory(int idRoomCategory, String roomType, int capacity, double priceForNight, String accessories, int roomAmount) {
        this.idRoomCategory = idRoomCategory;
        this.roomType = roomType;
        this.capacity = capacity;
        this.priceForNight = priceForNight;
        this.accessories = accessories;
        this.roomAmount = roomAmount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getIdRoomCategory() {
        return idRoomCategory;
    }

    public void setIdRoomCategory(int idRoomCategory) {
        this.idRoomCategory = idRoomCategory;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPriceForNight() {
        return priceForNight;
    }

    public void setPriceForNight(double priceForNight) {
        this.priceForNight = priceForNight;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public int getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(int roomAmount) {
        this.roomAmount = roomAmount;
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
