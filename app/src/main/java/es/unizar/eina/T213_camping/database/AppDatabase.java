package es.unizar.eina.T213_camping.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.models.Reserva;
import androidx.room.TypeConverters;
import es.unizar.eina.T213_camping.database.converters.DateConverter;

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
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

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
                        "Primera parcela con capacidad para 4 personas.",
                        4, 25.0);
                parcelaDao.insert(parcela1);

                Parcela parcela2 = new Parcela("Parcela B",
                        "Segunda parcela con capacidad para 6 personas.",
                        6, 30.0);
                parcelaDao.insert(parcela2);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    
                    // Insert initial Reservas
                    Reserva reserva1 = new Reserva("Juan Perez",
                            dateFormat.parse("2024-10-01"),
                            dateFormat.parse("2024-10-07"),
                            "123456789",
                            25.0); // Initial price
                    long res1Id = reservaDao.insert(reserva1);

                    Reserva reserva2 = new Reserva("Maria Lopez",
                            dateFormat.parse("2024-10-10"),
                            dateFormat.parse("2024-10-15"),
                            "987654321",
                            30.0); // Initial price
                    long res2Id = reservaDao.insert(reserva2);

                    // Insert initial ParcelaReservadas
                    ParcelaReservada parcelaReservada1 = new ParcelaReservada("Parcela A",
                            res1Id, 4);
                    parcelaReservadaDao.insert(parcelaReservada1);

                    ParcelaReservada parcelaReservada2 = new ParcelaReservada("Parcela B",
                            res2Id, 6);
                    parcelaReservadaDao.insert(parcelaReservada2);

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
