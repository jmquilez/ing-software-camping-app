package es.unizar.eina.T213_camping.utils.ModelUtils;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import java.util.Objects;

import es.unizar.eina.T213_camping.database.models.Parcela;

/**
 * Representa una parcela junto con su ocupación actual.
 * Esta clase implementa Parcelable para permitir la transferencia de objetos entre componentes de Android.
 */
public class ParcelaOccupancy implements Parcelable {
    
    /**
     * Parcela asociada a esta ocupación.
     * Se utiliza @Embedded para incluir todos los campos de Parcela en esta clase.
     */
    @Embedded
    private Parcela parcela;

    /**
     * Número actual de ocupantes en la parcela.
     */
    public int numOcupantes;

    /**
     * Constructor principal.
     * @param parcela Parcela asociada
     * @param numOcupantes Número de ocupantes actuales
     */
    public ParcelaOccupancy(Parcela parcela, int numOcupantes) {
        this.parcela = parcela;
        this.numOcupantes = numOcupantes;
    }

    /**
     * Constructor para crear un objeto a partir de un Parcel.
     * @param in Parcel que contiene los datos
     */
    protected ParcelaOccupancy(Parcel in) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcela = in.readParcelable(Parcela.class.getClassLoader(), Parcela.class);
        } else {
            @SuppressWarnings("deprecation")
            Parcela parcela = in.readParcelable(Parcela.class.getClassLoader());
            this.parcela = parcela;
        }
        numOcupantes = in.readInt();
    }

    /**
     * Creator para la interfaz Parcelable.
     */
    public static final Creator<ParcelaOccupancy> CREATOR = new Creator<ParcelaOccupancy>() {
        @Override
        public ParcelaOccupancy createFromParcel(Parcel in) {
            return new ParcelaOccupancy(in);
        }

        @Override
        public ParcelaOccupancy[] newArray(int size) {
            return new ParcelaOccupancy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(parcela, flags);
        dest.writeInt(numOcupantes);
    }

    /**
     * Obtiene la parcela asociada.
     * @return Parcela asociada a esta ocupación
     */
    public Parcela getParcela() {
        return parcela;
    }

    /**
     * Establece la parcela asociada.
     * @param parcela Nueva parcela a asociar
     */
    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    /**
     * Obtiene el número de ocupantes actuales.
     * @return Número de ocupantes
     */
    public int getOccupancy() {
        return numOcupantes;
    }

    /**
     * Establece el número de ocupantes.
     * @param numOcupantes Nuevo número de ocupantes
     */
    public void setOccupancy(int numOcupantes) {
        this.numOcupantes = numOcupantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParcelaOccupancy that = (ParcelaOccupancy) o;
        return numOcupantes == that.getOccupancy() &&
                Objects.equals(parcela, that.getParcela());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcela, numOcupantes);
    }

    @Override
    public String toString() {
        return "ParcelaOccupancy{" +
                "parcela=" + parcela +
                ", numOcupantes=" + numOcupantes +
                '}';
    }
}
