package es.unizar.eina.T213_camping.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utilidades para el manejo de fechas en la aplicación.
 * Proporciona métodos para mostrar selectores de fecha y validar fechas.
 */
public class DateUtils {

    /**
     * Formato estándar de fecha usado en toda la aplicación.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    /**
     * Muestra un diálogo para seleccionar una fecha.
     * @param context Contexto de la aplicación
     * @param isCheckIn true si es fecha de entrada, false si es fecha de salida
     * @param dateInput Botón que mostrará la fecha seleccionada
     * @param onDateSet Runnable a ejecutar después de seleccionar la fecha
     */
    public static void showDatePickerDialog(Context context, boolean isCheckIn, Button dateInput, Runnable onDateSet) {
        Calendar calendar = Calendar.getInstance();
        
        // Try to parse existing date from button
        try {
            Date currentDate = DATE_FORMAT.parse(dateInput.getText().toString());
            if (currentDate != null) {
                calendar.setTime(currentDate);
            }
        } catch (ParseException e) {
            // Keep default date if parsing fails
        }
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            String date = DATE_FORMAT.format(calendar.getTime());
            dateInput.setText(date);
            onDateSet.run();
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Verifica si la fecha de salida es anterior a la fecha de entrada.
     * @param checkInDate Fecha de entrada en formato dd/MM/yyyy
     * @param checkOutDate Fecha de salida en formato dd/MM/yyyy
     * @return true si la fecha de salida es anterior a la de entrada
     */
    public static boolean isCheckOutBeforeCheckIn(String checkInDate, String checkOutDate) {
        String[] checkInParts = checkInDate.split("/");
        String[] checkOutParts = checkOutDate.split("/");

        Calendar checkInCal = Calendar.getInstance();
        checkInCal.set(Integer.parseInt(checkInParts[2]), Integer.parseInt(checkInParts[1]) - 1, Integer.parseInt(checkInParts[0]));

        Calendar checkOutCal = Calendar.getInstance();
        checkOutCal.set(Integer.parseInt(checkOutParts[2]), Integer.parseInt(checkOutParts[1]) - 1, Integer.parseInt(checkOutParts[0]));

        return checkOutCal.before(checkInCal);
    }

    /**
     * Formatea una fecha al formato dd/MM/yyyy
     * @param date Fecha a formatear
     * @return String con la fecha formateada
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Valida que las fechas de entrada y salida sean correctas.
     * La fecha de salida debe ser al menos un día después de la fecha de entrada.
     * @param checkInDate Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @return null si las fechas son válidas, mensaje de error en caso contrario
     */
    public static String validateDates(Date checkInDate, Date checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return "Por favor, seleccione ambas fechas";
        }

        // Set check-in date to start of day for comparison
        Calendar checkInCal = Calendar.getInstance();
        checkInCal.setTime(checkInDate);
        checkInCal.set(Calendar.HOUR_OF_DAY, 0);
        checkInCal.set(Calendar.MINUTE, 0);
        checkInCal.set(Calendar.SECOND, 0);
        checkInCal.set(Calendar.MILLISECOND, 0);
        checkInDate = checkInCal.getTime();

        // Calculate difference in days using normalized dates
        Calendar checkOutCal = Calendar.getInstance();
        checkOutCal.setTime(checkOutDate);
        checkOutCal.set(Calendar.HOUR_OF_DAY, 0);
        checkOutCal.set(Calendar.MINUTE, 0);
        checkOutCal.set(Calendar.SECOND, 0);
        checkOutCal.set(Calendar.MILLISECOND, 0);
        checkOutDate = checkOutCal.getTime();

        long diffInMillies = checkOutDate.getTime() - checkInDate.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (diffInDays < 1) {
            return "La fecha de salida debe ser al menos un día posterior al de la fecha de entrada";
        }

        return null;
    }
}
