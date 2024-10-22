package es.unizar.eina.T213_camping.db.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reserva")
public class Reserva {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    // TODO: convert to actual date types
    @NonNull
    private String nombreCliente;
    @NonNull
    private String fechaEntrada;
    @NonNull
    private String fechaSalida;
    @NonNull
    private String telefonoCliente;

    // Constructor
    public Reserva(String nombreCliente, String fechaEntrada, String fechaSalida, String telefonoCliente) {
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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }
}
