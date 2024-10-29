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
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;

public class NewParcelSelectionActivity extends BaseActivity {

    private RecyclerView availableParcelsRecyclerView, addedParcelsRecyclerView;
    private AvailableParcelsAdapter availableParcelsAdapter;
    private AddedParcelsAdapter addedParcelsAdapter;
    private List<ParcelaOccupancy> addedParcels;

    private ParcelaViewModel parcelaViewModel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_new_parcel_selection;
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
        setupListeners();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    private void setupViews() {
        availableParcelsRecyclerView = findViewById(R.id.new_reservation_available_parcels_recycler_view);
        addedParcelsRecyclerView = findViewById(R.id.new_reservation_added_parcels_recycler_view);
    }

    private void setupViewModels() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        addedParcels = new ArrayList<>();
    }

    private void setupRecyclerViews() {
        availableParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableParcelsAdapter = new AvailableParcelsAdapter(this, addedParcels, this::updateAddedParcels);
        availableParcelsRecyclerView.setAdapter(availableParcelsAdapter);

        parcelaViewModel.getAvailableParcelas().observe(this, parcelas -> {
            availableParcelsAdapter.submitList(parcelas);
        });

        addedParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addedParcelsAdapter = new AddedParcelsAdapter(this, this::updateAddedParcels);
        addedParcelsRecyclerView.setAdapter(addedParcelsAdapter);
        addedParcelsAdapter.submitList(addedParcels);
    }

    private void setupListeners() {
        findViewById(R.id.new_reservation_previous_button).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.new_reservation_confirm_button).setOnClickListener(v -> confirmReservation());
    }

    private void confirmReservation() {
        DialogUtils.showConfirmationDialog(
            this, "CONFIRMAR RESERVA",
            "¿Está seguro de que desea confirmar la reserva?",
            R.drawable.ic_checkmark,
            () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtras(Objects.requireNonNull(getIntent().getExtras()));
                resultIntent.putExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(addedParcels));
                resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_INSERT);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        );
    }

    public void updateAddedParcels(List<ParcelaOccupancy> updatedList) {
        addedParcels = new ArrayList<>(updatedList);
        addedParcelsAdapter.submitList(addedParcels);
        availableParcelsAdapter.submitList(availableParcelsAdapter.getCurrentList());
    }
}
