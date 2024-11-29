import android.util.Log;
import com.tuapp.model.Parcela;
import com.tuapp.repository.ParcelaRepository;

public class ParcelaTest {
    private static final String TAGP = "ParcelaRepositoryTest";
    private static final long TIMEOUT = 15000;
    private final ParcelaRepository parcelarepository;
    private static final String TAGR = "ReservaRepositoryTest";
    private final ReservaRepository reservarepository;

    public ParcelaRepositoryTest(ParcelaRepository repository) {
        this.repository = repository;
    }

    public void runAllTests() {
        testInsertValidParcela();
        testInsertNullName();
        testInsertEmptyName();
        testInsertDuplicateName();
        testInsertInvalidOccupants();
        testInsertInvalidPrice();
        testInsertNullDescription();
        testInsertValidReserva();
        testInsertNullClientName();
        testInsertEmptyClientName();
        testInsertInvalidPhone();
        testInsertPastDate();
        testInsertInvalidDates();
        testNoOverlap();
        testTotalOverlap();
        testPartialOverlapStart();
        testPartialOverlapEnd();
        testContainedOverlap();
    }

    private void testInsertValidParcela() {
        try {
            Parcela parcela = new Parcela(
                "TEST_PARCELA_1",
                "Descripción de prueba",
                4,
                25.0
            );
            parcelarepository.insert(parcela);
            Log.d(TAGP, "Test 1 (Inserción válida): CORRECTO");
        } catch (Throwable e) {
            Log.e(TAGP, "Test 1 (Inserción válida): ERROR - " + e.getMessage());
        }
    }

    private void testInsertNullName() {
        try {
            Parcela parcela = new Parcela(
                null,
                "Descripción de prueba",
                4,
                25.0
            );
            repository.insert(parcela);
            Log.d(TAG, "Test 2 (Nombre nulo): ERROR - Debería haber fallado");
        } catch (Throwable e) {
            Log.d(TAG, "Test 2 (Nombre nulo): CORRECTO - Falló como se esperaba");
        }
    }

    private static final String TAG = "ReservaRepositoryTest";
    private final ReservaRepository repository;


    private void testInsertValidReserva() {
        try {
            Date entryDate = new Date(System.currentTimeMillis() + 86400000); // mañana
            Date departureDate = new Date(System.currentTimeMillis() + 172800000); // pasado mañana
            
            Reserva reserva = new Reserva(
                "Cliente Prueba",
                entryDate,
                departureDate,
                "666777888",
                100.0
            );
            
            long id = repository.insert(reserva);
            if (id > 0) {
                Log.d(TAG, "Test 1 (Inserción válida): CORRECTO");
            } else {
                Log.d(TAG, "Test 1 (Inserción válida): ERROR - ID inválido");
            }
        } catch (Throwable e) {
            Log.e(TAG, "Test 1 (Inserción válida): ERROR - " + e.getMessage());
        }
    }
}
