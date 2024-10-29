package es.unizar.eina.T213_camping.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.models.Reserva;

// TODO: CHANGE TO CAMPINGAPPDATABASE
@Database(entities = {Parcela.class, Reserva.class, ParcelaReservada.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ParcelaDao parcelaDao();
    public abstract ReservaDao reservaDao();
    public abstract ParcelaReservadaDao parcelaReservadaDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // RoomDatabase.Callback for prepopulating data
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Example of prepopulating database with initial data
            databaseWriteExecutor.execute(() -> {
                // Get DAOs
                ParcelaDao parcelaDao = INSTANCE.parcelaDao();
                ReservaDao reservaDao = INSTANCE.reservaDao();
                ParcelaReservadaDao parcelaReservadaDao = INSTANCE.parcelaReservadaDao();

                parcelaDao.deleteAll();
                reservaDao.deleteAll();
                parcelaReservadaDao.deleteAll();

                // Clear existing data (optional)
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

                // Insert initial Reservas
                Reserva reserva1 = new Reserva("Juan Perez",
                        "2024-10-01", "2024-10-07", "123456789");
                long res1Id = reservaDao.insert(reserva1);

                Reserva reserva2 = new Reserva("Maria Lopez",
                        "2024-10-10", "2024-10-15", "987654321");
                long res2Id = reservaDao.insert(reserva2);

                // Insert initial ParcelaReservadas
                ParcelaReservada parcelaReservada1 = new ParcelaReservada("Parcela A",
                res1Id, 4);
                parcelaReservadaDao.insert(parcelaReservada1);

                ParcelaReservada parcelaReservada2 = new ParcelaReservada("Parcela B",
                res2Id, 6);
                parcelaReservadaDao.insert(parcelaReservada2);
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "camping_database")
                            .addCallback(sRoomDatabaseCallback)  // Add the callback here
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
