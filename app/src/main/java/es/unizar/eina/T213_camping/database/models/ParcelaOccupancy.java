package es.unizar.eina.T213_camping.database.models;

import androidx.room.Embedded;

import java.io.Serializable;
import java.util.Objects;

public class ParcelaOccupancy implements Serializable {
    @Embedded
    private Parcela parcela;
    public int numOcupantes;

    public ParcelaOccupancy(Parcela parcela, int numOcupantes) {
        this.parcela = parcela;
        this.numOcupantes = numOcupantes;
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
