package es.unizar.eina.T213_camping.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reserva")
public class Reserva {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    // TODO: convert to actual date types
    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @NonNull
    @ColumnInfo(name = "fechaEntrada")
    private String fechaEntrada;

    @NonNull
    @ColumnInfo(name = "fechaSalida")
    private String fechaSalida;

    @NonNull
    @ColumnInfo(name = "telefonoCliente")
    private String telefonoCliente;

    // Constructor
    public Reserva(@NonNull String nombreCliente, @NonNull String fechaEntrada, @NonNull String fechaSalida, @NonNull String telefonoCliente) {
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.telefonoCliente = telefonoCliente;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }
    public void setId(long id) { this.id = id; }

    // NOTE: the id is immutable, it cannot be set

    @NonNull
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    @NonNull
    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(@NonNull String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @NonNull
    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(@NonNull String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    @NonNull
    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(@NonNull String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public boolean equals(Reserva other) {
        return this.nombreCliente.equals(other.nombreCliente) &&
                this.fechaEntrada.equals(other.fechaEntrada) &&
                this.fechaSalida.equals(other.fechaSalida) &&
                this.telefonoCliente.equals(other.telefonoCliente) &&
                this.id == other.id;
    }
}
