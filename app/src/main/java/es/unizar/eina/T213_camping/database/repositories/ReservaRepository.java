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

public class ReservaRepository {

    private ReservaDao mReservaDao;
    private LiveData<List<Reserva>> mAllReservas;
    private ExecutorService executorService;

    public ReservaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mReservaDao = db.reservaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert a new reserva
    public long insert(Reserva reserva) {
        Future<Long> future = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mReservaDao.insert(reserva);
            }
        });

        try {
            return future.get(); // This will block until the result is available
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1; // Or handle the error as appropriate for your application
        }
    }

    // Retrieve all reservas
    public LiveData<List<Reserva>> getAllReservas() {
        // TODO, CHANGE
        return mReservaDao.getAllReservas();
    }

    // Update a reserva
    public void update(Reserva reserva) {
        executorService.execute(() -> mReservaDao.update(reserva));
    }

    // Delete a reserva by object
    public void delete(Reserva reserva) {
        executorService.execute(() -> mReservaDao.delete(reserva));
    }

    // Delete a reserva by ID
    public void deleteById(long reservationId) {
        executorService.execute(() -> mReservaDao.deleteById(reservationId));
    }
}
