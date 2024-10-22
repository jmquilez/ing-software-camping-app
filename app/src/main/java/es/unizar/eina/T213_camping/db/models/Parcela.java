package es.unizar.eina.T213_camping.db.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parcela")
public class Parcela {

    @PrimaryKey
    @NonNull
    private String nombre;
    @NonNull
    private String descripcion;
    @NonNull
    private int maxOcupantes;
    @NonNull
    private double eurPorPersona;

    // Constructor
    public Parcela(String nombre, String descripcion, int maxOcupantes, double eurPorPersona) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.maxOcupantes = maxOcupantes;
        this.eurPorPersona = eurPorPersona;
    }

    // Getters and Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMaxOcupantes() {
        return maxOcupantes;
    }

    public void setMaxOcupantes(int maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    public double getEurPorPersona() {
        return eurPorPersona;
    }

    public void setEurPorPersona(double eurPorPersona) {
        this.eurPorPersona = eurPorPersona;
    }
}
