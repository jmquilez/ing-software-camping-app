package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.repositories.ParcelaRepository;

public class ParcelaViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;

    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
    }

    public LiveData<List<Parcela>> getAllParcelas() { return mAllParcelas; }

    public void insert(Parcela parcela) { mRepository.insert(parcela); }

    public void update(Parcela parcela) { mRepository.update(parcela); }

    public void delete(Parcela parcela) { mRepository.delete(parcela); }

    // New method to retrieve available parcelas
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return mRepository.getAvailableParcelas();
    }

    // New method to retrieve parcelas by reservation ID
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return mRepository.getParcelasByReservationId(reservationId);
    }

    // New method to retrieve parcelas not linked to a specific reservation ID
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return mRepository.getParcelasNotLinkedToReservation(reservationId);
    }

    // New method to delete a parcela by its nombre
    public void deleteByNombre(String nombre) {
        mRepository.deleteByNombre(nombre);
    }
}
