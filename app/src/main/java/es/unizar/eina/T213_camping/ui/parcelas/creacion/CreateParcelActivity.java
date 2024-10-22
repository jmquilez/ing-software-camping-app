package es.unizar.eina.T213_camping.ui.parcelas.creacion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.src.ParcelUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;

public class CreateParcelActivity extends BaseActivity {

    private EditText parcelNameInput, maxOccupantsInput, pricePerPersonInput, descriptionInput;
    private TextView errorMessage;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parcelNameInput = findViewById(R.id.create_parcel_name_input);
        maxOccupantsInput = findViewById(R.id.create_parcel_max_occupants_input);
        pricePerPersonInput = findViewById(R.id.create_parcel_price_input);
        descriptionInput = findViewById(R.id.create_parcel_description_input);
        Button createParcelButton = findViewById(R.id.create_parcel_submit_button);
        errorMessage = findViewById(R.id.create_parcel_error_message);

        createParcelButton.setOnClickListener(v -> createParcel());

        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_parcel;
    }

    @Override
    protected String getToolbarTitle() {
        return "Create Parcel";
    }

    // TODO: back button? => CREATE LAYOUT

    private void createParcel() {
        if (!ParcelUtils.validateInputs(this, parcelNameInput, maxOccupantsInput, pricePerPersonInput, errorMessage)) {
            return;
        }

        // Prepare the replyIntent with the data
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ParcelConstants.PARCEL_NAME, parcelNameInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.MAX_OCCUPANTS, maxOccupantsInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.PRICE_PER_PERSON, pricePerPersonInput.getText().toString());
        replyIntent.putExtra(ParcelConstants.DESCRIPTION, descriptionInput.getText().toString());

        // Add the operation type flag (e.g., "update")
        replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, ParcelConstants.OPERATION_INSERT);

        // Set result with the reply intent but do not finish yet
        setResult(RESULT_OK, replyIntent);
        
        // Leave the loading dialog showing until ParcelFeedActivity handles it
        finish();
    }
}
