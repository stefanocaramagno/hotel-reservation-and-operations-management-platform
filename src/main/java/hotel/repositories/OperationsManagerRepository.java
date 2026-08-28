package hotel.repositories;

import hotel.models.OperationsManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationsManagerRepository extends JpaRepository<OperationsManager, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello OperationsManager.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'OPERATIONS_MANAGER' nel database. 
    */

    // Metodo per Trovare un Operations Manager tramite Email
    OperationsManager findByEmail(String email);
    
}
