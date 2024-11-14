package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

import java.util.List;

/** 
 * Definición del Data Access Object para las parcelas.
 * Proporciona métodos para realizar operaciones CRUD sobre la tabla de parcelas.
 */
@Dao
public interface ParcelaDao {

    /** 
     * Inserta una nueva parcela en la base de datos.
     * @param parcela Parcela a insertar
     */
    @Insert
    void insert(Parcela parcela);

    /** 
     * Obtiene todas las parcelas ordenadas por defecto.
     * @return LiveData con la lista de todas las parcelas
     */
    @Query("SELECT * FROM parcela")
    LiveData<List<Parcela>> getAllParcelas();

    /** 
     * Actualiza una parcela existente.
     * @param parcela Parcela con los datos actualizados
     */
    @Update
    void update(Parcela parcela);

    /** 
     * Elimina una parcela de la base de datos.
     * @param parcela Parcela a eliminar
     */
    @Delete
    void delete(Parcela parcela);

    /** 
     * Elimina todas las parcelas de la base de datos.
     */
    @Query("DELETE FROM parcela")
    void deleteAll();

    /** 
     * Obtiene las parcelas que no están reservadas.
     * @return LiveData con la lista de parcelas disponibles
     */
    @Query("SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada)")
    LiveData<List<Parcela>> getAvailableParcelas();

    /** 
     * Obtiene las parcelas asociadas a una reserva específica junto con su ocupación.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas y su ocupación
     */
    @Query("SELECT p.*, pr.numOcupantes FROM parcela p " +
           "INNER JOIN parcela_reservada pr ON p.nombre = pr.parcelaNombre " +
           "WHERE pr.reservaId = :reservationId")
    LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId);

    /** 
     * Obtiene las parcelas que no están vinculadas a una reserva específica.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas no vinculadas
     */
    @Query("SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada WHERE reservaId = :reservationId)")
    LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId);

    /** 
     * Elimina una parcela por su nombre.
     * @param nombre Nombre de la parcela a eliminar
     */
    @Query("DELETE FROM parcela WHERE nombre = :nombre")
    void deleteByNombre(String nombre);
}
