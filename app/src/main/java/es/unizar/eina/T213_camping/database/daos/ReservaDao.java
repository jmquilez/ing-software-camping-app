package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Reserva;

/** 
 * Definición del Data Access Object para las reservas.
 * Proporciona métodos para realizar operaciones CRUD sobre la tabla de reservas.
 */
@Dao
public interface ReservaDao {

    /** 
     * Inserta una nueva reserva en la base de datos.
     * @param reserva Reserva a insertar
     * @return ID generado para la nueva reserva
     */
    @Insert
    long insert(Reserva reserva);

    /** 
     * Obtiene todas las reservas ordenadas por defecto.
     * @return LiveData con la lista de todas las reservas
     */
    @Query("SELECT * FROM reserva")
    LiveData<List<Reserva>> getAllReservas();

    /** 
     * Actualiza una reserva existente.
     * @param reserva Reserva con los datos actualizados
     */
    @Update
    int update(Reserva reserva);

    /** 
     * Elimina una reserva de la base de datos.
     * @param reserva Reserva a eliminar
     */
    @Delete
    int delete(Reserva reserva);

    /** 
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM reserva")
    int deleteAll();

    /** 
     * Elimina una reserva por su ID.
     * @param reservationId ID de la reserva a eliminar
     */
    @Query("DELETE FROM reserva WHERE id = :reservationId")
    int deleteById(long reservationId);

    /** 
     * Verifica si existe una reserva con el ID especificado.
     * @param reservaId ID de la reserva a verificar
     * @return true si existe la reserva, false en caso contrario
     */
    @Query("SELECT EXISTS(SELECT 1 FROM reserva WHERE id = :reservaId)")
    boolean exists(long reservaId);

    /** 
     * Cuenta el número total de reservas en la base de datos.
     * @return número total de reservas
     */
    @Query("SELECT COUNT(*) FROM reserva")
    int countReservas();

    /** 
     * Elimina todas las reservas de la base de datos que tienen un nombreCliente que comienza con el prefijo especificado.
     * @param prefix Prefijo del nombreCliente de la reserva a eliminar
     * @return Número de reservas eliminadas
     */
    @Query("DELETE FROM reserva WHERE nombreCliente IN (SELECT nombreCliente FROM reserva WHERE CAST(nombreCliente AS TEXT) LIKE :prefix || '%')")
    int deleteReservasWithClientNamePrefix(String prefix);
}
