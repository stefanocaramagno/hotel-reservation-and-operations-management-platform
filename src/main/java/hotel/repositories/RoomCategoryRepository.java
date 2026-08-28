package hotel.repositories;

import hotel.models.RoomCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello Room.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'ROOMS' nel database. 
    */

    // Metodo per Trovare una Stanza in base al Tipo
    RoomCategory findByRoomType(String roomType);
    
}
