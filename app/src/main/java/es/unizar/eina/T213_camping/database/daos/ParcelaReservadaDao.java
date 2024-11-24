package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.converters.DateConverter;

import java.util.Date;
import java.util.List;

/** 
 * Definición del Data Access Object para las parcelas reservadas.
 * Gestiona la relación entre parcelas y reservas.
 */
@Dao
@TypeConverters(DateConverter.class)
public interface ParcelaReservadaDao {

    /** 
     * Inserta una nueva relación parcela-reserva.
     * @param parcelaReservada ParcelaReservada a insertar
     */
    @Insert
    void insert(ParcelaReservada parcelaReservada);

    /** 
     * Obtiene todas las relaciones parcela-reserva.
     * @return LiveData con la lista de todas las parcelas reservadas
     */
    @Query("SELECT * FROM parcela_reservada")
    LiveData<List<ParcelaReservada>> getAllParcelaReservadas();

    /** 
     * Obtiene todas las parcelas reservadas para una reserva específica.
     * @param id ID de la reserva
     * @return LiveData con la lista de parcelas reservadas para esa reserva
     */
    @Query("SELECT * FROM parcela_reservada WHERE reservaId = :id")
    LiveData<List<ParcelaReservada>> getAllParcelaReservadasForReserva(int id);

    /** 
     * Actualiza una relación parcela-reserva existente.
     * @param parcelaReservada ParcelaReservada con los datos actualizados
     */
    @Update
    void update(ParcelaReservada parcelaReservada);

    /** 
     * Elimina una relación parcela-reserva.
     * @param parcelaReservada ParcelaReservada a eliminar
     */
    @Delete
    void delete(ParcelaReservada parcelaReservada);

    /** 
     * Elimina todas las relaciones parcela-reserva.
     */
    @Query("DELETE FROM parcela_reservada")
    void deleteAll();

    /** 
     * Obtiene las parcelas disponibles para una reserva específica.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas disponibles
     */
    @Query("SELECT p.* FROM parcela p WHERE p.nombre NOT IN (SELECT pr.parcelaNombre FROM parcela_reservada pr WHERE pr.reservaId = :reservationId)")
    LiveData<List<Parcela>> getAvailableParcelasForReserva(String reservationId);

    /** 
     * Elimina todas las parcelas asociadas a una reserva.
     * @param reservationId ID de la reserva
     */
    @Query("DELETE FROM parcela_reservada WHERE reservaId = :reservationId")
    void deleteParcelasForReservation(long reservationId);

    @Query("SELECT p.* FROM parcela p " +
           "WHERE p.nombre NOT IN " +
           "(SELECT pr.parcelaNombre FROM parcela_reservada pr " +
           "JOIN reserva r ON pr.reservaId = r.id " +
           "WHERE r.fechaEntrada < :fechaFin " +
           "AND r.fechaSalida > :fechaInicio)")
    LiveData<List<Parcela>> getParcelasDisponiblesEnIntervalo(Date fechaInicio, Date fechaFin);
}
