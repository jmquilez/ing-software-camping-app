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
import es.unizar.eina.T213_camping.utils.PriceUtils;
import es.unizar.eina.T213_camping.utils.ReservationUtils;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel;
import es.unizar.eina.T213_camping.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.utils.DialogUtils;

/**
 * Activity que permite modificar una reserva existente.
 * Proporciona funcionalidades para:
 * - Modificar datos del cliente
 * - Cambiar fechas de entrada/salida
 * - Gestionar parcelas reservadas
 * - Notificar al cliente
 * - Eliminar la reserva
 */
public class ModifyReservationActivity extends BaseActivity {

    private EditText clientNameInput, clientPhoneInput;
    private TextView totalPrice, errorMessage;
    private Button saveChanges, notifyClient, deleteReservation;
    private Button checkInDatePicker, checkOutDatePicker;
    private Date checkInDate, checkOutDate;

    // Variables para almacenar los datos recibidos
    private long reservationId;
    private String clientName;
    private String clientPhone;
    private String entryDate;
    private String departureDate;

    private ActivityResultLauncher<Intent> parcelSelectionLauncher;
    private ReservaViewModel reservaViewModel;
    private List<ParcelaOccupancy> selectedParcels;

    private TextView priceDisplay;

    private ParcelaReservadaViewModel parcelaReservadaViewModel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_modify_reservation;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.modify_reservation_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();
        setupViewModels();
        // KEY: done before setting listeners (ensure that all data is loaded properly)
        loadReservationData();
        setupListeners();
        setupParcelSelectionLauncher();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    /**
     * Inicializa las referencias a las vistas de la actividad.
     */
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
        priceDisplay = findViewById(R.id.modify_reservation_total_price);
    }

    /**
     * Carga los datos de la reserva recibidos en el Intent.
     * Incluye ID, datos del cliente, fechas y parcelas seleccionadas.
     */
    private void loadReservationData() {
        reservationId = getIntent().getLongExtra(ReservationConstants.RESERVATION_ID, 0L);
        clientName = getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
        clientPhone = getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
        
        try {
            String entryDateStr = getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
            String departureDateStr = getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);
            
            Log.d("ModifyReservationActivity", "Entry date string: " + entryDateStr);
            Log.d("ModifyReservationActivity", "Departure date string: " + departureDateStr);
            
            if (entryDateStr != null && !entryDateStr.isEmpty() && 
                departureDateStr != null && !departureDateStr.isEmpty()) {
                
                checkInDate = DateUtils.DATE_FORMAT.parse(entryDateStr);
                checkOutDate = DateUtils.DATE_FORMAT.parse(departureDateStr);
                
                if (checkInDate == null || checkOutDate == null) {
                    throw new ParseException("Error al parsear las fechas", 0);
                }
                
                checkInDatePicker.setText(DateUtils.DATE_FORMAT.format(checkInDate));
                checkOutDatePicker.setText(DateUtils.DATE_FORMAT.format(checkOutDate));
                
                Log.d("ModifyReservationActivity", "Fechas parseadas correctamente - Entry: " + 
                      checkInDate + ", Departure: " + checkOutDate);
            } else {
                throw new ParseException("Fechas vacías o nulas", 0);
            }
        } catch (ParseException e) {
            Log.e("ModifyReservationActivity", "Error parsing dates: " + e.getMessage(), e);
            DialogUtils.showErrorDialog(this, 
                getString(R.string.error_loading_dates, e.getMessage()));
            checkInDate = new Date();
            checkOutDate = new Date(checkInDate.getTime() + 86400000);
        }

        selectedParcels = getIntent().getParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS);
        if (selectedParcels == null) {
            selectedParcels = new ArrayList<>();
        }

        clientNameInput.setText(clientName);
        clientPhoneInput.setText(clientPhone);

        // Get initial price from intent
        double initialPrice = getIntent().getDoubleExtra(ReservationConstants.RESERVATION_PRICE, 0.0);
        
        if (initialPrice >= 0) {
            priceDisplay.setText(String.format(Locale.getDefault(), 
                getString(R.string.price_total), initialPrice));
        } else {
            // Update initial price display
            PriceUtils.updatePriceDisplay(priceDisplay, checkInDate, checkOutDate, selectedParcels);
        }
    }

    /**
     * Configura el ViewModel para la gestión de datos.
     */
    private void setupViewModels() {
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        parcelaReservadaViewModel = new ViewModelProvider(this).get(ParcelaReservadaViewModel.class);
    }

    /**
     * Configura los listeners para todos los botones y elementos interactivos.
     * Incluye botones de fecha, navegación y acciones principales.
     */
    private void setupListeners() {
        checkInDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, true, checkInDatePicker, () -> {
            try {
                checkInDate = DateUtils.DATE_FORMAT.parse(checkInDatePicker.getText().toString());
                validateDates();
            } catch (ParseException e) {
                Log.e("ModifyReservationActivity", "Error parsing check-in date", e);
                DialogUtils.showErrorDialog(this, "Error al procesar la fecha de entrada");
            }
        }));

        checkOutDatePicker.setOnClickListener(v -> DateUtils.showDatePickerDialog(this, false, checkOutDatePicker, () -> {
            try {
                checkOutDate = DateUtils.DATE_FORMAT.parse(checkOutDatePicker.getText().toString());
                validateDates();
            } catch (ParseException e) {
                Log.e("ModifyReservationActivity", "Error parsing check-out date", e);
                DialogUtils.showErrorDialog(this, "Error al procesar la fecha de salida");
            }
        }));

        notifyClient.setOnClickListener(v -> ReservationUtils.notifyClient(this, this));
        deleteReservation.setOnClickListener(v -> ReservationUtils.deleteReservation(this, this));
        
        saveChanges.setOnClickListener(v -> confirmReservation());

        // Set up navigation buttons
        ImageButton prevButton = findViewById(R.id.modify_reservation_prev_button);
        ImageButton nextButton = findViewById(R.id.modify_reservation_next_button);
        prevButton.setOnClickListener(v -> navigateToPrevious());
        nextButton.setOnClickListener(v -> validateAndProceed());
    }

    /**
     * Configura el launcher para la actividad de selección de parcelas.
     */
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

    /**
     * Navega a la pantalla anterior.
     */
    private void navigateToPrevious() {
        setResult(RESULT_CANCELED);
        finish(); // Go back to the previous screen
    }

    /**
     * Valida que las fechas de entrada y salida sean correctas.
     * @return true si las fechas son válidas, false en caso contrario
     */
    private void validateDates() {
        if (checkInDate != null && checkOutDate != null) {
            String error = DateUtils.validateDates(checkInDate, checkOutDate);
            if (error != null) {
                errorMessage.setText(error);
                errorMessage.setVisibility(View.VISIBLE);
            } else {
                errorMessage.setVisibility(View.GONE);
                PriceUtils.updatePriceDisplay(priceDisplay, checkInDate, checkOutDate, selectedParcels);

                // Get available parcels for new dates, excluding current reservation
                parcelaReservadaViewModel.getParcelasDisponiblesEnIntervaloExcludingReservation(
                    checkInDate, checkOutDate, reservationId)
                    .observe(this, availableParcels -> {
                        // Convert to set of names for easy checking
                        Set<String> availableParcelNames = availableParcels.stream()
                            .map(Parcela::getNombre)
                            .collect(Collectors.toSet());

                        // Get unavailable parcels
                        List<String> unavailableParcels = selectedParcels.stream()
                            .map(po -> po.getParcela().getNombre())
                            .filter(name -> !availableParcelNames.contains(name))
                            .collect(Collectors.toList());

                        if (!unavailableParcels.isEmpty()) {
                            String parcelList = unavailableParcels.stream()
                                .limit(3)  // Take only first 3
                                .collect(Collectors.joining(", "));
                            
                            String message = "Las siguientes parcelas no están disponibles: " + parcelList;
                            if (unavailableParcels.size() > 3) {
                                message += " y " + (unavailableParcels.size() - 3) + " más";
                            }
                            
                            errorMessage.setText(message);
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    });
            }
        }
    }

    /**
     * Valida los datos y procede a la selección de parcelas.
     * Muestra mensajes de error si la validación falla.
     */
    private void validateAndProceed() {
        if (clientNameInput.getText().toString().isEmpty() || clientPhoneInput.getText().toString().isEmpty()) {
            DialogUtils.showErrorDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        if (errorMessage.getVisibility() == View.VISIBLE) {
            DialogUtils.showErrorDialog(this, errorMessage.getText().toString());
            return;
        }

        Intent intent = new Intent(this, ParcelSelectionActivity.class);
        intent.putExtra(ReservationConstants.RESERVATION_ID, reservationId);
        intent.putExtra(ReservationConstants.CLIENT_NAME, clientNameInput.getText().toString());
        intent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhoneInput.getText().toString());
        intent.putExtra(ReservationConstants.ENTRY_DATE, DateUtils.DATE_FORMAT.format(checkInDate));
        intent.putExtra(ReservationConstants.DEPARTURE_DATE, DateUtils.DATE_FORMAT.format(checkOutDate));
        intent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(selectedParcels));
        
        // Calculate and pass the current price
        double currentPrice = PriceUtils.calculateReservationPrice(checkInDate, checkOutDate, selectedParcels);
        intent.putExtra(ReservationConstants.RESERVATION_PRICE, currentPrice);
        
        parcelSelectionLauncher.launch(intent);
    }

    /**
     * Confirma los cambios en la reserva utilizando ReservationUtils.
     */
    private void confirmReservation() {
        try {
            if (checkInDate == null || checkOutDate == null) {
                DialogUtils.showErrorDialog(this, "Las fechas no son válidas");
                return;
            }
            
            if (errorMessage.getVisibility() == View.VISIBLE) {
                DialogUtils.showErrorDialog(this, errorMessage.getText().toString());
                return;
            }
            
            ReservationUtils.confirmReservation(this, reservationId, 
                clientNameInput.getText().toString(),
                clientPhoneInput.getText().toString(), 
                checkInDate,    // Usamos directamente los objetos Date
                checkOutDate,   // Usamos directamente los objetos Date
                selectedParcels, true);
        } catch (Exception e) {
            Log.e("ModifyReservationActivity", "Error al confirmar la reserva: " + e.getMessage(), e);
            DialogUtils.showErrorDialog(this, 
                getString(R.string.error_update_reservation, e.getMessage()));
        }
    }

    /**
     * Obtiene el teléfono del cliente introducido en el formulario.
     * @return String con el número de teléfono del cliente
     */
    public String getClientPhone() {
        return clientPhoneInput.getText().toString();
    }

    /**
     * Obtiene el nombre del cliente introducido en el formulario.
     * @return String con el nombre del cliente
     */
    public String getClientName() {
        return clientNameInput.getText().toString();
    }

    /**
     * Obtiene la fecha de entrada de la reserva.
     * @return Date con la fecha de entrada
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * Obtiene la fecha de salida de la reserva.
     * @return Date con la fecha de salida
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Obtiene la lista de parcelas seleccionadas con sus ocupaciones.
     * @return List<ParcelaOccupancy> con las parcelas seleccionadas
     */
    public List<ParcelaOccupancy> getSelectedParcels() {
        return selectedParcels;
    }
}
