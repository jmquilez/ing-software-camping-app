package es.unizar.eina.T213_camping.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Button;

import java.util.Calendar;

public class DateUtils {

    public static void showDatePickerDialog(Context context, boolean isCheckIn, Button dateInput, Runnable onDateSet) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            dateInput.setText(date);
            onDateSet.run(); // Call the provided runnable to handle date validation
        }, year, month, day);
        datePickerDialog.show();
    }

    public static boolean isCheckOutBeforeCheckIn(String checkInDate, String checkOutDate) {
        String[] checkInParts = checkInDate.split("/");
        String[] checkOutParts = checkOutDate.split("/");

        Calendar checkInCal = Calendar.getInstance();
        checkInCal.set(Integer.parseInt(checkInParts[2]), Integer.parseInt(checkInParts[1]) - 1, Integer.parseInt(checkInParts[0]));

        Calendar checkOutCal = Calendar.getInstance();
        checkOutCal.set(Integer.parseInt(checkOutParts[2]), Integer.parseInt(checkOutParts[1]) - 1, Integer.parseInt(checkOutParts[0]));

        return checkOutCal.before(checkInCal);
    }
}
