package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las parcelas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la
 * aplicación.
 */
public class ParcelaRepository {

    private static final String TAG = "ParcelaRepository";
    private static final long TIMEOUT = 3000; // 3 seconds in milliseconds
    private static final int MAX_PARCELAS = 100;
    private static final int MAX_NOMBRE_LENGTH = 60;
    private static final int MAX_DESCRIPCION_LENGTH = 300;
    private static final int MIN_MAX_OCUPANTES = 1;
    private static final int MAX_MAX_OCUPANTES = 999;
    private static final double MIN_EUR_POR_PERSONA = 0.0;
    private static final double MAX_EUR_POR_PERSONA = 999.0;

    /**
     * DAO para acceder a las operaciones de parcelas en la base de datos.
     */
    private final ParcelaDao parcelaDao;

    /**
     * DAO para acceder a las operaciones de parcelas reservadas en la base de
     * datos.
     */
    private final ParcelaReservadaDao parcelaReservadaDao;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private final ExecutorService executorService;

    /**
     * Constructor del repositorio.
     * 
     * @param application Contexto de la aplicación para acceder a la base de datos
     */
    public ParcelaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        parcelaDao = db.parcelaDao();
        parcelaReservadaDao = db.parcelaReservadaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta una nueva parcela en la base de datos.
     * La operación se realiza de forma asíncrona.
     * 
     * @param parcela Parcela a insertar
     * @return -1 si hay error, otro valor si éxito
     */
    public long insert(Parcela parcela) {
        if (!isValidParcela(parcela, "Insert")) {
            return -1L;
        }

        // Check if we've reached the maximum number of parcelas
        Future<Integer> countFuture = executorService.submit(() -> parcelaDao.countParcelas());
        try {
            int currentCount = countFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
            if (currentCount >= MAX_PARCELAS) {
                Log.e(TAG, "Insert error: Maximum number of parcelas (" + MAX_PARCELAS + ") reached");
                return -1L;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking parcela count: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return -1L;
        }

        Future<Long> future = executorService.submit(() -> parcelaDao.insert(parcela));
        return handleFutureResult(future, "Insert");
    }

    /**
     * Obtiene todas las parcelas de la base de datos.
     * 
     * @return LiveData con la lista de todas las parcelas
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return parcelaDao.getAllParcelas();
    }

    /**
     * Actualiza una parcela existente.
     * La operación se realiza de forma asíncrona.
     * 
     * @param parcela Parcela con los datos actualizados
     * @return -1 si hay error, otro valor si éxito
     */
    public Long update(Parcela parcela) {
        if (!isValidParcela(parcela, "Update")) {
            return -1L;
        }
        Future<Integer> future = executorService.submit(() -> parcelaDao.update(parcela));
        return handleFutureResult(future, "Update");
    }

    /**
     * Elimina una parcela de la base de datos.
     * La operación se realiza de forma asíncrona.
     * 
     * @param parcela Parcela a eliminar
     */
    public Long delete(Parcela parcela) {
        Future<Integer> future = executorService.submit(() -> parcelaDao.delete(parcela));
        return handleFutureResult(future, "Delete");
    }

    /**
     * Obtiene las parcelas que no están reservadas.
     * 
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return parcelaDao.getAvailableParcelas();
    }

    /**
     * Obtiene las parcelas asociadas a una reserva específica junto con su
     * ocupación.
     * 
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas y su ocupación
     */
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return parcelaDao.getParcelasByReservationId(reservationId);
    }

    /**
     * Obtiene las parcelas que no están vinculadas a una reserva específica.
     * 
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas no vinculadas
     */
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return parcelaDao.getParcelasNotLinkedToReservation(reservationId);
    }

    /**
     * Elimina una parcela por su nombre.
     * La operación se realiza de forma asíncrona.
     * 
     * @param nombre Nombre de la parcela a eliminar
     */
    public Long deleteByNombre(String nombre) {
        Future<Integer> future = executorService.submit(() -> parcelaDao.deleteByNombre(nombre));
        return handleFutureResult(future, "DeleteByNombre");
    }

    public boolean exists(String nombre) {
        Future<Boolean> future = executorService.submit(() -> parcelaDao.exists(nombre));
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

    /**
     * Actualiza una parcela cambiando su nombre.
     * Esta operación es atómica y actualiza tanto la parcela como sus referencias
     * en reservas.
     * 
     * @param oldName        Nombre actual de la parcela
     * @param updatedParcela Parcela con los datos actualizados (incluyendo el nuevo
     *                       nombre)
     * @throws RuntimeException si hay un error durante la transacción
     */
    public void updateWithNameChange(String oldName, Parcela updatedParcela) {
        executorService.execute(() -> {
            parcelaDao.runInTransaction(() -> {
                try {
                    parcelaDao.insert(updatedParcela);
                    parcelaReservadaDao.updateParcelaNombre(oldName, updatedParcela.getNombre());
                    parcelaDao.deleteByNombre(oldName);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating parcela name: " + e.getMessage());
                    throw e;
                }
            });
        });
    }

    /**
     * Valida los datos de una parcela.
     * 
     * @param parcela   Parcela a validar
     * @param operation Nombre de la operación para el log
     * @return true si la parcela es válida, false en caso contrario
     */
    private boolean isValidParcela(Parcela parcela, String operation) {
        if (parcela == null) {
            Log.e(TAG, operation + " error: Parcela cannot be a null object reference");
            return false;
        } else if (parcela.getNombre() != null && parcela.getNombre().isEmpty()) {
            Log.e(TAG, operation + " error: Parcela name cannot be empty");
            return false;
        } else if (parcela.getNombre() != null && parcela.getNombre().length() > MAX_NOMBRE_LENGTH) {
            Log.e(TAG, operation + " error: Parcela name cannot be longer than " + MAX_NOMBRE_LENGTH + " characters");
            return false;
        } else if (parcela.getDescripcion() != null && parcela.getDescripcion().length() > MAX_DESCRIPCION_LENGTH) {
            Log.e(TAG, operation + " error: Parcela description cannot be longer than " + MAX_DESCRIPCION_LENGTH + " characters");
            return false;
        } else if (parcela.getMaxOcupantes() != null && 
                (parcela.getMaxOcupantes() < MIN_MAX_OCUPANTES || parcela.getMaxOcupantes() > MAX_MAX_OCUPANTES)) {
            Log.e(TAG, operation + " error: Maximum occupants must be between " + MIN_MAX_OCUPANTES + " and " + MAX_MAX_OCUPANTES);
            return false;
        } else if (parcela.getEurPorPersona() != null && 
                (parcela.getEurPorPersona() <= MIN_EUR_POR_PERSONA || parcela.getEurPorPersona() > MAX_EUR_POR_PERSONA)) {
            Log.e(TAG, operation + " error: Price per person must be between " + MIN_EUR_POR_PERSONA + " (excluded) and " + MAX_EUR_POR_PERSONA + " (included)");
            return false;
        }
        return true;
    }

    private <T> Long handleFutureResult(Future<T> future, String operation) {
        try {
            return ((Number) future.get(TIMEOUT, TimeUnit.MILLISECONDS)).longValue();
        } catch (Exception e) {
            Log.e(TAG, operation + " error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return -1L;
        }
    }

    /**
     * Obtiene el número total de parcelas.
     * @return número de parcelas en la base de datos
     */
    public int getParcelasCount() {
        Future<Integer> future = executorService.submit(() -> parcelaDao.countParcelas());
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Error getting parcelas count: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return -1;
        }
    }
}
