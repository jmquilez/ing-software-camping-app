package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las parcelas reservadas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ParcelaReservadaRepository {

    private static final long TIMEOUT = 3000; // 3 seconds in milliseconds
    private final ParcelaReservadaDao parcelaReservadaDao;
    private final ExecutorService executorService;

    /**
     * Constructor del repositorio.
     * @param application Contexto de la aplicación para acceder a la base de datos
     */
    public ParcelaReservadaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        parcelaReservadaDao = db.parcelaReservadaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta una nueva relación parcela-reserva.
     * La operación se realiza de forma asíncrona.
     * @param parcelaReservada ParcelaReservada a insertar
     */
    public long insert(ParcelaReservada parcelaReservada) {
        Future<Long> future = executorService.submit(() -> parcelaReservadaDao.insert(parcelaReservada));
        return handleFutureResult(future, "Insert");
    }

    /**
     * Obtiene todas las relaciones parcela-reserva.
     * @return LiveData con la lista de todas las parcelas reservadas
     */
    public LiveData<List<ParcelaReservada>> getAllParcelaReservadas() {
        return parcelaReservadaDao.getAllParcelaReservadas();
    }

    /**
     * Obtiene las parcelas reservadas para una reserva específica.
     * @param idReserva ID de la reserva
     * @return LiveData con la lista de parcelas reservadas para esa reserva
     */
    public LiveData<List<ParcelaReservada>> fetchParcelsForReserva(int idReserva) {
        return parcelaReservadaDao.getAllParcelaReservadasForReserva(idReserva);
    }

    /**
     * Actualiza una relación parcela-reserva existente.
     * La operación se realiza de forma asíncrona.
     * @param parcelaReservada ParcelaReservada con los datos actualizados
     */
    public Long update(ParcelaReservada parcelaReservada) {
        Future<Integer> future = executorService.submit(() -> parcelaReservadaDao.update(parcelaReservada));
        return handleFutureResult(future, "Update");
    }

    /**
     * Elimina una relación parcela-reserva.
     * La operación se realiza de forma asíncrona.
     * @param parcelaReservada ParcelaReservada a eliminar
     */
    public Long delete(ParcelaReservada parcelaReservada) {
        Future<Integer> future = executorService.submit(() -> parcelaReservadaDao.delete(parcelaReservada));
        return handleFutureResult(future, "Delete");
    }

    /**
     * Actualiza las parcelas asociadas a una reserva.
     * Elimina las asociaciones existentes y crea las nuevas.
     * La operación se realiza de forma asíncrona.
     * @param reservationId ID de la reserva
     * @param updatedParcels Nueva lista de parcelas reservadas
     */
    public void updateParcelasForReservation(long reservationId, List<ParcelaReservada> updatedParcels) {
        executorService.execute(() -> {
            // Elimina las parcelas existentes para esta reserva
            parcelaReservadaDao.deleteParcelasForReservation(reservationId);

            // Inserta las parcelas actualizadas
            for (ParcelaReservada parcel : updatedParcels) {
                parcelaReservadaDao.insert(parcel);
            }
        });
    }

    /**
     * Obtiene las parcelas disponibles en un intervalo de fechas.
     * @param fechaInicio Fecha de inicio del intervalo
     * @param fechaFin Fecha de fin del intervalo
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervalo(Date fechaInicio, Date fechaFin) {
        return parcelaReservadaDao.getParcelasDisponiblesEnIntervalo(fechaInicio, fechaFin);
    }

    /**
     * Obtiene las parcelas disponibles en un intervalo de fechas, excluyendo una reserva específica.
     * @param startDate Fecha de inicio del intervalo
     * @param endDate Fecha de fin del intervalo
     * @param excludeReservationId ID de la reserva a excluir
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervaloExcludingReservation(Date startDate, Date endDate, long excludeReservationId) {
        return parcelaReservadaDao.getParcelasDisponiblesEnIntervaloExcludingReservation(startDate, endDate, excludeReservationId);
    }

    /**
     * Elimina las parcelas asociadas a una reserva.
     * La operación se realiza de forma asíncrona.
     * @param reservationId ID de la reserva
     */
    public Long deleteParcelasForReservation(long reservationId) {
        Future<Integer> future = executorService.submit(() -> parcelaReservadaDao.deleteParcelasForReservation(reservationId));
        return handleFutureResult(future, "DeleteForReservation");
    }

    /**
     * Actualiza el nombre de una parcela.
     * La operación se realiza de forma asíncrona.
     * @param oldName Nombre actual de la parcela
     * @param newName Nuevo nombre de la parcela
     */
    public Long updateParcelaNombre(String oldName, String newName) {
        Future<Integer> future = executorService.submit(() -> parcelaReservadaDao.updateParcelaNombre(oldName, newName));
        return handleFutureResult(future, "UpdateNombre");
    }

    /**
     * Verifica si existe una relación específica parcela-reserva.
     * @param parcelaNombre Nombre de la parcela
     * @param reservaId ID de la reserva
     * @return true si existe la relación, false en caso contrario
     */
    public boolean exists(String parcelaNombre, long reservaId) {
        Future<Boolean> future = executorService.submit(() -> 
            parcelaReservadaDao.exists(parcelaNombre, reservaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Logger.getLogger(ParcelaReservadaRepository.class.getName())
                  .severe("Error checking if ParcelaReservada exists: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    private <T> Long handleFutureResult(Future<T> future, String operation) {
        try {
            return ((Number) future.get(TIMEOUT, TimeUnit.MILLISECONDS)).longValue();
        } catch (Exception e) {
            Logger.getLogger(ParcelaReservadaRepository.class.getName()).severe(operation + " error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return -1L;
        }
    }
}
