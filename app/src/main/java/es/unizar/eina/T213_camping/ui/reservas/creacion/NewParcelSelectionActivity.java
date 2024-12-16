package es.unizar.eina.T213_camping.ui.reservas.creacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AvailableParcelsAdapter;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AddedParcelsAdapter;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.utils.ModelUtils.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel;
import es.unizar.eina.T213_camping.utils.DateUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;
import android.util.Log;
import android.widget.TextView;
import es.unizar.eina.T213_camping.utils.PriceUtils;
import es.unizar.eina.T213_camping.utils.ReservationUtils;

/**
 * Activity que permite seleccionar las parcelas para una nueva reserva.
 * Muestra dos listas:
 * - Parcelas disponibles para seleccionar
 * - Parcelas ya añadidas a la reserva
 * 
 * Permite al usuario añadir parcelas a la reserva, especificar su ocupación,
 * y eliminar parcelas previamente añadidas.
 */
public class NewParcelSelectionActivity extends BaseActivity {

    private RecyclerView availableParcelsRecyclerView, addedParcelsRecyclerView;
    private AvailableParcelsAdapter availableParcelsAdapter;
    private AddedParcelsAdapter addedParcelsAdapter;
    private List<ParcelaOccupancy> addedParcels;
    private List<Parcela> availableParcels;

    private ParcelaViewModel parcelaViewModel;
    private ParcelaReservadaViewModel parcelaReservadaViewModel;

    private TextView priceDisplay;

    private Date entryDate;
    private Date departureDate;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_new_parcel_selection;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.select_parcels_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();
        setupViewModels();
        setupRecyclerViews();
        setupListeners();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    /**
     * Inicializa las referencias a las vistas de la actividad.
     */
    private void setupViews() {
        availableParcelsRecyclerView = findViewById(R.id.new_reservation_available_parcels_recycler_view);
        addedParcelsRecyclerView = findViewById(R.id.new_reservation_added_parcels_recycler_view);
        priceDisplay = findViewById(R.id.new_reservation_price_display);
    }

    /**
     * Configura los ViewModels y las listas iniciales.
     */
    private void setupViewModels() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelaReservadaViewModel = new ViewModelProvider(this).get(ParcelaReservadaViewModel.class);
        addedParcels = new ArrayList<>();
    }

    /**
     * Configura los RecyclerViews y sus adaptadores.
     */
    private void setupRecyclerViews() {
        availableParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addedParcels = new ArrayList<>();
        
        availableParcelsAdapter = new AvailableParcelsAdapter(this, addedParcels, this::updateParcelSelection);
        availableParcelsRecyclerView.setAdapter(availableParcelsAdapter);

        entryDate = getCheckInDate();
        departureDate = getCheckOutDate();

        if (entryDate == null || departureDate == null) {
            DialogUtils.showErrorDialog(this, getString(R.string.error_invalid_dates));
            return;
        }

        // Get available parcels for the date range
        parcelaReservadaViewModel.getParcelasDisponiblesEnIntervalo(entryDate, departureDate)
            .observe(this, parcelas -> {
                availableParcels = parcelas;
                availableParcelsAdapter.submitList(availableParcels);

                addedParcelsAdapter = new AddedParcelsAdapter(this, availableParcels, this::updateParcelSelection);
                addedParcelsRecyclerView.setAdapter(addedParcelsAdapter);
                addedParcelsAdapter.submitList(addedParcels);
            });

        addedParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * Configura los listeners para los botones de navegación y confirmación.
     */
    private void setupListeners() {
        findViewById(R.id.new_reservation_previous_button).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        findViewById(R.id.new_reservation_confirm_button).setOnClickListener(v -> confirmReservation());
    }

    /**
     * Muestra un diálogo de confirmación y procesa la creación de la reserva.
     */
    private void confirmReservation() {
        if (addedParcels == null || addedParcels.isEmpty()) {
            DialogUtils.showErrorDialog(this, getString(R.string.error_select_parcel));
            return;
        }

        String clientName = getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
        String clientPhone = getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
        
        ReservationUtils.confirmReservation(this, 0L, clientName, clientPhone, 
            entryDate, departureDate, addedParcels, false);
    }

    /**
     * Actualiza las listas de parcelas cuando cambia la selección.
     * No multiple inheritance in Java :(
     * @param updatedAddedParcelsList Lista actualizada de parcelas añadidas
     * @param updatedAvailableParcelsList Lista actualizada de parcelas disponibles
     * @param whoCalled Identificador del adaptador que realizó el cambio
     */
    public void updateParcelSelection(List<ParcelaOccupancy> updatedAddedParcelsList,
                                      List<Parcela> updatedAvailableParcelsList, String whoCalled) { // Change to List<Parcela>

        addedParcels = updatedAddedParcelsList;
        availableParcels = updatedAvailableParcelsList;

        // DONE: avoid calling the object that has called us first
        switch (whoCalled) {
            case ReservationConstants.ADDED_PARCELS_ADAPTER_CALLED:
                availableParcelsAdapter.updateAddedParcels(addedParcels);
                availableParcelsAdapter.submitList(availableParcels, () -> {
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
        PriceUtils.updatePriceDisplay(priceDisplay, entryDate, departureDate, addedParcels);
    }

    public Date getCheckInDate() {
        try {
            String dateStr = getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
            return DateUtils.DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            Log.e("NewParcelSelection", "Error parsing check-in date", e);
            return null;
        }
    }

    public Date getCheckOutDate() {
        try {
            String dateStr = getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);
            return DateUtils.DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            Log.e("NewParcelSelection", "Error parsing check-out date", e);
            return null;
        }
    }
}
