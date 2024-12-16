package es.unizar.eina.T213_camping.ui.parcelas.listado;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.listado.ParcelAdapter;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;
import es.unizar.eina.T213_camping.ui.parcelas.gestion.ModifyParcelActivity;
import es.unizar.eina.T213_camping.ui.parcelas.creacion.CreateParcelActivity;
import es.unizar.eina.T213_camping.utils.DialogUtils;

/**
 * Activity que muestra la lista de parcelas disponibles.
 * Permite crear, modificar y eliminar parcelas, así como ordenarlas según diferentes criterios.
 */
public class ParcelFeedActivity extends BaseActivity {

    private ParcelaViewModel parcelaViewModel;
    private ParcelAdapter parcelAdapter;
    private List<Parcela> parcelList;
    private String currentSortingCriteria = ParcelConstants.SORT_ID;

    private ActivityResultLauncher<Intent> parcelLauncher;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_parcel_feed;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.parcels_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NOTE: execute later (parcelAdapter hasn't even been created yet)
        setupViewModel();
        setupRecyclerView();
        setupSortingButtons();
        setupCreateParcelButton();
        setupParcelLauncher();

        // Set button visibility
        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    /**
     * Configura el ViewModel y observa los cambios en la lista de parcelas.
     */
    private void setupViewModel() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            Log.i("LIVEDATA_UPDATED, PARCELAS", parcelas.toString());
            parcelList = parcelas;
            sortParcels();
        });
    }

    /**
     * Configura el RecyclerView y su adaptador.
     */
    private void setupRecyclerView() {
        RecyclerView parcelRecyclerView = findViewById(R.id.parcel_list_recycler_view);
        parcelAdapter = new ParcelAdapter(this, currentSortingCriteria, this::onParcelClick);
        parcelRecyclerView.setAdapter(parcelAdapter);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Configura los botones de ordenación.
     */
    private void setupSortingButtons() {
        findViewById(R.id.parcel_sort_id_button).setOnClickListener(v -> sortParcels(ParcelConstants.SORT_ID));
        findViewById(R.id.parcel_sort_occupants_button).setOnClickListener(v -> sortParcels(ParcelConstants.SORT_MAX_OCCUPANTS));
        findViewById(R.id.parcel_sort_price_button).setOnClickListener(v -> sortParcels(ParcelConstants.SORT_EUR_PERSONA));
    }

    /**
     * Configura el botón para crear nuevas parcelas.
     */
    private void setupCreateParcelButton() {
        findViewById(R.id.parcel_create_button).setOnClickListener(v -> 
            parcelLauncher.launch(new Intent(this, CreateParcelActivity.class))
        );
    }

    /**
     * Configura el launcher para gestionar los resultados de las actividades de creación/modificación.
     */
    private void setupParcelLauncher() {
        parcelLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleParcelResult(result.getData());
                }
            }
        );
    }

    /**
     * Ordena las parcelas según el criterio especificado.
     * @param criteria Criterio de ordenación
     */
    private void sortParcels(String criteria) {
        currentSortingCriteria = criteria;
        sortParcels();
    }

    /**
     * Ordena las parcelas según el criterio actual.
     * Aplica el comparador correspondiente y actualiza el adaptador.
     */
    private void sortParcels() {
        if (parcelList != null) {
            Comparator<Parcela> comparator;
            if (currentSortingCriteria.equals(ParcelConstants.SORT_MAX_OCCUPANTS)) {
                comparator = Comparator.comparingInt(Parcela::getMaxOcupantes);
            } else if (currentSortingCriteria.equals(ParcelConstants.SORT_EUR_PERSONA)) {
                comparator = Comparator.comparingDouble(Parcela::getEurPorPersona);
            } else {
                comparator = Comparator.comparing(Parcela::getNombre);
            }

            List<Parcela> sortedList = new ArrayList<>(parcelList);
            sortedList.sort(comparator);

            // NOTE: See https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
            parcelAdapter.submitList(sortedList);
            parcelAdapter.updateSortingCriteria(currentSortingCriteria);
            parcelList = sortedList;
        }
    }

    /**
     * Maneja el resultado de las operaciones de creación/modificación/eliminación de parcelas.
     * @param data Intent con los datos de la operación
     */
    private void handleParcelResult(Intent data) {
        Bundle extras = data.getExtras();
        assert extras != null;
        String operationType = extras.getString(ParcelConstants.OPERATION_TYPE);
        String parcelName = extras.getString(ParcelConstants.PARCEL_NAME);

        String loadingMessage = getString(R.string.error_unknown_operation);

        if (Objects.requireNonNull(operationType).equals(ParcelConstants.OPERATION_INSERT)) {
            loadingMessage = getString(R.string.loading_create_parcel);
        } else if (operationType.equals(ParcelConstants.OPERATION_UPDATE) || 
                   operationType.equals(ParcelConstants.OPERATION_UPDATE_WITH_NAME)) {
            loadingMessage = getString(R.string.loading_update_parcel);
        } else if (operationType.equals(ParcelConstants.OPERATION_DELETE)) {
            loadingMessage = getString(R.string.loading_delete_parcel);
        }

        final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, loadingMessage);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            long result = -1;
            switch (Objects.requireNonNull(operationType)) {
                case ParcelConstants.OPERATION_INSERT:
                    result = insertParcel(extras);
                    showResultDialog(result, 
                        getString(R.string.success_create_parcel),
                        R.drawable.ic_create_success,
                        getString(R.string.error_create_parcel));
                    break;

                case ParcelConstants.OPERATION_UPDATE:
                case ParcelConstants.OPERATION_UPDATE_WITH_NAME:
                    result = updateParcel(extras, operationType.equals(ParcelConstants.OPERATION_UPDATE_WITH_NAME));
                    showResultDialog(result,
                        getString(R.string.success_update_parcel),
                        R.drawable.ic_update_success,
                        getString(R.string.error_update_parcel));
                    break;

                case ParcelConstants.OPERATION_DELETE:
                    result = parcelaViewModel.deleteByNombre(parcelName);
                    showResultDialog(result,
                        getString(R.string.success_delete_parcel),
                        R.drawable.ic_delete_success,
                        getString(R.string.error_delete_parcel));
                    break;

                default:
                    Toast.makeText(this, getString(R.string.error_unknown_operation), Toast.LENGTH_SHORT).show();
                    break;
            }
            loadingDialog.dismiss();
        }, 2000);
    }

    /**
     * Inserta una nueva parcela en la base de datos.
     * Muestra un diálogo de éxito al completar la operación.
     * @param extras Bundle con los datos de la nueva parcela
     */
    private long insertParcel(Bundle extras) {
        try {
            Parcela parcela = new Parcela(
                Objects.requireNonNull(extras.getString(ParcelConstants.PARCEL_NAME)),
                Objects.requireNonNull(extras.getString(ParcelConstants.DESCRIPTION)),
                Integer.parseInt(Objects.requireNonNull(extras.getString(ParcelConstants.MAX_OCCUPANTS))),
                Double.parseDouble(Objects.requireNonNull(extras.getString(ParcelConstants.PRICE_PER_PERSON)))
            );
            return parcelaViewModel.insert(parcela);
        } catch (NumberFormatException e) {
            Log.e("ParcelFeedActivity", "Error parsing parcel data: " + e.getMessage());
            DialogUtils.showErrorDialog(this, getString(R.string.error_process_parcel_data));
            return -1;
        } catch (Exception e) {
            Log.e("ParcelFeedActivity", "Error creating parcel: " + e.getMessage());
            DialogUtils.showErrorDialog(this, getString(R.string.error_create_parcel));
            return -1;
        }
    }

    /**
     * Actualiza una parcela existente en la base de datos.
     * Muestra un diálogo de éxito al completar la operación.
     * @param extras Bundle con los datos actualizados de la parcela
     */
    private long updateParcel(Bundle extras, boolean isNameChange) {
        try {
            Parcela parcela = new Parcela(
                Objects.requireNonNull(extras.getString(ParcelConstants.PARCEL_NAME)),
                Objects.requireNonNull(extras.getString(ParcelConstants.DESCRIPTION)),
                extras.getInt(ParcelConstants.MAX_OCCUPANTS),
                extras.getDouble(ParcelConstants.PRICE_PER_PERSON)
            );

            if (isNameChange) {
                String oldName = extras.getString(ParcelConstants.OLD_PARCEL_NAME);
                parcelaViewModel.updateWithNameChange(oldName, parcela);
                return 1; // Assuming success since updateWithNameChange doesn't return a value
            } else {
                return parcelaViewModel.update(parcela);
            }
        } catch (Exception e) {
            Log.e("ParcelFeedActivity", "Error updating parcel: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Maneja el evento de clic en una parcela.
     * Inicia ModifyParcelActivity con los datos de la parcela seleccionada.
     * @param parcela Parcela seleccionada
     */
    private void onParcelClick(Parcela parcela) {
        Intent intent = new Intent(this, ModifyParcelActivity.class);
        intent.putExtra(ParcelConstants.PARCEL_NAME, parcela.getNombre());
        intent.putExtra(ParcelConstants.MAX_OCCUPANTS, parcela.getMaxOcupantes());
        intent.putExtra(ParcelConstants.PRICE_PER_PERSON, parcela.getEurPorPersona());
        intent.putExtra(ParcelConstants.DESCRIPTION, parcela.getDescripcion());
        parcelLauncher.launch(intent);
    }

    /**
     * Muestra un diálogo de éxito o error según el resultado de una operación.
     * 
     * @param result Resultado de la operación (>0 éxito, <=0 error)
     * @param successMessage Mensaje a mostrar en caso de éxito
     * @param successIcon Icono a mostrar en caso de éxito
     * @param errorMessage Mensaje a mostrar en caso de error
     */
    private void showResultDialog(long result, String successMessage, int successIcon, String errorMessage) {
        if (result > 0) {
            DialogUtils.showSuccessDialog(this, successMessage, successIcon);
        } else {
            DialogUtils.showErrorDialog(this, errorMessage);
        }
    }
}
