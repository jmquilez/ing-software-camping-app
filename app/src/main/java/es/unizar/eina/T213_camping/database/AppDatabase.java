package es.unizar.eina.T213_camping.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.models.Reserva;
import androidx.room.TypeConverters;
import es.unizar.eina.T213_camping.database.converters.DateConverter;
import es.unizar.eina.T213_camping.utils.DateUtils;
import es.unizar.eina.T213_camping.utils.PriceUtils;

/**
 * Clase principal de la base de datos de la aplicación.
 * Implementa el patrón Singleton para mantener una única instancia de la base de datos.
 * Utiliza Room como ORM para la gestión de la base de datos SQLite.
 */
@Database(entities = {Parcela.class, Reserva.class, ParcelaReservada.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Instancia única de la base de datos.
     */
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtiene el DAO para las operaciones con parcelas.
     * @return ParcelaDao para acceder a las operaciones de parcelas
     */
    public abstract ParcelaDao parcelaDao();

    /**
     * Obtiene el DAO para las operaciones con reservas.
     * @return ReservaDao para acceder a las operaciones de reservas
     */
    public abstract ReservaDao reservaDao();

    /**
     * Obtiene el DAO para las operaciones con parcelas reservadas.
     * @return ParcelaReservadaDao para acceder a las operaciones de parcelas reservadas
     */
    public abstract ParcelaReservadaDao parcelaReservadaDao();

    /**
     * Número de hilos disponibles para operaciones de escritura en la base de datos.
     */
    private static final int NUMBER_OF_THREADS = 4;

    /**
     * Servicio ejecutor para operaciones asíncronas de escritura en la base de datos.
     */
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Callback que se ejecuta al crear la base de datos.
     * Se utiliza para poblar la base de datos con datos iniciales.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Get DAOs
                ParcelaDao parcelaDao = INSTANCE.parcelaDao();
                ReservaDao reservaDao = INSTANCE.reservaDao();
                ParcelaReservadaDao parcelaReservadaDao = INSTANCE.parcelaReservadaDao();

                // Clear existing data
                parcelaDao.deleteAll();
                reservaDao.deleteAll();
                parcelaReservadaDao.deleteAll();

                // Insert initial Parcelas
                Parcela parcela1 = new Parcela("Parcela A",
                        "Parcela pequeña ideal para parejas. Zona tranquila.",
                        2, 20.0);
                parcelaDao.insert(parcela1);

                Parcela parcela2 = new Parcela("Parcela B",
                        "Parcela mediana con sombra natural. Perfecta para familias.",
                        4, 25.0);
                parcelaDao.insert(parcela2);

                Parcela parcela3 = new Parcela("Parcela C",
                        "Parcela grande con conexión eléctrica. Ideal para autocaravanas.",
                        6, 30.0);
                parcelaDao.insert(parcela3);

                Parcela parcela4 = new Parcela("Parcela D",
                        "Parcela premium con vistas al lago. Incluye punto de agua.",
                        4, 35.0);
                parcelaDao.insert(parcela4);

                Parcela parcela5 = new Parcela("Parcela E",
                        "Parcela familiar con zona de barbacoa.",
                        5, 28.0);
                parcelaDao.insert(parcela5);

                try {
                    // Create dates for reservations
                    Date entryDate1 = DateUtils.DATE_FORMAT.parse("01/10/2024");
                    Date departureDate1 = DateUtils.DATE_FORMAT.parse("07/10/2024");
                    Date entryDate2 = DateUtils.DATE_FORMAT.parse("10/10/2024");
                    Date departureDate2 = DateUtils.DATE_FORMAT.parse("15/10/2024");
                    Date entryDate3 = DateUtils.DATE_FORMAT.parse("05/11/2024");
                    Date departureDate3 = DateUtils.DATE_FORMAT.parse("10/11/2024");

                    // Create ParcelaOccupancy lists for each reservation
                    List<ParcelaOccupancy> parcelas1 = new ArrayList<>();
                    parcelas1.add(new ParcelaOccupancy(parcela1, 2)); // Parcela A with 2 people

                    List<ParcelaOccupancy> parcelas2 = new ArrayList<>();
                    parcelas2.add(new ParcelaOccupancy(parcela2, 4)); // Parcela B with 4 people
                    parcelas2.add(new ParcelaOccupancy(parcela3, 3)); // Parcela C with 3 people

                    List<ParcelaOccupancy> parcelas3 = new ArrayList<>();
                    parcelas3.add(new ParcelaOccupancy(parcela4, 4)); // Parcela D with 4 people

                    // Calculate prices using PriceUtils
                    double price1 = PriceUtils.calculateReservationPrice(entryDate1, departureDate1, parcelas1);
                    double price2 = PriceUtils.calculateReservationPrice(entryDate2, departureDate2, parcelas2);
                    double price3 = PriceUtils.calculateReservationPrice(entryDate3, departureDate3, parcelas3);

                    // Insert Reservas with calculated prices
                    Reserva reserva1 = new Reserva("Juan Pérez",
                            entryDate1,
                            departureDate1,
                            "123456789",
                            price1);
                    long res1Id = reservaDao.insert(reserva1);

                    Reserva reserva2 = new Reserva("María López",
                            entryDate2,
                            departureDate2,
                            "987654321",
                            price2);
                    long res2Id = reservaDao.insert(reserva2);

                    Reserva reserva3 = new Reserva("Carlos Ruiz",
                            entryDate3,
                            departureDate3,
                            "555666777",
                            price3);
                    long res3Id = reservaDao.insert(reserva3);

                    // Insert ParcelaReservadas
                    parcelaReservadaDao.insert(new ParcelaReservada("Parcela A", res1Id, 2));
                    parcelaReservadaDao.insert(new ParcelaReservada("Parcela B", res2Id, 4));
                    parcelaReservadaDao.insert(new ParcelaReservada("Parcela C", res2Id, 3));
                    parcelaReservadaDao.insert(new ParcelaReservada("Parcela D", res3Id, 4));

                } catch (ParseException e) {
                    Log.e("AppDatabase", "Error parsing dates for initial data", e);
                }
            });
        }
    };

    /**
     * Obtiene la instancia única de la base de datos.
     * Si no existe, la crea utilizando el patrón Singleton.
     * @param context Contexto de la aplicación
     * @return Instancia única de AppDatabase
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "camping_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
