package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.models.Reserva;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las reservas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ReservaRepository {

    private static final long TIMEOUT = 3000;

    /**
     * DAO para acceder a las operaciones de reservas en la base de datos.
     */
    private final ReservaDao mReservaDao;

    /**
     * LiveData que contiene todas las reservas.
     */
    private LiveData<List<Reserva>> mAllReservas;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private final ExecutorService executorService;

    /**
     * Constructor del repositorio.
     * @param application Contexto de la aplicación para acceder a la base de datos
     */
    public ReservaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mReservaDao = db.reservaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     * La operación se realiza de forma asíncrona pero espera el resultado.
     * @param reserva Reserva a insertar
     * @return ID generado para la nueva reserva, o -1 si ocurre un error
     */
    public long insert(Reserva reserva) {
        Future<Long> future = executorService.submit(() -> mReservaDao.insert(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.e("ReservaRepository", "Insert interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
            return -1L;
        } catch (TimeoutException e) {
            Log.e("ReservaRepository", "Insert timed out: " + e.getMessage());
            return -2L;
        } catch (Exception e) {
            Log.e("ReservaRepository", "Error inserting: " + e.getMessage());
            return -3L;
        }
    }

    /**
     * Obtiene todas las reservas de la base de datos.
     * @return LiveData con la lista de todas las reservas
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mReservaDao.getAllReservas();
    }

    /**
     * Actualiza una reserva existente.
     * La operación se realiza de forma asíncrona.
     * @param reserva Reserva con los datos actualizados
     */
    public Long update(Reserva reserva) {
        Future<Integer> future = executorService.submit(() -> mReservaDao.update(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS).longValue();
        } catch (InterruptedException e) {
            Log.e("ReservaRepository", "Update interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return -1L;
        } catch (TimeoutException e) {
            Log.e("ReservaRepository", "Update timed out: " + e.getMessage());
            return -2L;
        } catch (Exception e) {
            Log.e("ReservaRepository", "Error updating: " + e.getMessage());
            return -3L;
        }
    }

    /**
     * Elimina una reserva de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param reserva Reserva a eliminar
     */
    public Long delete(Reserva reserva) {
        Future<Integer> future = executorService.submit(() -> mReservaDao.delete(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS).longValue();
        } catch (InterruptedException e) {
            Log.e("ReservaRepository", "Delete interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return -1L;
        } catch (TimeoutException e) {
            Log.e("ReservaRepository", "Delete timed out: " + e.getMessage());
            return -2L;
        } catch (Exception e) {
            Log.e("ReservaRepository", "Error deleting: " + e.getMessage());
            return -3L;
        }
    }

    /**
     * Elimina una reserva por su ID.
     * La operación se realiza de forma asíncrona.
     * @param reservationId ID de la reserva a eliminar
     */
    public Long deleteById(long reservationId) {
        Future<Integer> future = executorService.submit(() -> mReservaDao.deleteById(reservationId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS).longValue();
        } catch (InterruptedException e) {
            Log.e("ReservaRepository", "DeleteById interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return -1L;
        } catch (TimeoutException e) {
            Log.e("ReservaRepository", "DeleteById timed out: " + e.getMessage());
            return -2L;
        } catch (Exception e) {
            Log.e("ReservaRepository", "Error deleting by id: " + e.getMessage());
            return -3L;
        }
    }

    /**
     * Elimina todas las reservas de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @return Número de reservas eliminadas
     */
    public Long deleteAll() {
        Future<Integer> future = executorService.submit(() -> mReservaDao.deleteAll());
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS).longValue();
        } catch (InterruptedException e) {
            Log.e("ReservaRepository", "DeleteAll interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return -1L;
        } catch (TimeoutException e) {
            Log.e("ReservaRepository", "DeleteAll timed out: " + e.getMessage());
            return -2L;
        } catch (Exception e) {
            Log.e("ReservaRepository", "Error deleting all: " + e.getMessage());
            return -3L;
        }
    }
}
