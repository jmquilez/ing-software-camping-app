package es.unizar.eina.T213_camping.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import es.unizar.eina.T213_camping.db.models.Parcela;
import es.unizar.eina.T213_camping.db.models.ParcelaOccupancy; // Import the new class

import java.util.List;

@Dao
public interface ParcelaDao {

    @Insert
    void insert(Parcela parcela);

    @Query("SELECT * FROM parcela")
    LiveData<List<Parcela>> getAllParcelas();

    @Update
    void update(Parcela parcela);

    @Delete
    void delete(Parcela parcela);

    @Query("DELETE FROM parcela")
    void deleteAll();

    // New method to get available parcelas
    @Query("SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada)")
    LiveData<List<Parcela>> getAvailableParcelas();

    // Updated method to get parcelas associated with a specific reservationId
    @Query("SELECT p.*, pr.numOcupantes FROM parcela p " +
           "INNER JOIN parcela_reservada pr ON p.nombre = pr.parcelaNombre " +
           "WHERE pr.reservaId = :reservationId")
    LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId);

    // New method to get parcelas not linked to a specific reservation ID
    @Query("SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada WHERE reservaId = :reservationId)")
    LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId);

    // New method to delete a Parcela by its nombre
    @Query("DELETE FROM parcela WHERE nombre = :nombre")
    void deleteByNombre(String nombre);
}
