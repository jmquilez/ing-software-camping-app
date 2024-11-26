package es.unizar.eina.T213_camping.ui.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.repositories.ParcelaReservadaRepository;

/**
 * ViewModel que gestiona los datos de las parcelas reservadas.
 * Actúa como intermediario entre la UI y el repositorio de parcelas reservadas,
 * proporcionando una capa de abstracción para las operaciones de datos.
 */
public class ParcelaReservadaViewModel extends AndroidViewModel {

    /**
     * Repositorio para acceder a las operaciones de parcelas reservadas.
     */
    private ParcelaReservadaRepository mRepository;

    /**
     * LiveData que contiene todas las relaciones parcela-reserva.
     */
    private final LiveData<List<ParcelaReservada>> mAllParcelaReservadas;

    /**
     * Constructor del ViewModel.
     * Inicializa el repositorio y obtiene la lista inicial de parcelas reservadas.
     * @param application Contexto de la aplicación
     */
    public ParcelaReservadaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaReservadaRepository(application);
        mAllParcelaReservadas = mRepository.getAllParcelaReservadas();
    }

    /**
     * Obtiene todas las parcelas reservadas.
     * @return LiveData con la lista de todas las parcelas reservadas
     */
    public LiveData<List<ParcelaReservada>> getAllParcelaReservadas() { return mAllParcelaReservadas; }

    /**
     * Obtiene las parcelas reservadas para una reserva específica.
     * @param idReserva ID de la reserva
     * @return LiveData con la lista de parcelas reservadas para esa reserva
     */
    public LiveData<List<ParcelaReservada>> getAllParcelaReservadasForReserva(int idReserva) { 
        return mRepository.fetchParcelsForReserva(idReserva); 
    }

    /**
     * Inserta una nueva relación parcela-reserva.
     * @param parcelaReservada ParcelaReservada a insertar
     */
    public void insert(ParcelaReservada parcelaReservada) { mRepository.insert(parcelaReservada); }

    /**
     * Actualiza una relación parcela-reserva existente.
     * @param parcelaReservada ParcelaReservada con los datos actualizados
     */
    public void update(ParcelaReservada parcelaReservada) { mRepository.update(parcelaReservada); }

    /**
     * Elimina una relación parcela-reserva.
     * @param parcelaReservada ParcelaReservada a eliminar
     */
    public void delete(ParcelaReservada parcelaReservada) { mRepository.delete(parcelaReservada); }

    /**
     * Actualiza las parcelas asociadas a una reserva.
     * @param reservationId ID de la reserva
     * @param updatedParcels Nueva lista de parcelas reservadas
     */
    public void updateParcelasForReservation(long reservationId, List<ParcelaReservada> updatedParcels) {
        mRepository.updateParcelasForReservation(reservationId, updatedParcels);
    }

    /**
     * Obtiene las parcelas disponibles en un intervalo de fechas.
     * @param entryDate Fecha de entrada del intervalo
     * @param departureDate Fecha de salida del intervalo
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervalo(Date entryDate, Date departureDate) {
        return mRepository.getParcelasDisponiblesEnIntervalo(entryDate, departureDate);
    }

    /**
     * Obtiene las parcelas disponibles en un intervalo de fechas, excluyendo una reserva específica.
     * @param startDate Fecha de inicio del intervalo
     * @param endDate Fecha de fin del intervalo
     * @param excludeReservationId ID de la reserva a excluir
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervaloExcludingReservation(Date startDate, Date endDate, long excludeReservationId) {
        return mRepository.getParcelasDisponiblesEnIntervaloExcludingReservation(startDate, endDate, excludeReservationId);
    }
}
