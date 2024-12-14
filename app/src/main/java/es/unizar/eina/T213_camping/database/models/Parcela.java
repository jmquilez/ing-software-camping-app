package es.unizar.eina.T213_camping.database.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.IntRange;
import androidx.annotation.FloatRange;
import androidx.annotation.Size;

/**
 * Representa una parcela en el camping.
 * Esta clase implementa Parcelable para permitir la transferencia de objetos Parcela entre componentes de Android.
 */
@Entity(tableName = "parcela")
public class Parcela implements Parcelable {
    /**
     * Nombre único de la parcela que actúa como identificador primario.
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "nombre")
    @Size(max = 60)
    private String nombre;

    /**
     * Descripción detallada de la parcela.
     */
    @NonNull
    @ColumnInfo(name = "descripcion")
    @Size(max = 300)
    private String descripcion;

    /**
     * Número máximo de ocupantes permitidos en la parcela.
     */
    @NonNull
    @ColumnInfo(name = "maxOcupantes")
    @IntRange(from = 1, to = 999)
    private Integer maxOcupantes;

    /**
     * Precio por persona por noche en euros.
     */
    @NonNull
    @ColumnInfo(name = "eurPorPersona")
    @FloatRange(from = 0.0, to = 999.0)
    private Double eurPorPersona;

    /**
     * Constructor principal para crear una nueva parcela.
     * @param nombre Nombre único de la parcela
     * @param descripcion Descripción de la parcela
     * @param maxOcupantes Capacidad máxima de personas
     * @param eurPorPersona Precio por persona por noche
     */
    public Parcela(@NonNull String nombre, @NonNull String descripcion, @NonNull Integer maxOcupantes, @NonNull Double eurPorPersona) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.maxOcupantes = maxOcupantes;
        this.eurPorPersona = eurPorPersona;
    }

    /**
     * Constructor para crear una parcela a partir de un objeto Parcel.
     * @param in Objeto Parcel que contiene los datos de la parcela
     */
    protected Parcela(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        maxOcupantes = in.readInt();
        eurPorPersona = in.readDouble();
    }

    /**
     * Implementación del Creator de Parcelable.
     * Permite crear objetos Parcela a partir de un Parcel.
     */
    public static final Creator<Parcela> CREATOR = new Creator<Parcela>() {
        /**
         * Crea una nueva instancia de Parcela a partir de un Parcel.
         * @param in Parcel que contiene los datos de la parcela
         * @return Nueva instancia de Parcela
         */
        @Override
        public Parcela createFromParcel(Parcel in) {
            return new Parcela(in);
        }

        /**
         * Crea un array de Parcelas del tamaño especificado.
         * @param size Tamaño del array a crear
         * @return Array de Parcelas del tamaño especificado
         */
        @Override
        public Parcela[] newArray(int size) {
            return new Parcela[size];
        }
    };

    /**
     * Describe los tipos especiales de objetos contenidos en la representación Parcelable.
     * @return 0, ya que no hay tipos especiales
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Escribe el objeto en un Parcel.
     * @param dest Parcel donde se escribirá el objeto
     * @param flags Flags adicionales sobre cómo debe escribirse el objeto
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(maxOcupantes);
        dest.writeDouble(eurPorPersona);
    }

    /**
     * Getters and Setters
     */

    /**
     * Obtiene el nombre único de la parcela.
     * @return Nombre único de la parcela
     */
    @NonNull
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre único de la parcela.
     * @param nombre Nombre único de la parcela
     */
    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada de la parcela.
     * @return Descripción de la parcela
     */
    @NonNull
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada de la parcela.
     * @param descripcion Descripción de la parcela
     */
    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el número máximo de ocupantes permitidos en la parcela.
     * @return Capacidad máxima de personas
     */
    public Integer getMaxOcupantes() {
        return maxOcupantes;
    }

    /**
     * Establece el número máximo de ocupantes permitidos en la parcela.
     * @param maxOcupantes Capacidad máxima de personas
     */
    public void setMaxOcupantes(Integer maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    /**
     * Obtiene el precio por persona por noche en euros.
     * @return Precio por persona por noche
     */
    public Double getEurPorPersona() {
        return eurPorPersona;
    }

    /**
     * Establece el precio por persona por noche en euros.
     * @param eurPorPersona Precio por persona por noche
     */
    public void setEurPorPersona(double eurPorPersona) {
        this.eurPorPersona = eurPorPersona;
    }

    /**
     * Compara esta parcela con otra para determinar si son iguales.
     * @param other Parcela con la que se compara
     * @return true si las parcelas son iguales, false en caso contrario
     */
    public boolean equals(Parcela other) {
        return this.nombre.equals(other.nombre) &&
                this.descripcion.equals(other.descripcion) &&
                this.eurPorPersona.equals(other.eurPorPersona) &&
                this.maxOcupantes.equals(other.maxOcupantes);
    }
}
