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
    long insert(ParcelaReservada parcelaReservada);

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
    int update(ParcelaReservada parcelaReservada);

    /** 
     * Elimina una relación parcela-reserva.
     * @param parcelaReservada ParcelaReservada a eliminar
     */
    @Delete
    int delete(ParcelaReservada parcelaReservada);

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
    int deleteParcelasForReservation(long reservationId);

    /** 
     * Obtiene las parcelas disponibles en un intervalo de fechas.
     * @param fechaInicio Fecha de inicio del intervalo
     * @param fechaFin Fecha de fin del intervalo
     * @return LiveData con la lista de parcelas disponibles
     */
    @Query("SELECT p.* FROM parcela p " +
           "WHERE p.nombre NOT IN " +
           "(SELECT pr.parcelaNombre FROM parcela_reservada pr " +
           "JOIN reserva r ON pr.reservaId = r.id " +
           "WHERE r.fechaEntrada < :fechaFin " +
           "AND r.fechaSalida > :fechaInicio)")
    LiveData<List<Parcela>> getParcelasDisponiblesEnIntervalo(Date fechaInicio, Date fechaFin);

    /** 
     * Obtiene las parcelas disponibles en un intervalo de fechas excluyendo una reserva específica.
     * @param startDate Fecha de inicio del intervalo (inclusive)
     * @param endDate Fecha de fin del intervalo (exclusive)
     * @param excludeReservationId ID de la reserva a excluir
     * @return LiveData con la lista de parcelas disponibles
     */
    @Query("SELECT p.* FROM parcela p " +
           "WHERE p.nombre NOT IN " +
           "(SELECT pr.parcelaNombre FROM parcela_reservada pr " +
           "JOIN reserva r ON pr.reservaId = r.id " +
           "WHERE pr.reservaId != :excludeReservationId " +
           "AND r.fechaEntrada < :endDate " +
           "AND r.fechaSalida > :startDate)")
    LiveData<List<Parcela>> getParcelasDisponiblesEnIntervaloExcludingReservation(Date startDate, Date endDate, long excludeReservationId);

    /** 
     * Actualiza el nombre de una parcela en todas las relaciones parcela-reserva.
     * @param oldName Nombre actual de la parcela
     * @param newName Nuevo nombre de la parcela
     */
    @Query("UPDATE parcela_reservada SET parcelaNombre = :newName WHERE parcelaNombre = :oldName")
    int updateParcelaNombre(String oldName, String newName);

    /**
     * Verifica si existe una relación entre una parcela y una reserva.
     * @param parcelaNombre Nombre de la parcela a verificar
     * @param reservaId ID de la reserva a verificar
     * @return true si existe la relación, false en caso contrario
     */
    @Query("SELECT EXISTS(SELECT 1 FROM parcela_reservada WHERE parcelaNombre = :parcelaNombre AND reservaId = :reservaId)")
    boolean exists(String parcelaNombre, long reservaId);

    @Transaction
    default boolean updateParcelasForReservation(long reservationId, List<ParcelaReservada> updatedParcels) {
        try {
            deleteParcelasForReservation(reservationId);
            for (ParcelaReservada parcel : updatedParcels) {
                if (insert(parcel) == -1) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
