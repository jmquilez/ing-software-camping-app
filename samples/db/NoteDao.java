package es.unizar.eina.notepad.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para las notas */
@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Note note);

    @Update
    int update(Note note);

    @Delete
    int delete(Note note);

    @Query("DELETE FROM note")
    void deleteAll();

    @Query("SELECT * FROM note ORDER BY title ASC")
    LiveData<List<Note>> getOrderedNotes();
}

