package es.unizar.eina.T213_camping.ui.reservas.creacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.textfield.TextInputEditText;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.utils.src.DateUtils;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;

public class CreateReservationActivity extends BaseActivity {

    private TextInputEditText clientNameInput, clientPhoneInput;
    private Button checkInDatePicker, checkOutDatePicker, nextButton;
    private String checkInDate, checkOutDate;
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
        return "Create Reservation";
    }

    private void setupListeners() {
        checkInDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, true, checkInDatePicker, this::validateDates));
        checkOutDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, false, checkOutDatePicker, this::validateDates));
        nextButton.setOnClickListener(v -> validateAndProceed());
    }

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

    private void validateDates() {
        if (checkInDate != null && checkOutDate != null) {
            if (DateUtils.isCheckOutBeforeCheckIn(checkInDate, checkOutDate)) {
                errorMessage.setText("La fecha de salida debe ser posterior a la de entrada");
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                errorMessage.setVisibility(View.GONE);
            }
        }
    }

    private void validateAndProceed() {
        if (clientNameInput.getText().toString().isEmpty() || clientPhoneInput.getText().toString().isEmpty()) {
            showErrorDialog("Por favor, complete todos los campos.");
        } else if (errorMessage.getVisibility() == View.VISIBLE) {
            showErrorDialog("La fecha de salida debe ser posterior a la de entrada.");
        } else {
            Intent intent = new Intent(this, NewParcelSelectionActivity.class);
            
            intent.putExtra(ReservationConstants.CLIENT_NAME, clientNameInput.getText().toString());
            intent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhoneInput.getText().toString());
            intent.putExtra(ReservationConstants.ENTRY_DATE, checkInDate);
            intent.putExtra(ReservationConstants.DEPARTURE_DATE, checkOutDate);
            newParcelSelectionLauncher.launch(intent);
        }
    }

    private void showErrorDialog(String message) {
        // Implement error dialog
    }
}
