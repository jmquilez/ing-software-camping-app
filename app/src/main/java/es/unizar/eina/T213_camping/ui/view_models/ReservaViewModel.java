package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.database.repositories.ReservaRepository;

/**
 * ViewModel que gestiona los datos de las reservas.
 * Actúa como intermediario entre la UI y el repositorio de reservas,
 * proporcionando una capa de abstracción para las operaciones de datos.
 */
public class ReservaViewModel extends AndroidViewModel {

    /**
     * Repositorio para acceder a las operaciones de reservas.
     */
    private ReservaRepository mRepository;

    /**
     * LiveData que contiene todas las reservas del sistema.
     */
    private final LiveData<List<Reserva>> mAllReservas;

    /**
     * Constructor del ViewModel.
     * Inicializa el repositorio y obtiene la lista inicial de reservas.
     * @param application Contexto de la aplicación
     */
    public ReservaViewModel(Application application) {
        super(application);
        mRepository = new ReservaRepository(application);
        mAllReservas = mRepository.getAllReservas();
    }

    /**
     * Obtiene todas las reservas del sistema.
     * @return LiveData con la lista de todas las reservas
     */
    public LiveData<List<Reserva>> getAllReservas() { return mAllReservas; }

    /**
     * Inserta una nueva reserva en el sistema.
     * @param reserva Reserva a insertar
     * @return ID generado para la nueva reserva
     */
    public long insert(Reserva reserva) { return mRepository.insert(reserva); }

    /**
     * Actualiza una reserva existente.
     * @param reserva Reserva con los datos actualizados
     * @return ID de la reserva actualizada
     */
    public long update(Reserva reserva) { return mRepository.update(reserva); }

    /**
     * Elimina una reserva del sistema.
     * @param reserva Reserva a eliminar
     * @return ID de la reserva eliminada
     */
    public long delete(Reserva reserva) { return mRepository.delete(reserva); }

    /**
     * Elimina una reserva por su ID.
     * @param reservationId ID de la reserva a eliminar
     * @return ID de la reserva eliminada
     */
    public long deleteById(long reservationId) { return mRepository.deleteById(reservationId); }

    /**
     * Verifica si existe una reserva con el ID especificado.
     * @param reservaId ID de la reserva a verificar
     * @return true si existe la reserva, false en caso contrario
     */
    public boolean exists(long reservaId) {
        return mRepository.exists(reservaId);
    }

    /**
     * Obtiene el número total de reservas.
     * @return número de reservas en la base de datos
     */
    public int getReservasCount() {
        return mRepository.getReservasCount();
    }
}
