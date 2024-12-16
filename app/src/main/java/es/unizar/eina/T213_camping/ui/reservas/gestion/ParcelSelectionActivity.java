package es.unizar.eina.T213_camping.ui.reservas.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AlertDialog;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AvailableParcelsAdapter;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AddedParcelsAdapter;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel; // Change to ParcelaViewModel
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.utils.ReservationUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.utils.DateUtils;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel;
import es.unizar.eina.T213_camping.utils.PriceUtils;
import android.widget.TextView;

/**
 * Activity que permite gestionar las parcelas asociadas a una reserva existente.
 * Muestra dos listas:
 * - Parcelas disponibles para añadir
 * - Parcelas ya asociadas a la reserva
 * 
 * Permite añadir nuevas parcelas, modificar la ocupación de las existentes
 * y eliminar parcelas de la reserva.
 */
public class ParcelSelectionActivity extends BaseActivity {

    private RecyclerView availableParcelsRecyclerView, addedParcelsRecyclerView;
    private AvailableParcelsAdapter availableParcelsAdapter;
    private AddedParcelsAdapter addedParcelsAdapter;
    private List<ParcelaOccupancy> addedParcels;
    private List<Parcela> availableParcels;

    private ParcelaReservadaViewModel parcelaReservadaViewModel;
    private long reservationId;
    private ImageButton prevButton, nextButton;
    private TextView priceDisplay;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_parcel_selection;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.parcel_selection_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();
        setupViewModels();
        setupRecyclerViewsAndData();
        setupListeners();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    /**
     * Inicializa las referencias a las vistas de la actividad.
     */
    private void setupViews() {
        availableParcelsRecyclerView = findViewById(R.id.parcel_selection_available_parcels_recycler_view);
        addedParcelsRecyclerView = findViewById(R.id.parcel_selection_added_parcels_recycler_view);
        prevButton = findViewById(R.id.parcel_selection_prev_button);
        nextButton = findViewById(R.id.parcel_selection_next_button);
        priceDisplay = findViewById(R.id.parcel_selection_price_display);
    }

    /**
     * Configura los ViewModels necesarios.
     */
    private void setupViewModels() {
        parcelaReservadaViewModel = new ViewModelProvider(this).get(ParcelaReservadaViewModel.class);
    }

    /**
     * Configura los RecyclerViews y carga los datos iniciales.
     * Incluye la configuración de adaptadores y la carga de parcelas
     * disponibles y ya añadidas.
     */
    private void setupRecyclerViewsAndData() {
        availableParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservationId = getIntent().getLongExtra(ReservationConstants.RESERVATION_ID, 0L);
        addedParcels = getIntent().getParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS);

        // NOTE: shouldn't happen, there must be at least one added Parcela in a Reserva
        Log.i("ADDED_PARCELS", addedParcels != null ? addedParcels.toString() : "null");
        if (addedParcels == null) {
            addedParcels = new ArrayList<>();
        }

        // KEY: set up the available parcels adapter AFTER retrieving "addedParcels" from the DB
        availableParcelsAdapter = new AvailableParcelsAdapter(this, addedParcels, this::updateParcelSelection);
        availableParcelsRecyclerView.setAdapter(availableParcelsAdapter);

        // Get check-in and check-out dates
        Date checkInDate = getCheckInDate();
        Date checkOutDate = getCheckOutDate();

        if (checkInDate != null && checkOutDate != null) {
            parcelaReservadaViewModel.getParcelasDisponiblesEnIntervalo(checkInDate, checkOutDate)
                .observe(this, liveAvailableParcels -> {
                    availableParcels = liveAvailableParcels;
                    availableParcelsAdapter.submitList(availableParcels);

                    addedParcelsAdapter = new AddedParcelsAdapter(this, availableParcels, this::updateParcelSelection);
                    addedParcelsRecyclerView.setAdapter(addedParcelsAdapter);
                    addedParcelsAdapter.submitList(new ArrayList<>(addedParcels));

                    // Show initial price from intent or calculate if not provided
                    double initialPrice = getIntent().getDoubleExtra(ReservationConstants.RESERVATION_PRICE, -1);
                    if (initialPrice >= 0) {
                        priceDisplay.setText(String.format(Locale.getDefault(), "Precio total: %.2f€", initialPrice));
                    } else {
                        PriceUtils.updatePriceDisplay(priceDisplay, checkInDate, checkOutDate, addedParcels);
                    }
                });
        } else {
            DialogUtils.showErrorDialog(this, "Error al procesar las fechas");
        }

        addedParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * Configura los listeners para los botones de navegación y acciones.
     */
    private void setupListeners() {
        findViewById(R.id.parcel_selection_save_button).setOnClickListener(v -> confirmReservation());
        findViewById(R.id.parcel_selection_notify_button).setOnClickListener(v -> ReservationUtils.notifyClient(this, this));
        findViewById(R.id.parcel_selection_delete_button).setOnClickListener(v -> ReservationUtils.deleteReservation(this, this));

        prevButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        nextButton.setOnClickListener(v -> {
            // This is the last screen, so we'll just confirm the reservation
            confirmReservation();
        });
    }

    /**
     * Confirma los cambios en la reserva utilizando ReservationUtils.
     */
    private void confirmReservation() {
        if (addedParcels == null || addedParcels.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Error: Debe seleccionar al menos una parcela");
            return;
        }

        String clientName = getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
        String clientPhone = getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
        
        try {
            Date entryDate = DateUtils.DATE_FORMAT.parse(getIntent().getStringExtra(ReservationConstants.ENTRY_DATE));
            Date departureDate = DateUtils.DATE_FORMAT.parse(getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE));

            ReservationUtils.confirmReservation(this, reservationId, clientName, clientPhone, 
                entryDate, departureDate, addedParcels, true);  // true for update
                
        } catch (ParseException e) {
            Log.e("ParcelSelectionActivity", "Error parsing dates", e);
            DialogUtils.showErrorDialog(this, "Error al procesar las fechas de la reserva");
        }
    }

    /**
     * Actualiza las listas de parcelas cuando cambia la selección.
     * No multiple inheritance in Java :(
     * @param updatedAddedParcelsList Lista actualizada de parcelas añadidas
     * @param updatedAvailableParcelsList Lista actualizada de parcelas disponibles
     * @param whoCalled Identificador del adaptador que realizó el cambio
     */
    public void updateParcelSelection(List<ParcelaOccupancy> updatedAddedParcelsList,
                                   List<Parcela> updatedAvailableParcelsList, String whoCalled) {
        addedParcels = updatedAddedParcelsList;
        availableParcels = updatedAvailableParcelsList;

        // DONE: avoid calling the object that has called us first
        switch (whoCalled) {
            case ReservationConstants.ADDED_PARCELS_ADAPTER_CALLED:
                availableParcelsAdapter.updateAddedParcels(addedParcels);
                availableParcelsAdapter.submitList(new ArrayList<>(availableParcels), () -> {
                    availableParcelsRecyclerView.scrollToPosition(0);
                });
                break;
            case ReservationConstants.AVAILABLE_PARCELS_ADAPTER_CALLED:
                addedParcelsAdapter.updateAvailableParcels(availableParcels);
                addedParcelsAdapter.submitList(new ArrayList<>(addedParcels), () -> {
                    addedParcelsRecyclerView.scrollToPosition(0);
                });
                break;
            default:
                break;
        }

        // availableParcelsAdapter.submitList(availableParcelsAdapter.getCurrentList());

        // Update price after parcels change
        PriceUtils.updatePriceDisplay(priceDisplay, getCheckInDate(), getCheckOutDate(), addedParcels);
    }

    /**
     * Obtiene el teléfono del cliente almacenado en el intent.
     * @return String con el número de teléfono del cliente
     */
    public String getClientPhone() {
        return getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
    }

    /**
     * Obtiene la lista de parcelas añadidas a la reserva.
     * @return List<ParcelaOccupancy> con las parcelas añadidas y sus ocupaciones
     */
    public List<ParcelaOccupancy> getAddedParcels() {
        return addedParcels;
    }

    /**
     * Obtiene el nombre del cliente almacenado en el intent.
     * @return String con el nombre del cliente
     */
    public String getClientName() {
        return getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
    }

    /**
     * Obtiene la fecha de entrada almacenada en el intent.
     * @return Date con la fecha de entrada, o null si hay error al procesar la fecha
     */
    public Date getCheckInDate() {
        try {
            String dateStr = getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
            return DateUtils.DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            Log.e("ParcelSelectionActivity", "Error parsing check-in date", e);
            return null;
        }
    }

    /**
     * Obtiene la fecha de salida almacenada en el intent.
     * @return Date con la fecha de salida, o null si hay error al procesar la fecha
     */
    public Date getCheckOutDate() {
        try{
            String dateStr = getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);
            return DateUtils.DATE_FORMAT.parse(dateStr);
        } catch (ParseException e){
            Log.e("ParcelSelectionActivity", "Error parsing check-in date", e);
            return null;
        }
    }
}
