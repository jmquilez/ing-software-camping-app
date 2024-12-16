package es.unizar.eina.T213_camping.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.utils.ModelUtils.ParcelaOccupancy;

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
    long insert(Parcela parcela);

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
    int update(Parcela parcela);

    /** 
     * Elimina una parcela de la base de datos.
     * @param parcela Parcela a eliminar
     */
    @Delete
    int delete(Parcela parcela);

    /** 
     * Elimina todas las parcelas de la base de datos.
     */
    @Query("DELETE FROM parcela")
    int deleteAll();

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
    int deleteByNombre(String nombre);

    /** 
     * Verifica si una parcela con el nombre dado existe en la base de datos.
     * @param nombre Nombre de la parcela a verificar
     * @return true si la parcela existe, false en caso contrario
     */
    @Query("SELECT EXISTS(SELECT 1 FROM parcela WHERE nombre = :nombre LIMIT 1)")
    boolean exists(String nombre);

    /** 
     * Obtiene una parcela por su nombre.
     * @param nombre Nombre de la parcela a obtener
     * @return Parcela correspondiente al nombre dado
     */
    @Query("SELECT * FROM parcela WHERE nombre = :nombre LIMIT 1")
    Parcela getByNombre(String nombre);

    /** 
     * Cuenta el número total de parcelas en la base de datos.
     * @return número total de parcelas
     */
    @Query("SELECT COUNT(*) FROM parcela")
    int countParcelas();

    @Transaction
    default void runInTransaction(Runnable action) {
        action.run();
    }

    /** 
     * Elimina todas las parcelas de la base de datos que tienen un nombre que comienza con el prefijo dado.
     * @param prefix Prefijo de los nombres de las parcelas a eliminar
     * @return Número de parcelas eliminadas
     */
    @Query("DELETE FROM parcela WHERE nombre LIKE :prefix || '%'")
    int deleteParcelasWithPrefix(String prefix);
}
