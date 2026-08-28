package hotel.repositories;

import hotel.models.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello Payment.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'PAYMENTS' nel database. 
    */

    // Metodo (query) per Eliminare un Pagamento
    @Modifying
    @Transactional
    @Query("DELETE FROM Payment payment " +
           "WHERE payment.paidBooking.idBooking = :idBooking")
    void deleteByIdBooking(@Param("idBooking") int idBooking);

}
