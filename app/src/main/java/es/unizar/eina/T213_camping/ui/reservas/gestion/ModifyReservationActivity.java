package es.unizar.eina.T213_camping.ui.reservas.gestion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.utils.DateUtils;
import es.unizar.eina.T213_camping.utils.ReservationUtils;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel;
import es.unizar.eina.T213_camping.R;
import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;

public class ModifyReservationActivity extends BaseActivity {

    private EditText clientNameInput, clientPhoneInput;
    private TextView totalPrice, errorMessage;
    private Button saveChanges, notifyClient, deleteReservation;
    private Button checkInDatePicker, checkOutDatePicker;
    private String checkInDate, checkOutDate;

    // Variables to hold intent parameters
    private long reservationId;
    private String clientName;
    private String clientPhone;
    private String entryDate;
    private String departureDate;

    private ActivityResultLauncher<Intent> parcelSelectionLauncher;

    private ReservaViewModel reservaViewModel;

    private List<ParcelaOccupancy> selectedParcels;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_modify_reservation;
    }

    @Override
    protected String getToolbarTitle() {
        return "Modify Reservation";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();
        setupViewModels();
        loadReservationData(); // KEY: done before setting listeners (ensure that all data is loaded properly)
        setupListeners();
        setupParcelSelectionLauncher();

        // Set button visibility
        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    private void setupViews() {
        clientNameInput = findViewById(R.id.modify_reservation_client_name_input);
        clientPhoneInput = findViewById(R.id.modify_reservation_client_phone_input);
        totalPrice = findViewById(R.id.modify_reservation_total_price);
        errorMessage = findViewById(R.id.modify_reservation_error_message);
        saveChanges = findViewById(R.id.modify_reservation_save_button);
        notifyClient = findViewById(R.id.modify_reservation_notify_button);
        deleteReservation = findViewById(R.id.modify_reservation_delete_button);
        checkInDatePicker = findViewById(R.id.modify_reservation_check_in_date_picker);
        checkOutDatePicker = findViewById(R.id.modify_reservation_check_out_date_picker);
    }

    private void loadReservationData() {
        // Get data from intent
        reservationId = getIntent().getLongExtra("RESERVATION_ID", 0L);
        clientName = getIntent().getStringExtra("CLIENT_NAME");
        clientPhone = getIntent().getStringExtra("CLIENT_PHONE");
        entryDate = getIntent().getStringExtra("ENTRY_DATE"); // Get entry date
        departureDate = getIntent().getStringExtra("DEPARTURE_DATE"); // Get departure date
        selectedParcels = getIntent().getParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS);

        Log.i("SELECTED_PARCELS", selectedParcels != null ? selectedParcels.toString() : "null");

        // NOTE: shouldn't happen (all Reservas MUST have at list one ParcelaReservada => TODO, enforce checks)
        if (selectedParcels == null) {
            selectedParcels = new ArrayList<>();
        }

        // Set data to inputs
        clientNameInput.setText(clientName);
        clientPhoneInput.setText(clientPhone);
    }

    private void setupViewModels() {
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
    }

    private void setupListeners() {
        checkInDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, true, checkInDatePicker, this::validateDates));
        checkOutDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, false, checkOutDatePicker, this::validateDates));

        notifyClient.setOnClickListener(v -> ReservationUtils.notifyClient(this, this));
        deleteReservation.setOnClickListener(v -> ReservationUtils.deleteReservation(this, this));
        
        saveChanges.setOnClickListener(v -> confirmReservation());

        // Set up navigation buttons
        ImageButton prevButton = findViewById(R.id.modify_reservation_prev_button);
        ImageButton nextButton = findViewById(R.id.modify_reservation_next_button);
        prevButton.setOnClickListener(v -> navigateToPrevious());
        nextButton.setOnClickListener(v -> validateAndProceed());
    }

    // TODO: merge with navbar
    private void navigateToPrevious() {
        setResult(RESULT_CANCELED);
        finish(); // Go back to the previous screen
    }

    // TODO: handle error states and navigation
    private void validateDates() {
        if (checkInDate != null && checkOutDate != null) {
            // Use isCheckOutBeforeCheckIn to validate dates
            if (DateUtils.isCheckOutBeforeCheckIn(checkInDate, checkOutDate)) {
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                errorMessage.setVisibility(View.GONE);
            }
        }
    }

    private void setupParcelSelectionLauncher() {
        parcelSelectionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK, result.getData());
                    finish();
                }
            }
        );
    }

    private void validateAndProceed() {
        if (clientNameInput.getText().toString().isEmpty() || clientPhoneInput.getText().toString().isEmpty()) {
            showErrorDialog("Por favor, complete todos los campos.");
        } else if (errorMessage.getVisibility() == View.VISIBLE) {
            showErrorDialog("La fecha de salida debe ser posterior a la de entrada.");
        } else {
            Intent intent = new Intent(this, ParcelSelectionActivity.class);
            intent.putExtra(ReservationConstants.RESERVATION_ID, reservationId);
            intent.putExtra(ReservationConstants.CLIENT_NAME, clientNameInput.getText().toString());
            intent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhoneInput.getText().toString());
            // TODO: transform dates strings into actual date types
            intent.putExtra(ReservationConstants.ENTRY_DATE, checkInDatePicker.getText().toString());
            intent.putExtra(ReservationConstants.DEPARTURE_DATE, checkOutDatePicker.getText().toString());
            // intent.putExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(selectedParcels));
            intent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(selectedParcels));
            parcelSelectionLauncher.launch(intent);
        }
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void confirmReservation() {
        ReservationUtils.confirmReservation(this, reservationId, clientNameInput.getText().toString(),
            clientPhoneInput.getText().toString(), checkInDate, checkOutDate, selectedParcels);
    }
}
