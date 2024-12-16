package es.unizar.eina.T213_camping.ui.reservas.creacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.textfield.TextInputEditText;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.utils.DateUtils;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.utils.DialogUtils;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Activity que gestiona la primera fase de la creación de una reserva.
 * Permite al usuario introducir los datos básicos de la reserva:
 * - Nombre del cliente
 * - Teléfono del cliente
 * - Fecha de entrada
 * - Fecha de salida
 */
public class CreateReservationActivity extends BaseActivity {

    private TextInputEditText clientNameInput, clientPhoneInput;
    private Button checkInDatePicker, checkOutDatePicker, nextButton;
    private Date checkInDate, checkOutDate;
    private TextView errorMessage;

    private ActivityResultLauncher<Intent> newParcelSelectionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientNameInput = findViewById(R.id.create_reservation_client_name_input);
        clientPhoneInput = findViewById(R.id.create_reservation_client_phone_input);
        checkInDatePicker = findViewById(R.id.create_reservation_check_in_date_picker);
        checkOutDatePicker = findViewById(R.id.create_reservation_check_out_date_picker);
        nextButton = findViewById(R.id.create_reservation_next_button);
        errorMessage = findViewById(R.id.create_reservation_error_message);

        setupListeners();
        setupNewParcelSelectionLauncher();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_reservation;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.create_reservation_title);
    }

    /**
     * Configura los listeners para los botones de fecha y el botón siguiente.
     * Los date pickers utilizan DateUtils para mostrar el selector de fecha.
     */
    private void setupListeners() {
        checkInDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, true, checkInDatePicker, () -> {
            checkInDate = parseDate(checkInDatePicker.getText().toString());
            validateDates();
        }));
        
        checkOutDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, false, checkOutDatePicker, () -> {
            checkOutDate = parseDate(checkOutDatePicker.getText().toString());
            validateDates();
        }));

        nextButton.setOnClickListener(v -> {
            if (checkInDate == null || checkOutDate == null) {
                DialogUtils.showErrorDialog(this, getString(R.string.error_select_dates));
                return;
            }
            
            String error = DateUtils.validateDates(checkInDate, checkOutDate);
            if (error != null) {
                DialogUtils.showErrorDialog(this, error);
                return;
            }
            
            validateAndProceed();
        });
    }

    /**
     * Configura el launcher para la actividad de selección de parcelas.
     * Si la selección es exitosa, devuelve el resultado a la actividad anterior.
     */
    private void setupNewParcelSelectionLauncher() {
        newParcelSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK, result.getData());
                    finish();
                }
            }
        );
    }

    /**
     * Valida que la fecha de salida sea posterior a la de entrada.
     * Muestra un mensaje de error si las fechas no son válidas.
     */
    private void validateDates() {
        if (checkInDate == null || checkOutDate == null) {
            errorMessage.setText("Por favor, seleccione ambas fechas");
            errorMessage.setVisibility(View.VISIBLE);
            return;
        }

        String error = DateUtils.validateDates(checkInDate, checkOutDate);
        if (error != null) {
            errorMessage.setText(error);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.GONE);
        }
    }

    /**
     * Valida los datos y procede a la selección de parcelas.
     */
    private void validateAndProceed() {
        if (clientNameInput.getText().toString().isEmpty() || clientPhoneInput.getText().toString().isEmpty()) {
            DialogUtils.showErrorDialog(this, getString(R.string.error_complete_fields));
            return;
        }

        if (checkInDate == null || checkOutDate == null) {
            DialogUtils.showErrorDialog(this, getString(R.string.error_select_dates));
            return;
        }

        String error = DateUtils.validateDates(checkInDate, checkOutDate);
        if (error != null) {
            DialogUtils.showErrorDialog(this, error);
            return;
        }

        Intent intent = new Intent(this, NewParcelSelectionActivity.class);
        intent.putExtra(ReservationConstants.CLIENT_NAME, clientNameInput.getText().toString());
        intent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhoneInput.getText().toString());
        intent.putExtra(ReservationConstants.ENTRY_DATE, DateUtils.DATE_FORMAT.format(checkInDate));
        intent.putExtra(ReservationConstants.DEPARTURE_DATE, DateUtils.DATE_FORMAT.format(checkOutDate));
        newParcelSelectionLauncher.launch(intent);
    }

    /**
     * Convierte una fecha en formato String a Date.
     * @param dateString Fecha en formato String
     * @return Date correspondiente
     */
    private Date parseDate(String dateString) {
        try {
            return DateUtils.DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            Log.e("CreateReservationActivity", "Error parsing date: " + e.getMessage());
            errorMessage.setText(getString(R.string.error_invalid_date_format));
            errorMessage.setVisibility(View.VISIBLE);
            return null;
        }
    }
}
