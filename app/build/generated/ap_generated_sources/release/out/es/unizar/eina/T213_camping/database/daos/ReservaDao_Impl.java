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
import es.unizar.eina.T213_camping.database.models.Reserva;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
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

  public ReservaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReserva = new EntityInsertionAdapter<Reserva>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `reserva` (`id`,`nombreCliente`,`fechaEntrada`,`fechaSalida`,`telefonoCliente`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Reserva entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombreCliente() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreCliente());
        }
        if (entity.getFechaEntrada() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFechaEntrada());
        }
        if (entity.getFechaSalida() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFechaSalida());
        }
        if (entity.getTelefonoCliente() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefonoCliente());
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
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReserva = new EntityDeletionOrUpdateAdapter<Reserva>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reserva` SET `id` = ?,`nombreCliente` = ?,`fechaEntrada` = ?,`fechaSalida` = ?,`telefonoCliente` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Reserva entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNombreCliente() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNombreCliente());
        }
        if (entity.getFechaEntrada() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFechaEntrada());
        }
        if (entity.getFechaSalida() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFechaSalida());
        }
        if (entity.getTelefonoCliente() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelefonoCliente());
        }
        statement.bindLong(6, entity.getId());
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
  public void delete(final Reserva reserva) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfReserva.handle(reserva);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Reserva reserva) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReserva.handle(reserva);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public void deleteById(final long reservationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, reservationId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
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
          final List<Reserva> _result = new ArrayList<Reserva>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Reserva _item;
            final String _tmpNombreCliente;
            if (_cursor.isNull(_cursorIndexOfNombreCliente)) {
              _tmpNombreCliente = null;
            } else {
              _tmpNombreCliente = _cursor.getString(_cursorIndexOfNombreCliente);
            }
            final String _tmpFechaEntrada;
            if (_cursor.isNull(_cursorIndexOfFechaEntrada)) {
              _tmpFechaEntrada = null;
            } else {
              _tmpFechaEntrada = _cursor.getString(_cursorIndexOfFechaEntrada);
            }
            final String _tmpFechaSalida;
            if (_cursor.isNull(_cursorIndexOfFechaSalida)) {
              _tmpFechaSalida = null;
            } else {
              _tmpFechaSalida = _cursor.getString(_cursorIndexOfFechaSalida);
            }
            final String _tmpTelefonoCliente;
            if (_cursor.isNull(_cursorIndexOfTelefonoCliente)) {
              _tmpTelefonoCliente = null;
            } else {
              _tmpTelefonoCliente = _cursor.getString(_cursorIndexOfTelefonoCliente);
            }
            _item = new Reserva(_tmpNombreCliente,_tmpFechaEntrada,_tmpFechaSalida,_tmpTelefonoCliente);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
