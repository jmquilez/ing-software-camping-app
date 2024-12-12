package es.unizar.eina.T213_camping.database.converters;

import androidx.room.TypeConverter;
import java.util.Date;

public class DateConverter {
    // Note: See https://betulnecanli.medium.com/how-to-store-date-in-room-database-d06dec3a2d7e
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}