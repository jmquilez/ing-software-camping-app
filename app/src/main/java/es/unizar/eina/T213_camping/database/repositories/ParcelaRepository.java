package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las parcelas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ParcelaRepository {

    /**
     * DAO para acceder a las operaciones de parcelas en la base de datos.
     */
    private ParcelaDao parcelaDao;

    /**
     * DAO para acceder a las operaciones de parcelas reservadas en la base de datos.
     */
    private ParcelaReservadaDao parcelaReservadaDao;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private ExecutorService executorService;

    /**
     * Constructor del repositorio.
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
     * @param parcela Parcela a insertar
     */
    public long insert(Parcela parcela) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> parcelaDao.insert(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.d("ParcelaRepository", e.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Obtiene todas las parcelas de la base de datos.
     * @return LiveData con la lista de todas las parcelas
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return parcelaDao.getAllParcelas();
    }

    /**
     * Actualiza una parcela existente.
     * La operación se realiza de forma asíncrona.
     * @param parcela Parcela con los datos actualizados
     */
    public long update(Parcela parcela) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> parcelaDao.update(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.d("ParcelaRepository", e.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Elimina una parcela de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param parcela Parcela a eliminar
     */
    public long delete(Parcela parcela) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> parcelaDao.delete(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.d("ParcelaRepository", e.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Obtiene las parcelas que no están reservadas.
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return parcelaDao.getAvailableParcelas();
    }

    /**
     * Obtiene las parcelas asociadas a una reserva específica junto con su ocupación.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas y su ocupación
     */
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return parcelaDao.getParcelasByReservationId(reservationId);
    }

    /**
     * Obtiene las parcelas que no están vinculadas a una reserva específica.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas no vinculadas
     */
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return parcelaDao.getParcelasNotLinkedToReservation(reservationId);
    }

    /**
     * Elimina una parcela por su nombre.
     * La operación se realiza de forma asíncrona.
     * @param nombre Nombre de la parcela a eliminar
     */
    public void deleteByNombre(String nombre) {
        executorService.execute(() -> parcelaDao.deleteByNombre(nombre));
    }

    public boolean exists(String nombre) {
        try {
            return executorService.submit(() -> parcelaDao.exists(nombre)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateWithNameChange(String oldName, Parcela updatedParcela) {
        executorService.execute(() -> {
            // Start transaction to ensure atomicity
            parcelaDao.runInTransaction(() -> {
                try {
                    // 1. Insert new parcela with new name
                    parcelaDao.insert(updatedParcela);

                    // 2. Update all ParcelaReservada references to point to new name
                    parcelaReservadaDao.updateParcelaNombre(oldName, updatedParcela.getNombre());

                    // 3. Delete old parcela record
                    parcelaDao.deleteByNombre(oldName);
                } catch (Exception e) {
                    // If anything fails, the transaction will be rolled back
                    Log.e("ParcelaRepository", "Error updating parcela name: " + e.getMessage());
                    throw e;
                }
            });
        });
    }
}
