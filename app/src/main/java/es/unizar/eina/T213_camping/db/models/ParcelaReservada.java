package es.unizar.eina.T213_camping.db.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(
    tableName = "parcela_reservada",
    primaryKeys = {"parcelaNombre", "reservaId"},
    foreignKeys = {
        @ForeignKey(entity = Parcela.class,
                    parentColumns = "nombre",
                    childColumns = "parcelaNombre",
                    onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Reserva.class,
                    parentColumns = "id",
                    childColumns = "reservaId",
                    onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index(value = "reservaId")}
)
public class ParcelaReservada {

    @NonNull
    private String parcelaNombre;
    @NonNull
    private long reservaId;
    @NonNull
    private int numOcupantes;

    // Constructor
    public ParcelaReservada(String parcelaNombre, long reservaId, int numOcupantes) {
        this.parcelaNombre = parcelaNombre;
        this.reservaId = reservaId;
        this.numOcupantes = numOcupantes;
    }

    // Getters and Setters

    public String getParcelaNombre() {
        return parcelaNombre;
    }

    // NOTE: `parcelaNombre` is part of the primary key
    /*public void setParcelaNombre(String parcelaNombre) {
        this.parcelaNombre = parcelaNombre;
    }*/

    public long getReservaId() {
        return reservaId;
    }

    // NOTE: `reservaId` is part of the primary key
    /*public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }*/

    public int getNumOcupantes() {
        return numOcupantes;
    }

    public void setNumOcupantes(int numOcupantes) {
        this.numOcupantes = numOcupantes;
    }
}
