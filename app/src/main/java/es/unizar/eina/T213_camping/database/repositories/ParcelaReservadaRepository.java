package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las parcelas reservadas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ParcelaReservadaRepository {

    /**
     * DAO para acceder a las operaciones de parcelas reservadas en la base de datos.
     */
    private ParcelaReservadaDao parcelaReservadaDao;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private ExecutorService executorService;

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
    public void insert(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.insert(parcelaReservada));
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
    public void update(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.update(parcelaReservada));
    }

    /**
     * Elimina una relación parcela-reserva.
     * La operación se realiza de forma asíncrona.
     * @param parcelaReservada ParcelaReservada a eliminar
     */
    public void delete(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> parcelaReservadaDao.delete(parcelaReservada));
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
}
