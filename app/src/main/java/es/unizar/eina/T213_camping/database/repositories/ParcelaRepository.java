package es.unizar.eina.T213_camping.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.database.AppDatabase;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

/**
 * Repositorio que gestiona las operaciones de acceso a datos para las parcelas.
 * Proporciona una capa limpia de acceso a los datos para el resto de la aplicación.
 */
public class ParcelaRepository {

    /**
     * DAO para acceder a las operaciones de parcelas en la base de datos.
     */
    private ParcelaDao mParcelaDao;

    /**
     * Servicio ejecutor para realizar operaciones asíncronas.
     */
    private ExecutorService executorService;

    /**
     * Constructor del repositorio.
     * @param application Contexto de la aplicación para acceder a la base de datos
     */
    public ParcelaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mParcelaDao = db.parcelaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta una nueva parcela en la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param parcela Parcela a insertar
     */
    public void insert(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.insert(parcela));
    }

    /**
     * Obtiene todas las parcelas de la base de datos.
     * @return LiveData con la lista de todas las parcelas
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return mParcelaDao.getAllParcelas();
    }

    /**
     * Actualiza una parcela existente.
     * La operación se realiza de forma asíncrona.
     * @param parcela Parcela con los datos actualizados
     */
    public void update(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.update(parcela));
    }

    /**
     * Elimina una parcela de la base de datos.
     * La operación se realiza de forma asíncrona.
     * @param parcela Parcela a eliminar
     */
    public void delete(Parcela parcela) {
        executorService.execute(() -> mParcelaDao.delete(parcela));
    }

    /**
     * Obtiene las parcelas que no están reservadas.
     * @return LiveData con la lista de parcelas disponibles
     */
    public LiveData<List<Parcela>> getAvailableParcelas() {
        return mParcelaDao.getAvailableParcelas();
    }

    /**
     * Obtiene las parcelas asociadas a una reserva específica junto con su ocupación.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas y su ocupación
     */
    public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(long reservationId) {
        return mParcelaDao.getParcelasByReservationId(reservationId);
    }

    /**
     * Obtiene las parcelas que no están vinculadas a una reserva específica.
     * @param reservationId ID de la reserva
     * @return LiveData con la lista de parcelas no vinculadas
     */
    public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(long reservationId) {
        return mParcelaDao.getParcelasNotLinkedToReservation(reservationId);
    }

    /**
     * Elimina una parcela por su nombre.
     * La operación se realiza de forma asíncrona.
     * @param nombre Nombre de la parcela a eliminar
     */
    public void deleteByNombre(String nombre) {
        executorService.execute(() -> mParcelaDao.deleteByNombre(nombre));
    }
}
