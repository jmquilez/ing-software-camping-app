package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;

import java.util.List;

@Dao
public interface ParcelaReservadaDao {

    @Insert
    void insert(ParcelaReservada parcelaReservada);

    @Query("SELECT * FROM parcela_reservada")
    LiveData<List<ParcelaReservada>> getAllParcelaReservadas();

    @Query("SELECT * FROM parcela_reservada WHERE reservaId = :id")
    LiveData<List<ParcelaReservada>> getAllParcelaReservadasForReserva(int id);

    @Update
    void update(ParcelaReservada parcelaReservada);

    @Delete
    void delete(ParcelaReservada parcelaReservada);

    @Query("DELETE FROM parcela_reservada")
    void deleteAll();

    // New method to get available parcels for a reservation
    @Query("SELECT p.* FROM parcela p WHERE p.nombre NOT IN (SELECT pr.parcelaNombre FROM parcela_reservada pr WHERE pr.reservaId = :reservationId)")
    LiveData<List<Parcela>> getAvailableParcelasForReserva(String reservationId);

    @Query("DELETE FROM parcela_reservada WHERE reservaId = :reservationId")
    void deleteParcelasForReservation(long reservationId);
}
