package es.unizar.eina.T213_camping.ui.reservas.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.db.models.Parcela;
import es.unizar.eina.T213_camping.db.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AvailableParcelsAdapter;
import es.unizar.eina.T213_camping.ui.reservas.adapters.AddedParcelsAdapter;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel; // Change to ParcelaViewModel
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.utils.src.ReservationUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;

public class ParcelSelectionActivity extends BaseActivity {

    private RecyclerView availableParcelsRecyclerView, addedParcelsRecyclerView;
    private AvailableParcelsAdapter availableParcelsAdapter;
    private AddedParcelsAdapter addedParcelsAdapter;
    private List<ParcelaOccupancy> addedParcels; // Change to List<Parcela>

    private ParcelaViewModel parcelaViewModel; // Change to ParcelaViewModel
    private ReservaViewModel reservaViewModel;

    private long reservationId;

    private ImageButton prevButton, nextButton;

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
        setupRecyclerViews();
        loadReservationData();
        setupListeners();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    private void setupViews() {
        availableParcelsRecyclerView = findViewById(R.id.parcel_selection_available_parcels_recycler_view);
        addedParcelsRecyclerView = findViewById(R.id.parcel_selection_added_parcels_recycler_view);
        prevButton = findViewById(R.id.parcel_selection_prev_button);
        nextButton = findViewById(R.id.parcel_selection_next_button);
    }

    private void setupViewModels() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class); // Change to ParcelaViewModel
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
    }

    private void setupRecyclerViews() {
        availableParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableParcelsAdapter = new AvailableParcelsAdapter(this, addedParcels, this::updateAddedParcels);
        availableParcelsRecyclerView.setAdapter(availableParcelsAdapter);

        addedParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addedParcelsAdapter = new AddedParcelsAdapter(this, this::updateAddedParcels);
        addedParcelsRecyclerView.setAdapter(addedParcelsAdapter);
    }

    private void loadReservationData() {
        reservationId = getIntent().getLongExtra(ReservationConstants.RESERVATION_ID, 0L);
        addedParcels = (ArrayList<ParcelaOccupancy>) getIntent().getSerializableExtra(ReservationConstants.SELECTED_PARCELS); // Change to List<Parcela>
        if (addedParcels == null) {
            addedParcels = new ArrayList<>();
        }
        addedParcelsAdapter.submitList(new ArrayList<>(addedParcels));

        // Call the method to get parcelas not linked to the specific reservation ID
        parcelaViewModel.getParcelasNotLinkedToReservation(reservationId).observe(this, availableParcelas -> {
            availableParcelsAdapter.submitList(availableParcelas);
        });
    }

    private void setupListeners() {
        findViewById(R.id.parcel_selection_save_button).setOnClickListener(v -> confirmReservation());
        findViewById(R.id.parcel_selection_notify_button).setOnClickListener(v -> ReservationUtils.notifyClient(this, this));
        findViewById(R.id.parcel_selection_delete_button).setOnClickListener(v -> ReservationUtils.deleteReservation(this, this));

        prevButton.setOnClickListener(v -> onBackPressed());
        nextButton.setOnClickListener(v -> {
            // This is the last screen, so we'll just confirm the reservation
            confirmReservation();
        });
    }

    private void confirmReservation() {
        String clientName = getIntent().getStringExtra(ReservationConstants.CLIENT_NAME);
        String clientPhone = getIntent().getStringExtra(ReservationConstants.CLIENT_PHONE);
        String entryDate = getIntent().getStringExtra(ReservationConstants.ENTRY_DATE);
        String departureDate = getIntent().getStringExtra(ReservationConstants.DEPARTURE_DATE);

        ReservationUtils.confirmReservation(this, reservationId, clientName, clientPhone, entryDate, departureDate, addedParcels);
    }

    public void updateAddedParcels(List<ParcelaOccupancy> updatedList) { // Change to List<Parcela>
        addedParcels.clear();
        addedParcels.addAll(updatedList);
        addedParcelsAdapter.submitList(new ArrayList<>(addedParcels));
        availableParcelsAdapter.submitList(availableParcelsAdapter.getCurrentList());
    }
}
