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
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
public final class ParcelaReservadaDao_Impl implements ParcelaReservadaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ParcelaReservada> __insertionAdapterOfParcelaReservada;

  private final EntityDeletionOrUpdateAdapter<ParcelaReservada> __deletionAdapterOfParcelaReservada;

  private final EntityDeletionOrUpdateAdapter<ParcelaReservada> __updateAdapterOfParcelaReservada;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteParcelasForReservation;

  private final SharedSQLiteStatement __preparedStmtOfUpdateParcelaNombre;

  public ParcelaReservadaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfParcelaReservada = new EntityInsertionAdapter<ParcelaReservada>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `parcela_reservada` (`parcelaNombre`,`reservaId`,`numOcupantes`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ParcelaReservada entity) {
        if (entity.getParcelaNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getParcelaNombre());
        }
        if (entity.getReservaId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getReservaId());
        }
        if (entity.getNumOcupantes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getNumOcupantes());
        }
      }
    };
    this.__deletionAdapterOfParcelaReservada = new EntityDeletionOrUpdateAdapter<ParcelaReservada>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `parcela_reservada` WHERE `parcelaNombre` = ? AND `reservaId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ParcelaReservada entity) {
        if (entity.getParcelaNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getParcelaNombre());
        }
        if (entity.getReservaId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getReservaId());
        }
      }
    };
    this.__updateAdapterOfParcelaReservada = new EntityDeletionOrUpdateAdapter<ParcelaReservada>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `parcela_reservada` SET `parcelaNombre` = ?,`reservaId` = ?,`numOcupantes` = ? WHERE `parcelaNombre` = ? AND `reservaId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ParcelaReservada entity) {
        if (entity.getParcelaNombre() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getParcelaNombre());
        }
        if (entity.getReservaId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getReservaId());
        }
        if (entity.getNumOcupantes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getNumOcupantes());
        }
        if (entity.getParcelaNombre() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getParcelaNombre());
        }
        if (entity.getReservaId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getReservaId());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM parcela_reservada";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteParcelasForReservation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM parcela_reservada WHERE reservaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateParcelaNombre = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE parcela_reservada SET parcelaNombre = ? WHERE parcelaNombre = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final ParcelaReservada parcelaReservada) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfParcelaReservada.insertAndReturnId(parcelaReservada);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final ParcelaReservada parcelaReservada) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfParcelaReservada.handle(parcelaReservada);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final ParcelaReservada parcelaReservada) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfParcelaReservada.handle(parcelaReservada);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public boolean updateParcelasForReservation(final long reservationId,
      final List<ParcelaReservada> updatedParcels) {
    __db.beginTransaction();
    try {
      final boolean _result;
      _result = ParcelaReservadaDao.super.updateParcelasForReservation(reservationId, updatedParcels);
      __db.setTransactionSuccessful();
      return _result;
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
  public int deleteParcelasForReservation(final long reservationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteParcelasForReservation.acquire();
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
      __preparedStmtOfDeleteParcelasForReservation.release(_stmt);
    }
  }

  @Override
  public int updateParcelaNombre(final String oldName, final String newName) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateParcelaNombre.acquire();
    int _argIndex = 1;
    if (newName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, newName);
    }
    _argIndex = 2;
    if (oldName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, oldName);
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
      __preparedStmtOfUpdateParcelaNombre.release(_stmt);
    }
  }

  @Override
  public LiveData<List<ParcelaReservada>> getAllParcelaReservadas() {
    final String _sql = "SELECT * FROM parcela_reservada";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela_reservada"}, false, new Callable<List<ParcelaReservada>>() {
      @Override
      @Nullable
      public List<ParcelaReservada> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfParcelaNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "parcelaNombre");
          final int _cursorIndexOfReservaId = CursorUtil.getColumnIndexOrThrow(_cursor, "reservaId");
          final int _cursorIndexOfNumOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "numOcupantes");
          final List<ParcelaReservada> _result = new ArrayList<ParcelaReservada>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ParcelaReservada _item;
            final String _tmpParcelaNombre;
            if (_cursor.isNull(_cursorIndexOfParcelaNombre)) {
              _tmpParcelaNombre = null;
            } else {
              _tmpParcelaNombre = _cursor.getString(_cursorIndexOfParcelaNombre);
            }
            final Long _tmpReservaId;
            if (_cursor.isNull(_cursorIndexOfReservaId)) {
              _tmpReservaId = null;
            } else {
              _tmpReservaId = _cursor.getLong(_cursorIndexOfReservaId);
            }
            final Integer _tmpNumOcupantes;
            if (_cursor.isNull(_cursorIndexOfNumOcupantes)) {
              _tmpNumOcupantes = null;
            } else {
              _tmpNumOcupantes = _cursor.getInt(_cursorIndexOfNumOcupantes);
            }
            _item = new ParcelaReservada(_tmpParcelaNombre,_tmpReservaId,_tmpNumOcupantes);
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
  public LiveData<List<ParcelaReservada>> getAllParcelaReservadasForReserva(final int id) {
    final String _sql = "SELECT * FROM parcela_reservada WHERE reservaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela_reservada"}, false, new Callable<List<ParcelaReservada>>() {
      @Override
      @Nullable
      public List<ParcelaReservada> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfParcelaNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "parcelaNombre");
          final int _cursorIndexOfReservaId = CursorUtil.getColumnIndexOrThrow(_cursor, "reservaId");
          final int _cursorIndexOfNumOcupantes = CursorUtil.getColumnIndexOrThrow(_cursor, "numOcupantes");
          final List<ParcelaReservada> _result = new ArrayList<ParcelaReservada>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ParcelaReservada _item;
            final String _tmpParcelaNombre;
            if (_cursor.isNull(_cursorIndexOfParcelaNombre)) {
              _tmpParcelaNombre = null;
            } else {
              _tmpParcelaNombre = _cursor.getString(_cursorIndexOfParcelaNombre);
            }
            final Long _tmpReservaId;
            if (_cursor.isNull(_cursorIndexOfReservaId)) {
              _tmpReservaId = null;
            } else {
              _tmpReservaId = _cursor.getLong(_cursorIndexOfReservaId);
            }
            final Integer _tmpNumOcupantes;
            if (_cursor.isNull(_cursorIndexOfNumOcupantes)) {
              _tmpNumOcupantes = null;
            } else {
              _tmpNumOcupantes = _cursor.getInt(_cursorIndexOfNumOcupantes);
            }
            _item = new ParcelaReservada(_tmpParcelaNombre,_tmpReservaId,_tmpNumOcupantes);
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
  public LiveData<List<Parcela>> getAvailableParcelasForReserva(final String reservationId) {
    final String _sql = "SELECT p.* FROM parcela p WHERE p.nombre NOT IN (SELECT pr.parcelaNombre FROM parcela_reservada pr WHERE pr.reservaId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (reservationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, reservationId);
    }
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
  public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervalo(final Date fechaInicio,
      final Date fechaFin) {
    final String _sql = "SELECT p.* FROM parcela p WHERE p.nombre NOT IN (SELECT pr.parcelaNombre FROM parcela_reservada pr JOIN reserva r ON pr.reservaId = r.id WHERE r.fechaEntrada < ? AND r.fechaSalida > ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = DateConverter.dateToTimestamp(fechaFin);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = DateConverter.dateToTimestamp(fechaInicio);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela",
        "parcela_reservada", "reserva"}, false, new Callable<List<Parcela>>() {
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
  public LiveData<List<Parcela>> getParcelasDisponiblesEnIntervaloExcludingReservation(
      final Date startDate, final Date endDate, final long excludeReservationId) {
    final String _sql = "SELECT p.* FROM parcela p WHERE p.nombre NOT IN (SELECT pr.parcelaNombre FROM parcela_reservada pr JOIN reserva r ON pr.reservaId = r.id WHERE pr.reservaId != ? AND r.fechaEntrada < ? AND r.fechaSalida > ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, excludeReservationId);
    _argIndex = 2;
    final Long _tmp = DateConverter.dateToTimestamp(endDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 3;
    final Long _tmp_1 = DateConverter.dateToTimestamp(startDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"parcela",
        "parcela_reservada", "reserva"}, false, new Callable<List<Parcela>>() {
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
  public boolean exists(final String parcelaNombre, final long reservaId) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM parcela_reservada WHERE parcelaNombre = ? AND reservaId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (parcelaNombre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, parcelaNombre);
    }
    _argIndex = 2;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
