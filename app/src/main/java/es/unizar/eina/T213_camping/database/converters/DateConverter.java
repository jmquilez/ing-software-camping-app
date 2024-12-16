package es.unizar.eina.T213_camping.database.converters;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * Clase que proporciona métodos de conversión para almacenar y recuperar fechas en la base de datos Room.
 * Convierte entre objetos Date y Long para permitir el almacenamiento de fechas en SQLite.
 *
 * @author Grupo T213
 * @version 1.0
 * @see Date
 */
public class DateConverter {
    /**
     * Convierte un valor Long (timestamp) a un objeto Date.
     *
     * @param value timestamp en milisegundos desde epoch, puede ser null
     * @note See https://betulnecanli.medium.com/how-to-store-date-in-room-database-d06dec3a2d7e
     * @return objeto Date correspondiente al timestamp, o null si el input es null
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convierte un objeto Date a su representación en Long (timestamp).
     *
     * @param date objeto Date a convertir, puede ser null
     * @return timestamp en milisegundos desde epoch, o null si el input es null
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}