package hotel.repositories;

import hotel.models.Receptionist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello Receptionist.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'RECEPTIONIST' nel database. 
    */

    // Metodo per Trovare un Receptionist tramite Email
    Receptionist findByEmail(String email);
    
}
