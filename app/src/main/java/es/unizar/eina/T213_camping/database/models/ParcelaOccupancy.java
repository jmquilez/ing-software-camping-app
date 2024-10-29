package es.unizar.eina.T213_camping.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import java.util.Objects;

public class ParcelaOccupancy implements Parcelable {
    @Embedded
    private Parcela parcela;
    public int numOcupantes;

    public ParcelaOccupancy(Parcela parcela, int numOcupantes) {
        this.parcela = parcela;
        this.numOcupantes = numOcupantes;
    }

    // Parcelable constructor
    protected ParcelaOccupancy(Parcel in) {
        parcela = in.readParcelable(Parcela.class.getClassLoader());
        numOcupantes = in.readInt();
    }

    // Parcelable CREATOR
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

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public int getOccupancy() {
        return numOcupantes;
    }

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
