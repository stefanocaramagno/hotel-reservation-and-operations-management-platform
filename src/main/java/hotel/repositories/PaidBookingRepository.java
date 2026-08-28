package hotel.repositories;

import hotel.models.PaidBooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface PaidBookingRepository extends JpaRepository<PaidBooking, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello PaidBooking.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'PAID_BOOKINGS' nel database. 
    */
    
    // Metodo (query) per Trovare tutte le Prenotazioni confermate da un Pagamento Precedente   
    @Query("SELECT paidBooking " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.customer.idCustomer = :idCustomer " +
              "AND paidBooking.status = 'Paid'")
    List<PaidBooking> findPaidBookingsForCustomer(@Param("idCustomer") int idCustomer);

    // Metodo (query) per Trovare tutte le Prenotazioni confermate da un Pagamento Precedente   
    @Query("SELECT paidBooking " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.status = 'Paid'")
    List<PaidBooking> findPaidBookingsForReceptionists();

    // Metodo (query) per Contare le Prenotazione Pagate di una Stanza in un determinato Intervallo di Date
    @Query("SELECT COUNT(paidBooking) " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.roomCategory.idRoomCategory = :idRoomCategory " +
                "AND paidBooking.startDate <= :endDate " +
                "AND paidBooking.endDate >= :startDate " +
                "AND paidBooking.status != 'Cancelled'")
    long countPaidBookedRooms(@Param("idRoomCategory") int idRoomCategory, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Metodo (query) per Trovare tutte le Prenotazioni confermate da un Pagamento Precedente e in attesa di Modifica
    @Query("SELECT paidBooking " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.status = 'Pending Modification'")
    List<PaidBooking> findPaidBookingsInPendingModification();

    // Metodo (query) per Trovare tutte le Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-In
    @Query("SELECT paidBooking " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.customer.idCustomer = :idCustomer " +
                "AND paidBooking.startDate = CURRENT_DATE " +
                "AND paidBooking.status = 'Paid'")
    List<PaidBooking> findPaidBookingsReadyForCheckIn(@Param("idCustomer") int idCustomer);

    // Metodo (query) per Trovare tutte le Prenotazioni confermate da un Pagamento Precedente e abilitate al Check-Out
    @Query("SELECT paidBooking " +
           "FROM PaidBooking paidBooking " +
           "WHERE paidBooking.customer.idCustomer = :idCustomer " +
                "AND paidBooking.endDate = CURRENT_DATE " +
                "AND paidBooking.status = 'Check-In Confirmed'")
    List<PaidBooking> findPaidBookingsReadyForCheckOut(@Param("idCustomer") int idCustomer);

}
