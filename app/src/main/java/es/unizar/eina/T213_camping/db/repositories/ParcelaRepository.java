package es.unizar.eina.T213_camping.db.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.db.AppDatabase;
import es.unizar.eina.T213_camping.db.daos.ParcelaDao;
import es.unizar.eina.T213_camping.db.models.Parcela;
import es.unizar.eina.T213_camping.db.models.ParcelaOccupancy;

public class ParcelaRepository {

    private ParcelaDao parcelaDao;
    private ExecutorService executorService;

    public ParcelaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        parcelaDao = db.parcelaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert a new parcela
    public void insert(Parcela parcela) {
        executorService.execute(() -> parcelaDao.insert(parcela));
    }

    // Retrieve all parcelas
    public LiveData<List<Parcela>> getAllParcelas() {
        return parcelaDao.getAllParcelas();
    }

    // Update a parcela
    public void update(Parcela parcela) {
        executorService.execute(() -> parcelaDao.update(parcela));
    }

    // Delete a parcela
    public void delete(Parcela parcela) {
        executorService.execute(() -> parcelaDao.delete(parcela));
    }

    // Retrieve available parcelas
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return parcelaDao.getAvailableParcelas();
    }

    // Retrieve parcelas by reservation ID
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return parcelaDao.getParcelasByReservationId(reservationId);
    }

    // New method to retrieve parcelas not linked to a specific reservation ID
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return parcelaDao.getParcelasNotLinkedToReservation(reservationId);
    }

    // New method to delete a parcela by its nombre
    public void deleteByNombre(String nombre) {
        executorService.execute(() -> parcelaDao.deleteByNombre(nombre));
    }
}
