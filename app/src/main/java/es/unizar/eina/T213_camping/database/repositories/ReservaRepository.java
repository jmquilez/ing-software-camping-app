package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.models.Reserva;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las reservas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ReservaRepository {

    private static final long TIMEOUT = 3000;
    private static final String TAG = "ReservaRepository";

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
        if (!isValidReserva(reserva, "Insert")) {
            return -1L;
        }
        Future<Long> future = executorService.submit(() -> mReservaDao.insert(reserva));
        return handleFutureResult(future, "Insert");
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
        if (!isValidReserva(reserva, "Update")) {
            return -1L;
        }
        Future<Integer> future = executorService.submit(() -> mReservaDao.update(reserva));
        return handleFutureResult(future, "Update");
    }

    /**
     * Elimina una reserva de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param reserva Reserva a eliminar
     */
    public Long delete(Reserva reserva) {
        Future<Integer> future = executorService.submit(() -> mReservaDao.delete(reserva));
        return handleFutureResult(future, "Delete");
    }

    /**
     * Elimina una reserva por su ID.
     * La operación se realiza de forma asíncrona.
     * @param reservationId ID de la reserva a eliminar
     */
    public Long deleteById(long reservationId) {
        Future<Integer> future = executorService.submit(() -> mReservaDao.deleteById(reservationId));
        return handleFutureResult(future, "DeleteById");
    }

    /**
     * Elimina todas las reservas de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @return Número de reservas eliminadas
     */
    public Long deleteAll() {
        Future<Integer> future = executorService.submit(() -> mReservaDao.deleteAll());
        return handleFutureResult(future, "DeleteAll");
    }

    /**
     * Verifica si existe una reserva con el ID especificado.
     * @param reservaId ID de la reserva a verificar
     * @return true si existe la reserva, false en caso contrario
     */
    public boolean exists(long reservaId) {
        Future<Boolean> future = executorService.submit(() -> mReservaDao.exists(reservaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error in exists: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    private <T> Long handleFutureResult(Future<T> future, String operation) {
        try {
            return ((Number) future.get(TIMEOUT, TimeUnit.MILLISECONDS)).longValue();
        } catch (Exception e) {
            Log.e("ReservaRepository", operation + " error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return -1L;
        }
    }

    /**
     * Valida los datos de una reserva.
     * @param reserva Reserva a validar
     * @param operation Nombre de la operación para el log
     * @return true si la reserva es válida, false en caso contrario
     */
    private boolean isValidReserva(Reserva reserva, String operation) {
        if (reserva == null) {
            Log.e(TAG, operation + " error: Reserva cannot be a null object reference");
            return false;
        }
        
        if (isNotValidDateRange(reserva)) {
            Log.e(TAG, operation + " error: Entry date must be before departure date");
            return false;
        }
        
        if (reserva.getNombreCliente() != null && reserva.getNombreCliente().isEmpty()) {
            Log.e(TAG, operation + " error: Client name cannot be empty");
            return false;
        }
        
        if (reserva.getTelefonoCliente() != null && 
            !reserva.getTelefonoCliente().matches("\\d{9}")) {
            Log.e(TAG, operation + " error: Phone number must be exactly 9 digits");
            return false;
        }
        
        if (reserva.getPrecio() != null && reserva.getPrecio() <= 0) {
            Log.e(TAG, operation + " error: Price must be greater than zero");
            return false;
        }
        
        return true;
    }

    private boolean isNotValidDateRange(Reserva reserva) {
        return reserva.getFechaEntrada() == null ||
                reserva.getFechaSalida() == null ||
                !reserva.getFechaEntrada().before(reserva.getFechaSalida());
    }
}
