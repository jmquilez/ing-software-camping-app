package es.unizar.eina.T213_camping.database.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;
import androidx.room.IntRange;

/**
 * Representa la relación entre una parcela y una reserva.
 * Esta clase implementa una relación muchos a muchos entre Parcela y Reserva,
 * incluyendo información adicional sobre la ocupación.
 */
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

    /**
     * Nombre de la parcela reservada.
     * Forma parte de la clave primaria compuesta.
     */
    @NonNull
    @ColumnInfo(name = "parcelaNombre")
    private String parcelaNombre;

    /**
     * ID de la reserva asociada.
     * Forma parte de la clave primaria compuesta.
     */
    @NonNull
    @ColumnInfo(name = "reservaId")
    private Long reservaId;

    /**
     * Número de ocupantes para esta reserva específica.
     */
    @NonNull
    @ColumnInfo(name = "numOcupantes")
    @IntRange(from = 1, to = 999)
    private Integer numOcupantes;

    /**
     * Constructor para crear una nueva relación parcela-reserva.
     * @param parcelaNombre Nombre de la parcela
     * @param reservaId ID de la reserva
     * @param numOcupantes Número de ocupantes para esta reserva
     */
    public ParcelaReservada(@NonNull String parcelaNombre, @NonNull Long reservaId, @NonNull Integer numOcupantes) {
        this.parcelaNombre = parcelaNombre;
        this.reservaId = reservaId;
        this.numOcupantes = numOcupantes;
    }

    /**
     * Obtiene el nombre de la parcela.
     * @return Nombre de la parcela
     */
    @NonNull
    public String getParcelaNombre() {
        return parcelaNombre;
    }

    // NOTE: parcelaNombre no tiene setter por ser parte de la clave primaria

    /**
     * Obtiene el ID de la reserva.
     * @return ID de la reserva
     */
    public Long getReservaId() {
        return reservaId;
    }

    // NOTE: reservaId no tiene setter por ser parte de la clave primaria

    /**
     * Obtiene el número de ocupantes.
     * @return Número de ocupantes
     */
    public Integer getNumOcupantes() {
        return numOcupantes;
    }

    /**
     * Establece el número de ocupantes.
     * @param numOcupantes Nuevo número de ocupantes
     */
    public void setNumOcupantes(Integer numOcupantes) {
        this.numOcupantes = numOcupantes;
    }
}
