package es.unizar.eina.T213_camping.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Representa una reserva en el camping.
 * Almacena la información relacionada con una reserva específica, incluyendo datos del cliente y fechas.
 */
@Entity(tableName = "reserva")
public class Reserva {

    /**
     * Identificador único de la reserva, generado automáticamente.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    /**
     * Nombre del cliente que realiza la reserva.
     */
    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    /**
     * Fecha de entrada del cliente.
     */
    @NonNull
    @ColumnInfo(name = "fechaEntrada")
    private Date fechaEntrada;

    /**
     * Fecha de salida del cliente.
     */
    @NonNull
    @ColumnInfo(name = "fechaSalida")
    private Date fechaSalida;

    /**
     * Número de teléfono del cliente.
     */
    @NonNull
    @ColumnInfo(name = "telefonoCliente")
    private String telefonoCliente;

    /**
     * Precio de la reserva.
     */
    @NonNull
    @ColumnInfo(name = "precio")
    private Double precio;

    // Constructor
    public Reserva(@NonNull String nombreCliente, @NonNull Date fechaEntrada, @NonNull Date fechaSalida, @NonNull String telefonoCliente, @NonNull Double precio) {
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.telefonoCliente = telefonoCliente;
        this.precio = precio;
    }

    // Getters and Setters

    /**
     * Obtiene el ID único de la reserva.
     * @return ID de la reserva
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el ID de la reserva.
     * @param id Nuevo ID para la reserva
     */
    public void setId(long id) { this.id = id; }

    // NOTE: the id is immutable, it cannot be set

    /**
     * Obtiene el nombre del cliente que realizó la reserva.
     * @return Nombre del cliente
     */
    @NonNull
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Establece el nombre del cliente que realiza la reserva.
     * @param nombreCliente Nuevo nombre del cliente
     */
    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Obtiene la fecha de entrada de la reserva.
     * @return Fecha de entrada en formato YYYY-MM-DD
     */
    @NonNull
    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    /**
     * Establece la fecha de entrada de la reserva.
     * @param fechaEntrada Nueva fecha de entrada en formato YYYY-MM-DD
     */
    public void setFechaEntrada(@NonNull Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    /**
     * Obtiene la fecha de salida de la reserva.
     * @return Fecha de salida en formato YYYY-MM-DD
     */
    @NonNull
    public Date getFechaSalida() {
        return fechaSalida;
    }

    /**
     * Establece la fecha de salida de la reserva.
     * @param fechaSalida Nueva fecha de salida en formato YYYY-MM-DD
     */
    public void setFechaSalida(@NonNull Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * @return Número de teléfono del cliente
     */
    @NonNull
    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    /**
     * Establece el número de teléfono del cliente.
     * @param telefonoCliente Nuevo número de teléfono del cliente
     */
    public void setTelefonoCliente(@NonNull String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    /**
     * Obtiene el precio de la reserva.
     * @return Precio de la reserva
     */
    @NonNull
    public Double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de la reserva.
     * @param precio Nuevo precio para la reserva
     * @throws IllegalArgumentException si el precio es nulo
     */
    public void setPrecio(@NonNull Double precio) {
        this.precio = precio;
    }

    /**
     * Compara esta reserva con otra para determinar si son iguales.
     * Dos reservas se consideran iguales si todos sus campos coinciden.
     * @param other Reserva con la que se compara
     * @return true si las reservas son iguales, false en caso contrario
     */
    public boolean equals(Reserva other) {
        return this.nombreCliente.equals(other.nombreCliente) &&
                this.fechaEntrada.equals(other.fechaEntrada) &&
                this.fechaSalida.equals(other.fechaSalida) &&
                this.telefonoCliente.equals(other.telefonoCliente) &&
                this.id == other.id;
    }
}
