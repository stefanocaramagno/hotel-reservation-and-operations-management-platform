package hotel.repositories;

import hotel.models.CleaningStaff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningStaffRepository extends JpaRepository<CleaningStaff, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello CleaningStaff.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'CLEANING_STAFF' nel database. 
    */

    // Metodo per Trovare un Cleaning Staff tramite Email
    CleaningStaff findByEmail(String email);
    
}
