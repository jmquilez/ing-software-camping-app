package es.unizar.eina.T213_camping.ui.parcelas.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.src.ParcelUtils;
import es.unizar.eina.T213_camping.utils.src.DialogUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;

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

    private void initializeViews() {
        parcelNameInput = findViewById(R.id.modify_parcel_name_input);
        maxOccupantsInput = findViewById(R.id.modify_parcel_max_occupants_input);
        pricePerPersonInput = findViewById(R.id.modify_parcel_price_input);
        descriptionInput = findViewById(R.id.modify_parcel_description_input);
        saveChangesButton = findViewById(R.id.modify_parcel_save_button);
        deleteParcelButton = findViewById(R.id.modify_parcel_delete_button);
        errorMessage = findViewById(R.id.modify_parcel_error_message);
    }

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

    private void setupListeners() {
        saveChangesButton.setOnClickListener(v -> saveParcel());
        deleteParcelButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

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
    
    private void showDeleteConfirmationDialog() {
        DialogUtils.showConfirmationDialog(
            this,
            "Eliminar parcela",
            "¿Está seguro de que desea eliminar esta parcela?",
            R.drawable.ic_delete_confirm,
            this::deleteParcel
        );
    }

    private void deleteParcel() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ParcelConstants.PARCEL_NAME, parcelNameInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, ParcelConstants.OPERATION_DELETE);
    
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
