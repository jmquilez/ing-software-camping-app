package es.unizar.eina.T213_camping.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaDao_Impl;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao;
import es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao_Impl;
import es.unizar.eina.T213_camping.database.daos.ReservaDao;
import es.unizar.eina.T213_camping.database.daos.ReservaDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ParcelaDao _parcelaDao;

  private volatile ReservaDao _reservaDao;

  private volatile ParcelaReservadaDao _parcelaReservadaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `parcela` (`nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `maxOcupantes` INTEGER NOT NULL, `eurPorPersona` REAL NOT NULL, PRIMARY KEY(`nombre`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reserva` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombreCliente` TEXT NOT NULL, `fechaEntrada` TEXT NOT NULL, `fechaSalida` TEXT NOT NULL, `telefonoCliente` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `parcela_reservada` (`parcelaNombre` TEXT NOT NULL, `reservaId` INTEGER NOT NULL, `numOcupantes` INTEGER NOT NULL, PRIMARY KEY(`parcelaNombre`, `reservaId`), FOREIGN KEY(`parcelaNombre`) REFERENCES `parcela`(`nombre`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`reservaId`) REFERENCES `reserva`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_parcela_reservada_reservaId` ON `parcela_reservada` (`reservaId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b49f7f69600f720a1a48a39597dd95d7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `parcela`");
        db.execSQL("DROP TABLE IF EXISTS `reserva`");
        db.execSQL("DROP TABLE IF EXISTS `parcela_reservada`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsParcela = new HashMap<String, TableInfo.Column>(4);
        _columnsParcela.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParcela.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParcela.put("maxOcupantes", new TableInfo.Column("maxOcupantes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParcela.put("eurPorPersona", new TableInfo.Column("eurPorPersona", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysParcela = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesParcela = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoParcela = new TableInfo("parcela", _columnsParcela, _foreignKeysParcela, _indicesParcela);
        final TableInfo _existingParcela = TableInfo.read(db, "parcela");
        if (!_infoParcela.equals(_existingParcela)) {
          return new RoomOpenHelper.ValidationResult(false, "parcela(es.unizar.eina.T213_camping.database.models.Parcela).\n"
                  + " Expected:\n" + _infoParcela + "\n"
                  + " Found:\n" + _existingParcela);
        }
        final HashMap<String, TableInfo.Column> _columnsReserva = new HashMap<String, TableInfo.Column>(5);
        _columnsReserva.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReserva.put("nombreCliente", new TableInfo.Column("nombreCliente", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReserva.put("fechaEntrada", new TableInfo.Column("fechaEntrada", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReserva.put("fechaSalida", new TableInfo.Column("fechaSalida", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReserva.put("telefonoCliente", new TableInfo.Column("telefonoCliente", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReserva = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReserva = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReserva = new TableInfo("reserva", _columnsReserva, _foreignKeysReserva, _indicesReserva);
        final TableInfo _existingReserva = TableInfo.read(db, "reserva");
        if (!_infoReserva.equals(_existingReserva)) {
          return new RoomOpenHelper.ValidationResult(false, "reserva(es.unizar.eina.T213_camping.database.models.Reserva).\n"
                  + " Expected:\n" + _infoReserva + "\n"
                  + " Found:\n" + _existingReserva);
        }
        final HashMap<String, TableInfo.Column> _columnsParcelaReservada = new HashMap<String, TableInfo.Column>(3);
        _columnsParcelaReservada.put("parcelaNombre", new TableInfo.Column("parcelaNombre", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParcelaReservada.put("reservaId", new TableInfo.Column("reservaId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParcelaReservada.put("numOcupantes", new TableInfo.Column("numOcupantes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysParcelaReservada = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysParcelaReservada.add(new TableInfo.ForeignKey("parcela", "CASCADE", "NO ACTION", Arrays.asList("parcelaNombre"), Arrays.asList("nombre")));
        _foreignKeysParcelaReservada.add(new TableInfo.ForeignKey("reserva", "CASCADE", "NO ACTION", Arrays.asList("reservaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesParcelaReservada = new HashSet<TableInfo.Index>(1);
        _indicesParcelaReservada.add(new TableInfo.Index("index_parcela_reservada_reservaId", false, Arrays.asList("reservaId"), Arrays.asList("ASC")));
        final TableInfo _infoParcelaReservada = new TableInfo("parcela_reservada", _columnsParcelaReservada, _foreignKeysParcelaReservada, _indicesParcelaReservada);
        final TableInfo _existingParcelaReservada = TableInfo.read(db, "parcela_reservada");
        if (!_infoParcelaReservada.equals(_existingParcelaReservada)) {
          return new RoomOpenHelper.ValidationResult(false, "parcela_reservada(es.unizar.eina.T213_camping.database.models.ParcelaReservada).\n"
                  + " Expected:\n" + _infoParcelaReservada + "\n"
                  + " Found:\n" + _existingParcelaReservada);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "b49f7f69600f720a1a48a39597dd95d7", "e8a2316ff5ff2f64b949efa5e94416bf");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "parcela","reserva","parcela_reservada");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `parcela`");
      _db.execSQL("DELETE FROM `reserva`");
      _db.execSQL("DELETE FROM `parcela_reservada`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ParcelaDao.class, ParcelaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReservaDao.class, ReservaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ParcelaReservadaDao.class, ParcelaReservadaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ParcelaDao parcelaDao() {
    if (_parcelaDao != null) {
      return _parcelaDao;
    } else {
      synchronized(this) {
        if(_parcelaDao == null) {
          _parcelaDao = new ParcelaDao_Impl(this);
        }
        return _parcelaDao;
      }
    }
  }

  @Override
  public ReservaDao reservaDao() {
    if (_reservaDao != null) {
      return _reservaDao;
    } else {
      synchronized(this) {
        if(_reservaDao == null) {
          _reservaDao = new ReservaDao_Impl(this);
        }
        return _reservaDao;
      }
    }
  }

  @Override
  public ParcelaReservadaDao parcelaReservadaDao() {
    if (_parcelaReservadaDao != null) {
      return _parcelaReservadaDao;
    } else {
      synchronized(this) {
        if(_parcelaReservadaDao == null) {
          _parcelaReservadaDao = new ParcelaReservadaDao_Impl(this);
        }
        return _parcelaReservadaDao;
      }
    }
  }
}
