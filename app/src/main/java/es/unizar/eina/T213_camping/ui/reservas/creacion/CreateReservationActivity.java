package es.unizar.eina.T213_camping.ui.reservas.creacion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;

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

        setupInputValidation();
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
        // Collect all validation errors
        ArrayList<String> errors = new ArrayList<>();
        
        // Validate client name
        if (clientNameInput.getText().toString().isEmpty()) {
            errors.add(getString(R.string.error_empty_client_name));
        }
        
        // Validate phone number
        String phone = clientPhoneInput.getText().toString();
        if (phone.isEmpty()) {
            errors.add(getString(R.string.error_empty_phone));
        } else if (phone.length() != ReservationConstants.PHONE_LENGTH) {
            errors.add(getString(R.string.error_invalid_phone_length, ReservationConstants.PHONE_LENGTH));
        } else if (!phone.matches("\\d*")) {
            errors.add(getString(R.string.error_phone_digits_only));
        }

        // Validate dates
        if (checkInDate == null || checkOutDate == null) {
            errors.add(getString(R.string.error_select_dates));
        } else {
            String dateError = DateUtils.validateDates(checkInDate, checkOutDate);
            if (dateError != null) {
                errors.add(dateError);
            }
        }

        // If there are errors, show them all in the dialog
        if (!errors.isEmpty()) {
            String errorMessage = String.join("\n• ", errors);
            DialogUtils.showErrorDialog(this, 
                getString(R.string.error_validation_failed) + "\n\n• " + errorMessage);
            return;
        }

        // If validation passes, proceed
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

    private void setupInputValidation() {
        // Client name validation
        clientNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but must be implemented
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed, but must be implemented
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > ReservationConstants.MAX_CLIENT_NAME_LENGTH) {
                    s.delete(ReservationConstants.MAX_CLIENT_NAME_LENGTH, s.length());
                    clientNameInput.setError(getString(R.string.error_name_too_long, 
                        ReservationConstants.MAX_CLIENT_NAME_LENGTH));
                }
            }
        });

        // Phone validation
        clientPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but must be implemented
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed, but must be implemented
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString();
                
                // Check for non-digits
                if (!phone.matches("\\d*")) {
                    clientPhoneInput.setError(getString(R.string.error_phone_digits_only));
                    return;
                }
                
                // Truncate if too long
                if (phone.length() > ReservationConstants.PHONE_LENGTH) {
                    s.delete(ReservationConstants.PHONE_LENGTH, s.length());
                    clientPhoneInput.setError(getString(R.string.error_phone_too_long, 
                        ReservationConstants.PHONE_LENGTH));
                    return;
                }
                
                // Check if length is exactly 9
                if (phone.length() < ReservationConstants.PHONE_LENGTH) {
                    clientPhoneInput.setError(getString(R.string.error_phone_too_short, 
                        ReservationConstants.PHONE_LENGTH));
                    return;
                }
                
                // Clear error if validation passes
                clientPhoneInput.setError(null);
            }
        });
    }
}
