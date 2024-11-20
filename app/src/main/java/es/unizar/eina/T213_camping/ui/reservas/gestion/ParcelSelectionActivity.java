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

    private ParcelaViewModel parcelaViewModel;
    private long reservationId;
    private ImageButton prevButton, nextButton;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_parcel_selection;
    }

    @Override
    protected String getToolbarTitle() {
        return "Select Parcels";
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
    }

    /**
     * Configura los ViewModels necesarios.
     */
    private void setupViewModels() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
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

        // TODO: shouldn't happen, there must be at least one added Parcela in a Reserva
        Log.i("ADDED_PARCELS", addedParcels != null ? addedParcels.toString() : "null");
        if (addedParcels == null) {
            addedParcels = new ArrayList<>();
        }

        // KEY: set up the available parcels adapter AFTER retrieving "addedParcels" from the DB
        availableParcelsAdapter = new AvailableParcelsAdapter(this, addedParcels, this::updateParcelSelection);
        availableParcelsRecyclerView.setAdapter(availableParcelsAdapter);

        // Call the method to get parcelas not linked to the specific reservation ID
        parcelaViewModel.getParcelasNotLinkedToReservation(reservationId).observe(this, liveAvailableParcels -> {
            // Log.i("PARCELAS_NOT_LINKED_TO_RESERVATION", "are: " + availableParcelas.toString());
            availableParcels = liveAvailableParcels;
            availableParcelsAdapter.submitList(liveAvailableParcels);

            addedParcelsAdapter = new AddedParcelsAdapter(this, availableParcels, this::updateParcelSelection);
            addedParcelsRecyclerView.setAdapter(addedParcelsAdapter);
            addedParcelsAdapter.submitList(new ArrayList<>(addedParcels));
        });

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
        String clientName = getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
        String clientPhone = getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
        
        // Get the date strings from intent
        String entryDateStr = getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
        String departureDateStr = getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);

        try {
            // Parse the dates to ensure they're in the correct format
            Date entryDate = DATE_FORMAT.parse(entryDateStr);
            Date departureDate = DATE_FORMAT.parse(departureDateStr);

            // Format the dates back to strings in the consistent format
            ReservationUtils.confirmReservation(this, reservationId, clientName, clientPhone, 
                DATE_FORMAT.format(entryDate), 
                DATE_FORMAT.format(departureDate), 
                addedParcels);
                
        } catch (ParseException e) {
            Log.e("ParcelSelectionActivity", "Error parsing dates", e);
            // Show error dialog or handle the error appropriately
            showErrorDialog("Error al procesar las fechas de la reserva");
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
            .show();
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
    }

    public String getClientPhone() {
        return getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
    }

    public List<ParcelaOccupancy> getAddedParcels() {
        return addedParcels;
    }

    public String getClientName() {
        return getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
    }

    public String getCheckInDate() {
        return getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
    }

    public String getCheckOutDate() {
        return getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);
    }
}
