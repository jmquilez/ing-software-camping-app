package es.unizar.eina.T213_camping.database.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parcela")
public class Parcela implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @NonNull
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @NonNull
    @ColumnInfo(name = "maxOcupantes")
    private Integer maxOcupantes;

    @NonNull
    @ColumnInfo(name = "eurPorPersona")
    private Double eurPorPersona;

    // Constructor
    public Parcela(@NonNull String nombre, @NonNull String descripcion, @NonNull Integer maxOcupantes, @NonNull Double eurPorPersona) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.maxOcupantes = maxOcupantes;
        this.eurPorPersona = eurPorPersona;
    }

    // Parcelable constructor
    protected Parcela(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        maxOcupantes = in.readInt();
        eurPorPersona = in.readDouble();
    }

    // Parcelable CREATOR
    public static final Creator<Parcela> CREATOR = new Creator<Parcela>() {
        @Override
        public Parcela createFromParcel(Parcel in) {
            return new Parcela(in);
        }

        @Override
        public Parcela[] newArray(int size) {
            return new Parcela[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(maxOcupantes);
        dest.writeDouble(eurPorPersona);
    }

    // Getters and Setters

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMaxOcupantes() {
        return maxOcupantes;
    }

    public void setMaxOcupantes(Integer maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    public Double getEurPorPersona() {
        return eurPorPersona;
    }

    public void setEurPorPersona(double eurPorPersona) {
        this.eurPorPersona = eurPorPersona;
    }

    public boolean equals(Parcela other) {
        return this.nombre.equals(other.nombre) &&
                this.descripcion.equals(other.descripcion) &&
                this.eurPorPersona.equals(other.eurPorPersona) &&
                this.maxOcupantes.equals(other.maxOcupantes);
    }
}
