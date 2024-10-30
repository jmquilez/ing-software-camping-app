package es.unizar.eina.T213_camping.ui.reservas.listado;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;
import es.unizar.eina.T213_camping.ui.reservas.listado.ReservationAdapter;
import es.unizar.eina.T213_camping.ui.reservas.creacion.CreateReservationActivity;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.ui.reservas.gestion.ModifyReservationActivity;
import es.unizar.eina.T213_camping.ui.BaseActivity;

public class ReservationFeedActivity extends BaseActivity {
    private ReservaViewModel reservaViewModel;
    private ParcelaViewModel parcelaViewModel;
    private ParcelaReservadaViewModel parcelaReservadaViewModel;
    private ReservationAdapter reservationAdapter;
    private List<Reserva> reservationList;
    private String currentSortingCriteria = ReservationConstants.SORT_CLIENT_NAME;

    private ActivityResultLauncher<Intent> reservationLauncher;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reservation_feed;
    }

    @Override
    protected String getToolbarTitle() {
        return "Reservations";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewModels();
        setupRecyclerView();
        setupSortingButtons();
        setupCreateReservationButton();
        setupReservationLauncher();

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    private void setupViewModels() {
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelaReservadaViewModel = new ViewModelProvider(this).get(ParcelaReservadaViewModel.class);
        reservaViewModel.getAllReservas().observe(this, reservations -> {
            /* TODO: in case a Reserva only gets its associated ParcelaReservada's updated,
             *  will this observer callback be triggered? Shouldn't be a problem, since we re-fetch
             *  the corresponding rows from "ParcelaReservada" every time we launch a "Reserva"
             *  modification Intent */
            reservationList = reservations;
            sortReservations();
        });
    }

    private void setupRecyclerView() {
        RecyclerView reservationRecyclerView = findViewById(R.id.reservation_recycler_view);
        reservationAdapter = new ReservationAdapter(this, currentSortingCriteria, this::onReservationClick);
        reservationRecyclerView.setAdapter(reservationAdapter);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSortingButtons() {
        // TODO: sorting buttons array?
        findViewById(R.id.sort_reservations_by_client_name_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_CLIENT_NAME));
        findViewById(R.id.sort_reservations_by_client_phone_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_CLIENT_PHONE));
        findViewById(R.id.sort_reservations_by_entry_date_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_ENTRY_DATE));
    }

    private void setupCreateReservationButton() {
        findViewById(R.id.create_new_reservation_button).setOnClickListener(v -> 
            reservationLauncher.launch(new Intent(this, CreateReservationActivity.class))
        );
    }

    private void setupReservationLauncher() {
        reservationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleReservationResult(result.getData());
                }
            }
        );
    }

    private void sortReservations(String criteria) {
        currentSortingCriteria = criteria;
        sortReservations();
    }

    private void sortReservations() {
        if (reservationList != null) {
            Comparator<Reserva> comparator;
            if (currentSortingCriteria.equals(ReservationConstants.SORT_CLIENT_PHONE)) {
                comparator = Comparator.comparing(Reserva::getTelefonoCliente);
            } else if (currentSortingCriteria.equals(ReservationConstants.SORT_ENTRY_DATE)) {
                comparator = Comparator.comparing(Reserva::getFechaEntrada);
            } else {
                comparator = Comparator.comparing(Reserva::getNombreCliente);
            }

            List<Reserva> sortedList = new ArrayList<>(reservationList);
            sortedList.sort(comparator);

            // NOTE: See https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
            reservationAdapter.submitList(sortedList);
            reservationAdapter.updateSortingCriteria(currentSortingCriteria);
            reservationList = sortedList;
        }
    }

    private void handleReservationResult(Intent data) {
        Bundle extras = data.getExtras();
        ArrayList<ParcelaOccupancy> selectedParcels = data.getParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS);
        assert extras != null;
        String operationType = extras.getString(ReservationConstants.OPERATION_TYPE);
        long reservationId = extras.getLong(ReservationConstants.RESERVATION_ID);

        String loadingMessage = "UNKNOWN OPERATION...";

        if (Objects.requireNonNull(operationType).equals(ParcelConstants.OPERATION_INSERT)) {
            loadingMessage = "CREANDO RESERVA...";
        } else if (operationType.equals(ParcelConstants.OPERATION_UPDATE)) {
            loadingMessage = "ACTUALIZANDO RESERVA...";
        } else if (operationType.equals(ParcelConstants.OPERATION_DELETE)) {
            loadingMessage = "ELIMINANDO RESERVA...";
        }

        final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, loadingMessage);

        new android.os.Handler().postDelayed(() -> {
            switch (operationType) {
                case ReservationConstants.OPERATION_INSERT:
                    insertReservation(extras, selectedParcels);
                    break;
                case ReservationConstants.OPERATION_UPDATE:
                    updateReservation(extras, selectedParcels);
                    break;
                case ReservationConstants.OPERATION_DELETE:
                    reservaViewModel.deleteById(reservationId);
                    DialogUtils.showSuccessDialog(this, "Reserva eliminada con éxito.", R.drawable.ic_delete_success);
                    break;
                case ReservationConstants.OPERATION_NOTIFY_CLIENT:
                    notifyClient(reservationId);
                    break;
                default:
                    Toast.makeText(this, "Operación desconocida", Toast.LENGTH_SHORT).show();
                    break;
            }
            loadingDialog.dismiss();
        }, 2000);
    }

    private void notifyClient(long reservationId) {
        // Placeholder for actual notification logic
        // In a real implementation, you would call your notification service here
        
        // For now, we'll just show a success dialog
        DialogUtils.showSuccessDialog(this, "Cliente notificado con éxito.", R.drawable.ic_notify_success);
    }

    private void insertReservation(Bundle extras, ArrayList<ParcelaOccupancy> selectedParcels) {
        Reserva reservation = new Reserva(
                Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_NAME)),
                Objects.requireNonNull(extras.getString(ReservationConstants.ENTRY_DATE)),
                Objects.requireNonNull(extras.getString(ReservationConstants.DEPARTURE_DATE)),
                Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_PHONE))
        );

        assert selectedParcels != null;
        for (ParcelaOccupancy parcelaOccupancy : selectedParcels) {
            ParcelaReservada parcelaReservada = new ParcelaReservada(
                parcelaOccupancy.getParcela().getNombre(),
                reservation.getId(),
                parcelaOccupancy.getOccupancy()
            );
            parcelaReservadaViewModel.insert(parcelaReservada);
        }
        DialogUtils.showSuccessDialog(this, "Reserva creada con éxito.", R.drawable.ic_create_success);
    }

    private void updateReservation(Bundle extras, ArrayList<ParcelaOccupancy> selectedParcels) {
        long reservationId = extras.getLong(ReservationConstants.RESERVATION_ID);
        Reserva updatedReservation = new Reserva(
                Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_NAME)),
                Objects.requireNonNull(extras.getString(ReservationConstants.ENTRY_DATE)),
                Objects.requireNonNull(extras.getString(ReservationConstants.DEPARTURE_DATE)),
                Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_PHONE))
        );

        // NOTE: revise if the reserva has actually changed? But be careful, because we might have
        // to trigger a reupdate of the RecyclerView's UI
        reservaViewModel.update(updatedReservation);

        // Update ParcelasReservadas
        // ArrayList<ParcelaOccupancy> selectedParcels = (ArrayList<ParcelaOccupancy>) extras.getSerializable(ReservationConstants.SELECTED_PARCELS);
        assert selectedParcels != null;
        List<ParcelaReservada> parcelasReservadas = selectedParcels.stream()
            .map(po -> new ParcelaReservada(po.getParcela().getNombre(), reservationId, po.getOccupancy()))
            .collect(Collectors.toList());
        parcelaReservadaViewModel.updateParcelasForReservation(reservationId, parcelasReservadas);

        DialogUtils.showSuccessDialog(this, "Reserva actualizada con éxito.", R.drawable.ic_update_success);
    }

    private void onReservationClick(Reserva reserva) {
        Intent intent = new Intent(this, ModifyReservationActivity.class);
        intent.putExtra(ReservationConstants.RESERVATION_ID, reserva.getId());
        intent.putExtra(ReservationConstants.CLIENT_NAME, reserva.getNombreCliente());
        intent.putExtra(ReservationConstants.CLIENT_PHONE, reserva.getTelefonoCliente());
        intent.putExtra(ReservationConstants.ENTRY_DATE, reserva.getFechaEntrada());
        intent.putExtra(ReservationConstants.DEPARTURE_DATE, reserva.getFechaSalida());

        // NOTE: See https://www.youtube.com/watch?v=ESM3NwSqJFo
        LiveData<List<ParcelaOccupancy>> parcelasReservadasLiveData = parcelaViewModel.getParcelasByReservationId(reserva.getId());
        parcelasReservadasLiveData.observe(this, parcelasReservadas -> {
            if (parcelasReservadas == null) {
                parcelasReservadas = new ArrayList<>();
            }
            Log.i("PUT_PARCELABLE_ARRAY_EXTRA", "parcelasReservadas: " + parcelasReservadas);
            intent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(parcelasReservadas));
            reservationLauncher.launch(intent);
            // KEY: remove all observers handled by the current lifecycle owner (this activity)
            //      from `parcelaViewModel.getParcelasByReservationId` (we'll just retrieve the data once)
            parcelasReservadasLiveData.removeObservers(this);
        });


    }
}
