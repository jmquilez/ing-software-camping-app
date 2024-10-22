package es.unizar.eina.T213_camping.db.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.db.AppDatabase;
import es.unizar.eina.T213_camping.db.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.db.models.ParcelaReservada;

public class ParcelaReservadaRepository {

    private ParcelaReservadaDao parcelaReservadaDao;
    private ExecutorService executorService;

    public ParcelaReservadaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        parcelaReservadaDao = db.parcelaReservadaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert a new parcelaReservada
    public void insert(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.insert(parcelaReservada));
    }

    // Retrieve all parcelasReservadas
    public LiveData<List<ParcelaReservada>> getAllParcelaReservadas() {
        return parcelaReservadaDao.getAllParcelaReservadas();
    }

    public LiveData<List<ParcelaReservada>> fetchParcelsForReserva(int idReserva) {
        return parcelaReservadaDao.getAllParcelaReservadasForReserva(idReserva);
    }

    // Update a parcelaReservada
    public void update(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.update(parcelaReservada));
    }

    // Delete a parcelaReservada
    public void delete(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.delete(parcelaReservada));
    }

    public void updateParcelasForReservation(long reservationId, List<ParcelaReservada> updatedParcels) {
        executorService.execute(() -> {
            // Delete existing parcels for this reservation
            parcelaReservadaDao.deleteParcelasForReservation(reservationId);

            // Insert the updated parcels
            for (ParcelaReservada parcel : updatedParcels) {
                parcelaReservadaDao.insert(parcel);
            }
        });
    }
}
