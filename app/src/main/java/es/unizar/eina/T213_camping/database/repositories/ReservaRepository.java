package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.models.Reserva;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las reservas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ReservaRepository {

    /**
     * DAO para acceder a las operaciones de reservas en la base de datos.
     */
    private ReservaDao mReservaDao;

    /**
     * LiveData que contiene todas las reservas.
     */
    private LiveData<List<Reserva>> mAllReservas;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private ExecutorService executorService;

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
        Future<Long> future = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mReservaDao.insert(reserva);
            }
        });

        try {
            return future.get(); // Espera el resultado
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
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
    public void update(Reserva reserva) {
        executorService.execute(() -> mReservaDao.update(reserva));
    }

    /**
     * Elimina una reserva de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param reserva Reserva a eliminar
     */
    public void delete(Reserva reserva) {
        executorService.execute(() -> mReservaDao.delete(reserva));
    }

    /**
     * Elimina una reserva por su ID.
     * La operación se realiza de forma asíncrona.
     * @param reservationId ID de la reserva a eliminar
     */
    public void deleteById(long reservationId) {
        executorService.execute(() -> mReservaDao.deleteById(reservationId));
    }
}
