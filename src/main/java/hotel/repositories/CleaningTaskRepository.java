package hotel.repositories;

import hotel.models.CleaningTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CleaningTaskRepository extends JpaRepository<CleaningTask, Integer> {

    /**
     * Questa repository viene utilizzata per gestire l'accesso ai dati
     * relativi al modello CleaningTask.
     * Extendendo JpaRepository, forniamo un insieme di metodi 
     * predefiniti (ad esempio, save, findById, findAll, delete) per operare
     * sulla tabella 'CLEANING_TASKS' nel database. 
    */

    // Metodo (query) per Trovare tutte le Task di Pulizia da effettuare
    @Query("SELECT cleaningTask " +
           "FROM CleaningTask cleaningTask " +
           "WHERE cleaningTask.cleaningStaff.idCleaningStaff = :idCleaningStaff " +
                "AND cleaningTask.status = 'Assigned'")
    List<CleaningTask> findCleaningTasks(@Param("idCleaningStaff") int idCleaningStaff);

}
