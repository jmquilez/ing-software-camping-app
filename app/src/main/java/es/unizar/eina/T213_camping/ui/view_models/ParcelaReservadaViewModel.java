package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.repositories.ParcelaReservadaRepository;

public class ParcelaReservadaViewModel extends AndroidViewModel {

    private ParcelaReservadaRepository mRepository;

    private final LiveData<List<ParcelaReservada>> mAllParcelaReservadas;

    public ParcelaReservadaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaReservadaRepository(application);
        mAllParcelaReservadas = mRepository.getAllParcelaReservadas();
    }

    public LiveData<List<ParcelaReservada>> getAllParcelaReservadas() { return mAllParcelaReservadas; }

    public LiveData<List<ParcelaReservada>> getAllParcelaReservadasForReserva(int idReserva) { return mRepository.fetchParcelsForReserva(idReserva); }

    public void insert(ParcelaReservada parcelaReservada) { mRepository.insert(parcelaReservada); }

    public void update(ParcelaReservada parcelaReservada) { mRepository.update(parcelaReservada); }

    public void delete(ParcelaReservada parcelaReservada) { mRepository.delete(parcelaReservada); }

    public void updateParcelasForReservation(long reservationId, List<ParcelaReservada> updatedParcels) {
        mRepository.updateParcelasForReservation(reservationId, updatedParcels);
    }
}
