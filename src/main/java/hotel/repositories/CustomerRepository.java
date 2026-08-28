package hotel.repositories;

import hotel.models.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello Customer.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'CUSTOMER' nel database. 
    */

    // Metodo per Trovare un Customer tramite Email
    Customer findByEmail(String email);
    
}
