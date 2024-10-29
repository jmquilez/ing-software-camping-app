package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.database.repositories.ReservaRepository;

public class ReservaViewModel extends AndroidViewModel {

    private ReservaRepository mRepository;

    private final LiveData<List<Reserva>> mAllReservas;

    public ReservaViewModel(Application application) {
        super(application);
        mRepository = new ReservaRepository(application);
        mAllReservas = mRepository.getAllReservas();
    }

    public LiveData<List<Reserva>> getAllReservas() { return mAllReservas; }

    public long insert(Reserva reserva) { return mRepository.insert(reserva); }

    public void update(Reserva reserva) { mRepository.update(reserva); }

    public void delete(Reserva reserva) {
        mRepository.delete(reserva);
    }

    public void deleteById(long reservationId) {
        mRepository.deleteById(reservationId);
    }
}
