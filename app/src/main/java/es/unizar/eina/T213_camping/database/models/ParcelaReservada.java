package es.unizar.eina.T213_camping.database.models;

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
    @ColumnInfo(name = "parcelaNombre")
    private String parcelaNombre;

    @NonNull
    @ColumnInfo(name = "reservaId")
    private Long reservaId;

    @NonNull
    @ColumnInfo(name = "numOcupantes")
    private Integer numOcupantes;

    // Constructor
    public ParcelaReservada(@NonNull String parcelaNombre, @NonNull Long reservaId, @NonNull Integer numOcupantes) {
        this.parcelaNombre = parcelaNombre;
        this.reservaId = reservaId;
        this.numOcupantes = numOcupantes;
    }

    // Getters and Setters

    @NonNull
    public String getParcelaNombre() {
        return parcelaNombre;
    }

    // NOTE: `parcelaNombre` is part of the primary key
    /*public void setParcelaNombre(String parcelaNombre) {
        this.parcelaNombre = parcelaNombre;
    }*/

    public Long getReservaId() {
        return reservaId;
    }

    // NOTE: `reservaId` is part of the primary key
    /*public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }*/

    public Integer getNumOcupantes() {
        return numOcupantes;
    }

    public void setNumOcupantes(Integer numOcupantes) {
        this.numOcupantes = numOcupantes;
    }
}
