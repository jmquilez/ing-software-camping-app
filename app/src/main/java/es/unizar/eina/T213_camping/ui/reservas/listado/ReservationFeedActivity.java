package es.unizar.eina.T213_camping.ui.reservas.listado;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
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
import es.unizar.eina.T213_camping.utils.DateUtils;
import es.unizar.eina.T213_camping.ui.reservas.gestion.ModifyReservationActivity;
import es.unizar.eina.T213_camping.ui.BaseActivity;

/**
 * Activity que muestra y gestiona la lista de reservas del camping.
 * Permite crear nuevas reservas, modificar existentes y ordenar la lista
 * según diferentes criterios.
 */
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

    /**
     * Configura los ViewModels necesarios y observa los cambios en las reservas.
     */
    private void setupViewModels() {
        /*
            * TODO: in case a Reserva only gets its associated ParcelaReservada's updated,
            * will this observer callback be triggered? Shouldn't be a problem, since we re-fetch
            * the corresponding rows from "ParcelaReservada" every time we launch a "Reserva"
            * modification Intent
         */
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelaReservadaViewModel = new ViewModelProvider(this).get(ParcelaReservadaViewModel.class);
        reservaViewModel.getAllReservas().observe(this, reservations -> {
            reservationList = reservations;
            sortReservations();
        });
    }

    /**
     * Configura el RecyclerView y su adaptador.
     */
    private void setupRecyclerView() {
        RecyclerView reservationRecyclerView = findViewById(R.id.reservation_recycler_view);
        reservationAdapter = new ReservationAdapter(this, currentSortingCriteria, this::onReservationClick);
        reservationRecyclerView.setAdapter(reservationAdapter);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Configura los botones de ordenación.
     */
    private void setupSortingButtons() {
        // TODO: sorting buttons array?
        findViewById(R.id.sort_reservations_by_client_name_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_CLIENT_NAME));
        findViewById(R.id.sort_reservations_by_client_phone_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_CLIENT_PHONE));
        findViewById(R.id.sort_reservations_by_entry_date_button).setOnClickListener(v -> sortReservations(ReservationConstants.SORT_ENTRY_DATE));
    }

    /**
     * Configura el botón para crear nuevas reservas.
     */
    private void setupCreateReservationButton() {
        findViewById(R.id.create_new_reservation_button).setOnClickListener(v -> 
            reservationLauncher.launch(new Intent(this, CreateReservationActivity.class))
        );
    }

    /**
     * Configura el launcher para gestionar los resultados de las actividades de creación/modificación.
     */
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

    /**
     * Actualiza el criterio de ordenación y reordena la lista.
     * @param criteria Nuevo criterio de ordenación
     */
    private void sortReservations(String criteria) {
        currentSortingCriteria = criteria;
        sortReservations();
    }

    /**
     * Ordena las reservas según el criterio actual.
     */
    private void sortReservations() {
        // NOTE: See https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
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

    /**
     * Procesa el resultado de las operaciones sobre reservas.
     * @param data Intent con los datos de la operación
     */
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
            if (!isFinishing() && !isDestroyed()) {  // Check if activity is still alive
                switch (operationType) {
                    case ReservationConstants.OPERATION_INSERT:
                        insertReservation(extras, selectedParcels);
                        break;
                    case ReservationConstants.OPERATION_UPDATE:
                        updateReservation(extras);
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
            }
            if (loadingDialog.isShowing()) {  // Check if dialog is still showing
                loadingDialog.dismiss();
            }
        }, 2000);
    }

    /**
     * Notifica al cliente sobre su reserva.
     * Actualmente es un placeholder para la lógica real de notificación.
     * @param reservationId ID de la reserva
     */
    private void notifyClient(long reservationId) {
        DialogUtils.showSuccessDialog(this, "Cliente notificado con éxito.", R.drawable.ic_notify_success);
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     * @param extras Bundle con los datos de la nueva reserva
     * @param selectedParcels Lista de parcelas seleccionadas
     */
    private void insertReservation(Bundle extras, ArrayList<ParcelaOccupancy> selectedParcels) {
        try {
            // Get dates as longs (milliseconds since epoch), consistent with updateReservation
            long entryDateMillis = extras.getLong(ReservationConstants.ENTRY_DATE);
            long departureDateMillis = extras.getLong(ReservationConstants.DEPARTURE_DATE);
            
            if (entryDateMillis == 0 || departureDateMillis == 0) {
                throw new IllegalArgumentException("Fechas no válidas");
            }
            
            Date entryDate = new Date(entryDateMillis);
            Date departureDate = new Date(departureDateMillis);
            double price = extras.getDouble(ReservationConstants.RESERVATION_PRICE, 0.0);

            Reserva reservation = new Reserva(
                    Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_NAME)),
                    entryDate,
                    departureDate,
                    Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_PHONE)),
                    price
            );

            // Insert the reservation and get its ID
            long newReservationId = reservaViewModel.insert(reservation);

            // Insert associated parcels
            assert selectedParcels != null;
            for (ParcelaOccupancy parcelaOccupancy : selectedParcels) {
                ParcelaReservada parcelaReservada = new ParcelaReservada(
                    parcelaOccupancy.getParcela().getNombre(),
                    newReservationId,
                    parcelaOccupancy.getOccupancy()
                );
                parcelaReservadaViewModel.insert(parcelaReservada);
            }

            DialogUtils.showSuccessDialog(this, "Reserva creada con éxito.", R.drawable.ic_create_success);

        } catch (Exception e) {
            Log.e("ReservationFeedActivity", "Error creating reservation: " + e.getMessage());
            DialogUtils.showErrorDialog(this, "Error al crear la reserva: " + e.getMessage());
        }
    }

    /**
     * Actualiza una reserva existente.
     * @param extras Bundle con los datos actualizados
     */
    private void updateReservation(Bundle extras) {
        if (extras == null) {
            Log.e("RESERVATION_UPDATE", "Error: extras es null");
            Toast.makeText(this, "Error: datos de reserva no disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            long reservationId = extras.getLong(ReservationConstants.RESERVATION_ID);
            String clientName = Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_NAME));
            String clientPhone = Objects.requireNonNull(extras.getString(ReservationConstants.CLIENT_PHONE));
            
            // The dates are coming as longs (milliseconds since epoch)
            long entryDateMillis = extras.getLong(ReservationConstants.ENTRY_DATE);
            long departureDateMillis = extras.getLong(ReservationConstants.DEPARTURE_DATE);
            
            Date entryDate = new Date(entryDateMillis);
            Date departureDate = new Date(departureDateMillis);
            
            // Get price from extras
            double price = extras.getDouble(ReservationConstants.RESERVATION_PRICE, 0.0);
            
            Reserva updatedReservation = new Reserva(clientName, entryDate, departureDate, clientPhone, price);
            updatedReservation.setId(reservationId);

            // Update the reservation
            reservaViewModel.update(updatedReservation);

            // Update ParcelasReservadas
            ArrayList<ParcelaOccupancy> selectedParcels = extras.getParcelableArrayList(ReservationConstants.SELECTED_PARCELS);
            if (selectedParcels != null) {
                List<ParcelaReservada> parcelasReservadas = selectedParcels.stream()
                    .map(po -> new ParcelaReservada(
                        po.getParcela().getNombre(),
                        reservationId,
                        po.getOccupancy()
                    ))
                    .collect(Collectors.toList());
                parcelaReservadaViewModel.updateParcelasForReservation(reservationId, parcelasReservadas);
            }

            DialogUtils.showSuccessDialog(this, "Reserva actualizada con éxito.", R.drawable.ic_update_success);
        } catch (Exception e) {
            Log.e("ReservationFeedActivity", "Error updating reservation: " + e.getMessage(), e);
            DialogUtils.showErrorDialog(this, "Error al actualizar la reserva: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento de clic en una reserva.
     * Inicia ModifyReservationActivity con los datos de la reserva seleccionada.
     * @param reserva Reserva seleccionada
     */
    private void onReservationClick(Reserva reserva) {
        Intent intent = new Intent(this, ModifyReservationActivity.class);
        
        // Añadir logs antes de poner los extras
        Log.d("ReservationFeed", "Datos de la reserva a modificar:");
        Log.d("ReservationFeed", "ID: " + reserva.getId());
        Log.d("ReservationFeed", "Nombre: " + reserva.getNombreCliente());
        Log.d("ReservationFeed", "Teléfono: " + reserva.getTelefonoCliente());
        Log.d("ReservationFeed", "Fecha Entrada (Date): " + reserva.getFechaEntrada());
        Log.d("ReservationFeed", "Fecha Salida (Date): " + reserva.getFechaSalida());
        Log.d("ReservationFeed", "Precio: " + reserva.getPrecio());
        
        // Formatear y verificar las fechas antes de añadirlas al intent
        String entryDateStr = DateUtils.DATE_FORMAT.format(reserva.getFechaEntrada());
        String departureDateStr = DateUtils.DATE_FORMAT.format(reserva.getFechaSalida());
        
        Log.d("ReservationFeed", "Fecha Entrada (formateada): " + entryDateStr);
        Log.d("ReservationFeed", "Fecha Salida (formateada): " + departureDateStr);
        
        intent.putExtra(ReservationConstants.RESERVATION_ID, reserva.getId());
        intent.putExtra(ReservationConstants.CLIENT_NAME, reserva.getNombreCliente());
        intent.putExtra(ReservationConstants.CLIENT_PHONE, reserva.getTelefonoCliente());
        intent.putExtra(ReservationConstants.ENTRY_DATE, entryDateStr);
        intent.putExtra(ReservationConstants.DEPARTURE_DATE, departureDateStr);
        intent.putExtra(ReservationConstants.RESERVATION_PRICE, reserva.getPrecio());

        // Verificar que los extras se añadieron correctamente
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("ReservationFeed", "Extras añadidos al intent:");
            for (String key : extras.keySet()) {
                Log.d("ReservationFeed", key + ": " + extras.get(key));
            }
        }

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
