package es.unizar.eina.T213_camping.database.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.utils.ModelUtils.ParcelaOccupancy;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ParcelaDao_Impl implements ParcelaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Parcela> __insertionAdapterOfParcela;

  private final EntityDeletionOrUpdateAdapter<Parcela> __deletionAdapterOfParcela;

  private final EntityDeletionOrUpdateAdapter<Parcela> __updateAdapterOfParcela;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByNombre;

  private final SharedSQLiteStatement __preparedStmtOfDeleteParcelasWithPrefix;

  public ParcelaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfParcela = new EntityInsertionAdapter<Parcela>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `parcela` (`nombre`,`descripcion`,`maxOcupantes`,`eurPorPersona`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Parcela entity) {
        if (entity.getNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDescripcion());
        }
        if (entity.getMaxOcupantes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getMaxOcupantes());
        }
        if (entity.getEurPorPersona() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getEurPorPersona());
        }
      }
    };
    this.__deletionAdapterOfParcela = new EntityDeletionOrUpdateAdapter<Parcela>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `parcela` WHERE `nombre` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Parcela entity) {
        if (entity.getNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNombre());
        }
      }
    };
    this.__updateAdapterOfParcela = new EntityDeletionOrUpdateAdapter<Parcela>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `parcela` SET `nombre` = ?,`descripcion` = ?,`maxOcupantes` = ?,`eurPorPersona` = ? WHERE `nombre` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Parcela entity) {
        if (entity.getNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNombre());
        }
        if (entity.getDescripcion() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDescripcion());
        }
        if (entity.getMaxOcupantes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getMaxOcupantes());
        }
        if (entity.getEurPorPersona() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getEurPorPersona());
        }
        if (entity.getNombre() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNombre());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM parcela";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByNombre = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM parcela WHERE nombre = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteParcelasWithPrefix = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM parcela WHERE nombre LIKE ? || '%'";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Parcela parcela) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfParcela.insertAndReturnId(parcela);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Parcela parcela) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfParcela.handle(parcela);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Parcela parcela) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfParcela.handle(parcela);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void runInTransaction(final Runnable action) {
    __db.beginTransaction();
    try {
      ParcelaDao.super.runInTransaction(action);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public int deleteByNombre(final String nombre) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByNombre.acquire();
    int _argIndex = 1;
    if (nombre == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, nombre);
    }
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteByNombre.release(_stmt);
    }
  }

  @Override
  public int deleteParcelasWithPrefix(final String prefix) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteParcelasWithPrefix.acquire();
    int _argIndex = 1;
    if (prefix == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, prefix);
    }
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteParcelasWithPrefix.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Parcela>> getAllParcelas() {
    final String _sql = "SELECT * FROM parcela";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela"}, false, new Callable<List<Parcela>>() {
      @Override
      @Nullable
      public List<Parcela> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfMaxOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "maxOcupantes");
          final int _cursorIndexOfEurPorPersona = CursorUtil.getColumnIndexOrThrow(_cursor, "eurPorPersona");
          final List<Parcela> _result = new ArrayList<Parcela>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Parcela _item;
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final Integer _tmpMaxOcupantes;
            if (_cursor.isNull(_cursorIndexOfMaxOcupantes)) {
              _tmpMaxOcupantes = null;
            } else {
              _tmpMaxOcupantes = _cursor.getInt(_cursorIndexOfMaxOcupantes);
            }
            final Double _tmpEurPorPersona;
            if (_cursor.isNull(_cursorIndexOfEurPorPersona)) {
              _tmpEurPorPersona = null;
            } else {
              _tmpEurPorPersona = _cursor.getDouble(_cursorIndexOfEurPorPersona);
            }
            _item = new Parcela(_tmpNombre,_tmpDescripcion,_tmpMaxOcupantes,_tmpEurPorPersona);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Parcela>> getAvailableParcelas() {
    final String _sql = "SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela",
        "parcela_reservada"}, false, new Callable<List<Parcela>>() {
      @Override
      @Nullable
      public List<Parcela> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfMaxOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "maxOcupantes");
          final int _cursorIndexOfEurPorPersona = CursorUtil.getColumnIndexOrThrow(_cursor, "eurPorPersona");
          final List<Parcela> _result = new ArrayList<Parcela>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Parcela _item;
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final Integer _tmpMaxOcupantes;
            if (_cursor.isNull(_cursorIndexOfMaxOcupantes)) {
              _tmpMaxOcupantes = null;
            } else {
              _tmpMaxOcupantes = _cursor.getInt(_cursorIndexOfMaxOcupantes);
            }
            final Double _tmpEurPorPersona;
            if (_cursor.isNull(_cursorIndexOfEurPorPersona)) {
              _tmpEurPorPersona = null;
            } else {
              _tmpEurPorPersona = _cursor.getDouble(_cursorIndexOfEurPorPersona);
            }
            _item = new Parcela(_tmpNombre,_tmpDescripcion,_tmpMaxOcupantes,_tmpEurPorPersona);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<ParcelaOccupancy>> getParcelasByReservationId(final long reservationId) {
    final String _sql = "SELECT p.*, pr.numOcupantes FROM parcela p INNER JOIN parcela_reservada pr ON p.nombre = pr.parcelaNombre WHERE pr.reservaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reservationId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela",
        "parcela_reservada"}, false, new Callable<List<ParcelaOccupancy>>() {
      @Override
      @Nullable
      public List<ParcelaOccupancy> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfMaxOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "maxOcupantes");
          final int _cursorIndexOfEurPorPersona = CursorUtil.getColumnIndexOrThrow(_cursor, "eurPorPersona");
          final int _cursorIndexOfNumOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "numOcupantes");
          final List<ParcelaOccupancy> _result = new ArrayList<ParcelaOccupancy>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ParcelaOccupancy _item;
            final int _tmpNumOcupantes;
            _tmpNumOcupantes = _cursor.getInt(_cursorIndexOfNumOcupantes);
            final Parcela _tmpParcela;
            if (!(_cursor.isNull(_cursorIndexOfNombre) && _cursor.isNull(_cursorIndexOfDescripcion) && _cursor.isNull(_cursorIndexOfMaxOcupantes) && _cursor.isNull(_cursorIndexOfEurPorPersona))) {
              final String _tmpNombre;
              if (_cursor.isNull(_cursorIndexOfNombre)) {
                _tmpNombre = null;
              } else {
                _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
              }
              final String _tmpDescripcion;
              if (_cursor.isNull(_cursorIndexOfDescripcion)) {
                _tmpDescripcion = null;
              } else {
                _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
              }
              final Integer _tmpMaxOcupantes;
              if (_cursor.isNull(_cursorIndexOfMaxOcupantes)) {
                _tmpMaxOcupantes = null;
              } else {
                _tmpMaxOcupantes = _cursor.getInt(_cursorIndexOfMaxOcupantes);
              }
              final Double _tmpEurPorPersona;
              if (_cursor.isNull(_cursorIndexOfEurPorPersona)) {
                _tmpEurPorPersona = null;
              } else {
                _tmpEurPorPersona = _cursor.getDouble(_cursorIndexOfEurPorPersona);
              }
              _tmpParcela = new Parcela(_tmpNombre,_tmpDescripcion,_tmpMaxOcupantes,_tmpEurPorPersona);
            } else {
              _tmpParcela = null;
            }
            _item = new ParcelaOccupancy(_tmpParcela,_tmpNumOcupantes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Parcela>> getParcelasNotLinkedToReservation(final long reservationId) {
    final String _sql = "SELECT * FROM parcela WHERE nombre NOT IN (SELECT parcelaNombre FROM parcela_reservada WHERE reservaId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reservationId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela",
        "parcela_reservada"}, false, new Callable<List<Parcela>>() {
      @Override
      @Nullable
      public List<Parcela> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfMaxOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "maxOcupantes");
          final int _cursorIndexOfEurPorPersona = CursorUtil.getColumnIndexOrThrow(_cursor, "eurPorPersona");
          final List<Parcela> _result = new ArrayList<Parcela>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Parcela _item;
            final String _tmpNombre;
            if (_cursor.isNull(_cursorIndexOfNombre)) {
              _tmpNombre = null;
            } else {
              _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            }
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final Integer _tmpMaxOcupantes;
            if (_cursor.isNull(_cursorIndexOfMaxOcupantes)) {
              _tmpMaxOcupantes = null;
            } else {
              _tmpMaxOcupantes = _cursor.getInt(_cursorIndexOfMaxOcupantes);
            }
            final Double _tmpEurPorPersona;
            if (_cursor.isNull(_cursorIndexOfEurPorPersona)) {
              _tmpEurPorPersona = null;
            } else {
              _tmpEurPorPersona = _cursor.getDouble(_cursorIndexOfEurPorPersona);
            }
            _item = new Parcela(_tmpNombre,_tmpDescripcion,_tmpMaxOcupantes,_tmpEurPorPersona);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public boolean exists(final String nombre) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM parcela WHERE nombre = ? LIMIT 1)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nombre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nombre);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final boolean _result;
      if (_cursor.moveToFirst()) {
        final int _tmp;
        _tmp = _cursor.getInt(0);
        _result = _tmp != 0;
      } else {
        _result = false;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Parcela getByNombre(final String nombre) {
    final String _sql = "SELECT * FROM parcela WHERE nombre = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nombre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nombre);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
      final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
      final int _cursorIndexOfMaxOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "maxOcupantes");
      final int _cursorIndexOfEurPorPersona = CursorUtil.getColumnIndexOrThrow(_cursor, "eurPorPersona");
      final Parcela _result;
      if (_cursor.moveToFirst()) {
        final String _tmpNombre;
        if (_cursor.isNull(_cursorIndexOfNombre)) {
          _tmpNombre = null;
        } else {
          _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
        }
        final String _tmpDescripcion;
        if (_cursor.isNull(_cursorIndexOfDescripcion)) {
          _tmpDescripcion = null;
        } else {
          _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
        }
        final Integer _tmpMaxOcupantes;
        if (_cursor.isNull(_cursorIndexOfMaxOcupantes)) {
          _tmpMaxOcupantes = null;
        } else {
          _tmpMaxOcupantes = _cursor.getInt(_cursorIndexOfMaxOcupantes);
        }
        final Double _tmpEurPorPersona;
        if (_cursor.isNull(_cursorIndexOfEurPorPersona)) {
          _tmpEurPorPersona = null;
        } else {
          _tmpEurPorPersona = _cursor.getDouble(_cursorIndexOfEurPorPersona);
        }
        _result = new Parcela(_tmpNombre,_tmpDescripcion,_tmpMaxOcupantes,_tmpEurPorPersona);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countParcelas() {
    final String _sql = "SELECT COUNT(*) FROM parcela";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
