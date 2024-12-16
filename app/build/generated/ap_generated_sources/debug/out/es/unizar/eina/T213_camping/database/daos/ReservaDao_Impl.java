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
import es.unizar.eina.T213_camping.database.converters.DateConverter;
import es.unizar.eina.T213_camping.database.models.Reserva;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReservaDao_Impl implements ReservaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Reserva> __insertionAdapterOfReserva;

  private final EntityDeletionOrUpdateAdapter<Reserva> __deletionAdapterOfReserva;

  private final EntityDeletionOrUpdateAdapter<Reserva> __updateAdapterOfReserva;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReservasWithClientNamePrefix;

  public ReservaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReserva = new EntityInsertionAdapter<Reserva>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `reserva` (`id`,`nombreCliente`,`fechaEntrada`,`fechaSalida`,`telefonoCliente`,`precio`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Reserva entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getNombreCliente() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreCliente());
        }
        final Long _tmp = DateConverter.dateToTimestamp(entity.getFechaEntrada());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        final Long _tmp_1 = DateConverter.dateToTimestamp(entity.getFechaSalida());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        if (entity.getTelefonoCliente() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefonoCliente());
        }
        if (entity.getPrecio() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getPrecio());
        }
      }
    };
    this.__deletionAdapterOfReserva = new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reserva` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Reserva entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfReserva = new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reserva` SET `id` = ?,`nombreCliente` = ?,`fechaEntrada` = ?,`fechaSalida` = ?,`telefonoCliente` = ?,`precio` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Reserva entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getNombreCliente() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreCliente());
        }
        final Long _tmp = DateConverter.dateToTimestamp(entity.getFechaEntrada());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        final Long _tmp_1 = DateConverter.dateToTimestamp(entity.getFechaSalida());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        if (entity.getTelefonoCliente() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefonoCliente());
        }
        if (entity.getPrecio() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getPrecio());
        }
        if (entity.getId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reserva";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reserva WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteReservasWithClientNamePrefix = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reserva WHERE nombreCliente IN (SELECT nombreCliente FROM reserva WHERE CAST(nombreCliente AS TEXT) LIKE ? || '%')";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Reserva reserva) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfReserva.insertAndReturnId(reserva);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Reserva reserva) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfReserva.handle(reserva);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Reserva reserva) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfReserva.handle(reserva);
      __db.setTransactionSuccessful();
      return _total;
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
  public int deleteById(final long reservationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, reservationId);
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
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public int deleteReservasWithClientNamePrefix(final String prefix) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteReservasWithClientNamePrefix.acquire();
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
      __preparedStmtOfDeleteReservasWithClientNamePrefix.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Reserva>> getAllReservas() {
    final String _sql = "SELECT * FROM reserva";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"reserva"}, false, new Callable<List<Reserva>>() {
      @Override
      @Nullable
      public List<Reserva> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombreCliente = CursorUtil.getColumnIndexOrThrow(_cursor, "nombreCliente");
          final int _cursorIndexOfFechaEntrada = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaEntrada");
          final int _cursorIndexOfFechaSalida = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaSalida");
          final int _cursorIndexOfTelefonoCliente = CursorUtil.getColumnIndexOrThrow(_cursor, "telefonoCliente");
          final int _cursorIndexOfPrecio = CursorUtil.getColumnIndexOrThrow(_cursor, "precio");
          final List<Reserva> _result = new ArrayList<Reserva>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Reserva _item;
            final String _tmpNombreCliente;
            if (_cursor.isNull(_cursorIndexOfNombreCliente)) {
              _tmpNombreCliente = null;
            } else {
              _tmpNombreCliente = _cursor.getString(_cursorIndexOfNombreCliente);
            }
            final Date _tmpFechaEntrada;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfFechaEntrada)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfFechaEntrada);
            }
            _tmpFechaEntrada = DateConverter.fromTimestamp(_tmp);
            final Date _tmpFechaSalida;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfFechaSalida)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfFechaSalida);
            }
            _tmpFechaSalida = DateConverter.fromTimestamp(_tmp_1);
            final String _tmpTelefonoCliente;
            if (_cursor.isNull(_cursorIndexOfTelefonoCliente)) {
              _tmpTelefonoCliente = null;
            } else {
              _tmpTelefonoCliente = _cursor.getString(_cursorIndexOfTelefonoCliente);
            }
            final Double _tmpPrecio;
            if (_cursor.isNull(_cursorIndexOfPrecio)) {
              _tmpPrecio = null;
            } else {
              _tmpPrecio = _cursor.getDouble(_cursorIndexOfPrecio);
            }
            _item = new Reserva(_tmpNombreCliente,_tmpFechaEntrada,_tmpFechaSalida,_tmpTelefonoCliente,_tmpPrecio);
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
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
  public boolean exists(final long reservaId) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM reserva WHERE id = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, reservaId);
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
  public int countReservas() {
    final String _sql = "SELECT COUNT(*) FROM reserva";
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
