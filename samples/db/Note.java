package es.unizar.eina.notepad.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa una nota y que consta de título y cuerpo */
@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    public Note(@NonNull String title, String body) {
        this.title = title;
        this.body = body;
    }

    /** Devuelve el identificador de la nota */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de una nota */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el título de la nota */
    public String getTitle(){
        return this.title;
    }

    /** Devuelve el cuerpo de la nota */
    public String getBody(){
        return this.body;
    }

}
