package es.unizar.eina.T213_camping.ui.parcelas.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.ParcelUtils;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;
import androidx.lifecycle.ViewModelProvider;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;

/**
 * Activity para modificar o eliminar una parcela existente.
 * Permite editar todos los campos de una parcela o eliminarla completamente del sistema.
 * 
 * Recibe los datos actuales de la parcela a través de un Intent con los extras:
 * - PARCEL_NAME: nombre de la parcela
 * - MAX_OCCUPANTS: ocupantes máximos
 * - PRICE_PER_PERSON: precio por persona
 * - DESCRIPTION: descripción
 */
public class ModifyParcelActivity extends BaseActivity {

    private EditText parcelNameInput, maxOccupantsInput, pricePerPersonInput, descriptionInput;
    private Button saveChangesButton, deleteParcelButton;
    private TextView errorMessage;

    private String parcelName;
    private int maxOccupants;
    private double pricePerPerson;
    private String description;

    private ParcelaViewModel parcelaViewModel;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_modify_parcel;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.modify_parcel_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        initializeViews();
        loadParcelData();
        setupListeners();
        setupInputValidation();
        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    /**
     * Inicializa las referencias a las vistas de la actividad.
     */
    private void initializeViews() {
        parcelNameInput = findViewById(R.id.modify_parcel_name_input);
        maxOccupantsInput = findViewById(R.id.modify_parcel_max_occupants_input);
        pricePerPersonInput = findViewById(R.id.modify_parcel_price_input);
        descriptionInput = findViewById(R.id.modify_parcel_description_input);
        saveChangesButton = findViewById(R.id.modify_parcel_save_button);
        deleteParcelButton = findViewById(R.id.modify_parcel_delete_button);
        errorMessage = findViewById(R.id.modify_parcel_error_message);
    }

    /**
     * Carga los datos de la parcela recibidos en el Intent.
     */
    private void loadParcelData() {
        parcelName = getIntent().getStringExtra(ParcelConstants.PARCEL_NAME);
        maxOccupants = getIntent().getIntExtra(ParcelConstants.MAX_OCCUPANTS, 0);
        pricePerPerson = getIntent().getDoubleExtra(ParcelConstants.PRICE_PER_PERSON, 0);
        description = getIntent().getStringExtra(ParcelConstants.DESCRIPTION);

        parcelNameInput.setText(parcelName);
        maxOccupantsInput.setText(String.valueOf(maxOccupants));
        pricePerPersonInput.setText(String.valueOf(pricePerPerson));
        descriptionInput.setText(description);
    }

    /**
     * Configura los listeners para los botones de guardar y eliminar.
     */
    private void setupListeners() {
        saveChangesButton.setOnClickListener(v -> saveParcel());
        deleteParcelButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    /**
     * Valida y guarda los cambios realizados en la parcela.
     * Si la validación es exitosa, devuelve los datos actualizados con OPERATION_UPDATE.
     */
    private void saveParcel() {
        ParcelUtils.validateInputsAsync(this, parcelNameInput, maxOccupantsInput, 
            pricePerPersonInput, errorMessage, parcelaViewModel, parcelName,
            isValid -> {
                if (isValid) {
                    String newName = parcelNameInput.getText().toString();
                    boolean isNameChanged = !newName.equals(parcelName);

                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(ParcelConstants.PARCEL_NAME, newName);
                    replyIntent.putExtra(ParcelConstants.OLD_PARCEL_NAME, parcelName);
                    replyIntent.putExtra(ParcelConstants.MAX_OCCUPANTS, 
                        Integer.parseInt(maxOccupantsInput.getText().toString()));
                    replyIntent.putExtra(ParcelConstants.PRICE_PER_PERSON, 
                        Double.parseDouble(pricePerPersonInput.getText().toString()));
                    replyIntent.putExtra(ParcelConstants.DESCRIPTION, descriptionInput.getText().toString());
                    replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, 
                        isNameChanged ? ParcelConstants.OPERATION_UPDATE_WITH_NAME : ParcelConstants.OPERATION_UPDATE);

                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            });
    }
    
    /**
     * Muestra un diálogo de confirmación antes de eliminar la parcela.
     */
    private void showDeleteConfirmationDialog() {
        DialogUtils.showConfirmationDialog(
            this,
            getString(R.string.delete_parcel_dialog_title),
            getString(R.string.delete_parcel_dialog_message),
            R.drawable.ic_delete_confirm,
            this::deleteParcel
        );
    }

    /**
     * Procesa la eliminación de la parcela.
     * Devuelve el nombre de la parcela con OPERATION_DELETE.
     */
    private void deleteParcel() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ParcelConstants.PARCEL_NAME, parcelNameInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, ParcelConstants.OPERATION_DELETE);
    
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void setupInputValidation() {
        // Name length validation
        parcelNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > ParcelConstants.MAX_NAME_LENGTH) {
                    s.delete(ParcelConstants.MAX_NAME_LENGTH, s.length());
                    parcelNameInput.setError(getString(R.string.error_name_too_long, ParcelConstants.MAX_NAME_LENGTH));
                }
            }
        });

        // Description length validation
        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > ParcelConstants.MAX_DESCRIPTION_LENGTH) {
                    s.delete(ParcelConstants.MAX_DESCRIPTION_LENGTH, s.length());
                    descriptionInput.setError(getString(R.string.error_description_too_long, ParcelConstants.MAX_DESCRIPTION_LENGTH));
                }
            }
        });

        // Max occupants validation
        maxOccupantsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        int value = Integer.parseInt(input);
                        if (value > ParcelConstants.MAX_OCCUPANTS_NUM) {
                            s.replace(0, s.length(), String.valueOf(ParcelConstants.MAX_OCCUPANTS_NUM));
                            maxOccupantsInput.setError(getString(R.string.error_occupants_too_high, ParcelConstants.MAX_OCCUPANTS_NUM));
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
        });

        // Price validation
        pricePerPersonInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        double value = Double.parseDouble(input);
                        if (value > ParcelConstants.MAX_PRICE) {
                            s.replace(0, s.length(), String.valueOf(ParcelConstants.MAX_PRICE));
                            pricePerPersonInput.setError(getString(R.string.error_price_too_high, ParcelConstants.MAX_PRICE));
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }
}
