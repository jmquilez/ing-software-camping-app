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

public class ParcelFeedActivity extends BaseActivity {

    private ParcelaViewModel parcelaViewModel;
    private RecyclerView parcelRecyclerView;
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
        return "Parcels";
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

    private void setupViewModel() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            Log.i("LIVEDATA_UPDATED, PARCELAS", parcelas.toString());
            parcelList = parcelas;
            sortParcels();
        });
    }

    private void setupRecyclerView() {
        parcelRecyclerView = findViewById(R.id.parcel_list_recycler_view);
        parcelAdapter = new ParcelAdapter(this, currentSortingCriteria, this::onParcelClick);
        parcelRecyclerView.setAdapter(parcelAdapter);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSortingButtons() {
        findViewById(R.id.parcel_sort_id_button).setOnClickListener(v -> {
            currentSortingCriteria = ParcelConstants.SORT_ID;
            // parcelAdapter.updateSortingCriteria(currentSortingCriteria);
            sortParcels();
        });
        findViewById(R.id.parcel_sort_occupants_button).setOnClickListener(v -> {
            currentSortingCriteria = ParcelConstants.SORT_MAX_OCCUPANTS;
            // parcelAdapter.updateSortingCriteria(currentSortingCriteria);
            sortParcels();
        });
        findViewById(R.id.parcel_sort_price_button).setOnClickListener(v -> {
            currentSortingCriteria = ParcelConstants.SORT_EUR_PERSONA;
            // parcelAdapter.updateSortingCriteria(currentSortingCriteria);
            sortParcels();
        });
    }

    private void setupCreateParcelButton() {
        findViewById(R.id.parcel_create_button).setOnClickListener(v -> 
            parcelLauncher.launch(new Intent(this, CreateParcelActivity.class))
        );
    }

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

            for (Parcela p: sortedList) {
                Log.d("PARCEL_X", String.valueOf(p.getMaxOcupantes()));
            }

            parcelAdapter.updateSortingCriteria(currentSortingCriteria);
            Log.d("PARCEL_LIST", parcelList.toString());
            parcelAdapter.submitList(sortedList);
            parcelList = sortedList;
        }
    }

    private void handleParcelResult(Intent data) {
        Bundle extras = data.getExtras();
        assert extras != null;
        String operationType = extras.getString(ParcelConstants.OPERATION_TYPE);
        String parcelName = extras.getString(ParcelConstants.PARCEL_NAME);

        String loadingMessage = "UNKNOWN OPERATION...";

        if (Objects.requireNonNull(operationType).equals(ParcelConstants.OPERATION_INSERT)) {
            loadingMessage = "CREANDO PARCELA...";
        } else if (operationType.equals(ParcelConstants.OPERATION_UPDATE)) {
            loadingMessage = "ACTUALIZANDO PARCELA...";
        } else if (operationType.equals(ParcelConstants.OPERATION_DELETE)) {
            loadingMessage = "ELIMINANDO PARCELA...";
        }

        final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, loadingMessage);

        new android.os.Handler().postDelayed(() -> {
            switch (Objects.requireNonNull(operationType)) {
                case ParcelConstants.OPERATION_INSERT:
                    insertParcel(extras);
                    break;
                case ParcelConstants.OPERATION_UPDATE:
                    updateParcel(extras);
                    break;
                case ParcelConstants.OPERATION_DELETE:
                    parcelaViewModel.deleteByNombre(parcelName);
                    DialogUtils.showSuccessDialog(this, "Parcela eliminada con éxito.", R.drawable.ic_delete_success);
                    break;
                default:
                    Toast.makeText(this, "Operación desconocida", Toast.LENGTH_SHORT).show();
                    break;
            }
            loadingDialog.dismiss();
            
            // Force refresh the list after operation
            // refreshParcelList();
        }, 2000);
    }

    private void insertParcel(Bundle extras) {
        Parcela parcela = new Parcela(
            extras.getString(ParcelConstants.PARCEL_NAME),
            extras.getString(ParcelConstants.DESCRIPTION),
            extras.getInt(ParcelConstants.MAX_OCCUPANTS),
            extras.getDouble(ParcelConstants.PRICE_PER_PERSON)
        );
        parcelaViewModel.insert(parcela);
        // TODO: conditional?
        DialogUtils.showSuccessDialog(this, "Parcela creada con éxito.", R.drawable.ic_create_success);
    }

    private void updateParcel(Bundle extras) {
        Parcela parcela = new Parcela(
            extras.getString(ParcelConstants.PARCEL_NAME),
            extras.getString(ParcelConstants.DESCRIPTION),
            extras.getInt(ParcelConstants.MAX_OCCUPANTS),
            extras.getDouble(ParcelConstants.PRICE_PER_PERSON)
        );
        parcelaViewModel.update(parcela);
        // refreshParcelList();
        // TODO: conditional?
        DialogUtils.showSuccessDialog(this, "Parcela actualizada con éxito.", R.drawable.ic_update_success);
    }

    private void onParcelClick(Parcela parcela) {
        Intent intent = new Intent(this, ModifyParcelActivity.class);
        intent.putExtra(ParcelConstants.PARCEL_NAME, parcela.getNombre());
        intent.putExtra(ParcelConstants.MAX_OCCUPANTS, parcela.getMaxOcupantes());
        intent.putExtra(ParcelConstants.PRICE_PER_PERSON, parcela.getEurPorPersona());
        intent.putExtra(ParcelConstants.DESCRIPTION, parcela.getDescripcion());
        parcelLauncher.launch(intent);
    }

    private void refreshParcelList() {
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            if (parcelas != null) {
                parcelList = parcelas;
                sortParcels();
            }
        });
    }
}
