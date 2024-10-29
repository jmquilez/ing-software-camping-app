package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Reserva;

@Dao
public interface ReservaDao {

    @Insert
    long insert(Reserva reserva);

    @Query("SELECT * FROM reserva")
    LiveData<List<Reserva>> getAllReservas();

    @Update
    void update(Reserva reserva);

    @Delete
    void delete(Reserva reserva);

    @Query("DELETE FROM reserva")
    void deleteAll();

    @Query("DELETE FROM reserva WHERE id = :reservationId")
    void deleteById(long reservationId);
}
