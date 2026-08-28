package hotel.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CLEANING_TASKS")
public class CleaningTask {

    // Attributi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cleaning_task")
    private int idCleaningTask;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id_cleaning_staff", nullable = false)
    private CleaningStaff cleaningStaff;

    @ManyToOne
    @JoinColumn(name = "id_operations_manager", nullable = false)
    private OperationsManager operationsManager;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false)
    private String status = "Assigned";

    // Costruttore Predefinito (necessario per JPA)
    public CleaningTask() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Costruttore con Parametri
    public CleaningTask(Room room, CleaningStaff cleaningStaff, String status) {
        this.room = room;
        this.cleaningStaff = cleaningStaff;
        this.status = status != null ? status : "Assigned"; 
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getIdCleaningTask() {
        return idCleaningTask;
    }

    public void setIdCleaningTask(int idCleaningTask) {
        this.idCleaningTask = idCleaningTask;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public CleaningStaff getCleaningStaff() {
        return cleaningStaff;
    }

    public void setCleaningStaff(CleaningStaff cleaningStaff) {
        this.cleaningStaff = cleaningStaff;
    }

    public OperationsManager getOperationsManager() {
        return operationsManager;
    }

    public void setOperationsManager(OperationsManager operationsManager) {
        this.operationsManager = operationsManager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
