package es.unizar.eina.T213_camping.ui.parcelas.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.ParcelUtils;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;

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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_modify_parcel;
    }

    @Override
    protected String getToolbarTitle() {
        return "Modify Parcel";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();
        loadParcelData();
        setupListeners();

        // Set button visibility
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
        // TODO: make a NumberPicker
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
        if (!ParcelUtils.validateInputs(this, parcelNameInput, maxOccupantsInput, pricePerPersonInput, errorMessage)) {
            return;
        }
    
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ParcelConstants.PARCEL_NAME, parcelNameInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.MAX_OCCUPANTS, Integer.parseInt(maxOccupantsInput.getText().toString()));
        replyIntent.putExtra(ParcelConstants.PRICE_PER_PERSON, Double.parseDouble(pricePerPersonInput.getText().toString()));
        replyIntent.putExtra(ParcelConstants.DESCRIPTION, descriptionInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, ParcelConstants.OPERATION_UPDATE);

        setResult(RESULT_OK, replyIntent);
        finish();
    }
    
    /**
     * Muestra un diálogo de confirmación antes de eliminar la parcela.
     */
    private void showDeleteConfirmationDialog() {
        DialogUtils.showConfirmationDialog(
            this,
            "Eliminar parcela",
            "¿Está seguro de que desea eliminar esta parcela?",
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
}
