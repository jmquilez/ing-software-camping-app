package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

public class ParcelaRepository {

    private ParcelaDao mParcelaDao;
    private ExecutorService executorService;

    public ParcelaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mParcelaDao = db.parcelaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert a new parcela
    public void insert(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.insert(parcela));
    }

    // Retrieve all parcelas
    public LiveData<List<Parcela>> getAllParcelas() {
        return mParcelaDao.getAllParcelas();
    }

    // Update a parcela
    public void update(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.update(parcela));
    }

    // Delete a parcela
    public void delete(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.delete(parcela));
    }

    // Retrieve available parcelas
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return mParcelaDao.getAvailableParcelas();
    }

    // Retrieve parcelas by reservation ID
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return mParcelaDao.getParcelasByReservationId(reservationId);
    }

    // New method to retrieve parcelas not linked to a specific reservation ID
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return mParcelaDao.getParcelasNotLinkedToReservation(reservationId);
    }

    // New method to delete a parcela by its nombre
    public void deleteByNombre(String nombre) {
        executorService.execute(() -> mParcelaDao.deleteByNombre(nombre));
    }
}
