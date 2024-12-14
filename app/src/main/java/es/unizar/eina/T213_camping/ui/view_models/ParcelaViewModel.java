package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.repositories.ParcelaRepository;

/**
 * ViewModel que gestiona los datos de las parcelas.
 * Actúa como intermediario entre la UI y el repositorio de parcelas,
 * proporcionando una capa de abstracción para las operaciones de datos.
 */
public class ParcelaViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;

    /**
     * Constructor del ViewModel.
     * Inicializa el repositorio y obtiene la lista inicial de parcelas.
     * @param application Contexto de la aplicación
     */
    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
    }

    /**
     * Obtiene todas las parcelas del sistema.
     * @return LiveData con la lista de todas las parcelas
     */
    public LiveData<List<Parcela>> getAllParcelas() { return mAllParcelas; }

    /**
     * Inserta una nueva parcela en el sistema.
     * @param parcela Parcela a insertar
     */
    public long insert(Parcela parcela) {
        return mRepository.insert(parcela);
    }

    /**
     * Actualiza una parcela existente.
     * @param parcela Parcela con los datos actualizados
     */
    public long update(Parcela parcela) {
        return mRepository.update(parcela);
    }

    /**
     * Elimina una parcela del sistema.
     * @param parcela Parcela a eliminar
     */
    public long delete(Parcela parcela) {
        return mRepository.delete(parcela);
    }

    /**
     * Obtiene las parcelas que no están reservadas.
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return mRepository.getAvailableParcelas();
    }

    /**
     * Obtiene las parcelas asociadas a una reserva específica junto con su ocupación.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas y su ocupación
     */
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return mRepository.getParcelasByReservationId(reservationId);
    }

    /**
     * Obtiene las parcelas que no están vinculadas a una reserva específica.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas no vinculadas
     */
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return mRepository.getParcelasNotLinkedToReservation(reservationId);
    }

    /**
     * Elimina una parcela por su nombre.
     * @param nombre Nombre de la parcela a eliminar
     */
    public long deleteByNombre(String nombre) {
        return mRepository.deleteByNombre(nombre);
    }

    /**
     * Verifica si existe una parcela con el nombre especificado.
     * @param nombre Nombre de la parcela a verificar
     * @return true si existe una parcela con ese nombre, false en caso contrario
     */
    public boolean exists(String nombre) {
        return mRepository.exists(nombre);
    }

    /**
     * Actualiza una parcela incluyendo su nombre, manteniendo las referencias en ParcelaReservada.
     * @param oldName Nombre actual de la parcela
     * @param updatedParcela Parcela con los datos actualizados
     */
    public void updateWithNameChange(String oldName, Parcela updatedParcela) {
        mRepository.updateWithNameChange(oldName, updatedParcela);
    }
}
