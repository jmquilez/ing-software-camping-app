package es.unizar.eina.T213_camping.ui.parcelas.creacion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.ParcelUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;
import androidx.lifecycle.ViewModelProvider;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import java.util.Locale;

/**
 * Activity que permite crear una nueva parcela en el sistema.
 * 
 * Esta actividad proporciona una interfaz para que el usuario introduzca:
 * - Nombre único de la parcela (identificador)
 * - Número máximo de ocupantes permitidos
 * - Precio por persona por noche
 * - Descripción detallada de la parcela
 * 
 * La actividad realiza validaciones en tiempo real y muestra mensajes de error
 * cuando los datos introducidos no son válidos.
 * 
 * Al confirmar la creación, devuelve los datos a ParcelFeedActivity a través
 * de un Intent con los siguientes extras:
 * - PARCEL_NAME: nombre de la parcela
 * - MAX_OCCUPANTS: número máximo de ocupantes
 * - PRICE_PER_PERSON: precio por persona
 * - DESCRIPTION: descripción de la parcela
 * - OPERATION_TYPE: tipo de operación (OPERATION_INSERT)
 */
public class CreateParcelActivity extends BaseActivity {

    private EditText parcelNameInput, maxOccupantsInput, pricePerPersonInput, descriptionInput;
    private TextView errorMessage;
    private Dialog loadingDialog;
    private ParcelaViewModel parcelaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        setupInputs();
        setupInputValidation();
        setButtonVisibility("back", true);
        setButtonVisibility("home", true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_parcel;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.create_parcel_title);
    }

    private void setupInputs() {
        parcelNameInput = findViewById(R.id.create_parcel_name_input);
        maxOccupantsInput = findViewById(R.id.create_parcel_max_occupants_input);
        pricePerPersonInput = findViewById(R.id.create_parcel_price_input);
        descriptionInput = findViewById(R.id.create_parcel_description_input);
        Button confirmParcelButton = findViewById(R.id.create_parcel_submit_button);
        errorMessage = findViewById(R.id.create_parcel_error_message);

        confirmParcelButton.setOnClickListener(v -> createParcel());
    }

    private void setupInputValidation() {
        // Name length validation
        parcelNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > ParcelConstants.MAX_NAME_LENGTH) {
                    s.delete(ParcelConstants.MAX_NAME_LENGTH, s.length());
                    parcelNameInput.setError(getString(R.string.error_name_too_long, ParcelConstants.MAX_NAME_LENGTH));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Description length validation
        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > ParcelConstants.MAX_DESCRIPTION_LENGTH) {
                    s.delete(ParcelConstants.MAX_DESCRIPTION_LENGTH, s.length());
                    descriptionInput.setError(getString(R.string.error_description_too_long, ParcelConstants.MAX_DESCRIPTION_LENGTH));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Max occupants validation
        maxOccupantsInput.addTextChangedListener(new TextWatcher() {
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Price validation
        pricePerPersonInput.addTextChangedListener(new TextWatcher() {
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Valida y procesa la creación de una nueva parcela.
     * 
     * El proceso incluye:
     * 1. Validación de todos los campos usando ParcelUtils
     * 2. Si la validación es exitosa:
     *    - Crea un Intent con todos los datos de la parcela
     *    - Establece el tipo de operación como OPERATION_INSERT
     *    - Devuelve el resultado a la actividad anterior
     * 3. Si la validación falla:
     *    - Muestra el mensaje de error correspondiente
     *    - Mantiene al usuario en la pantalla de creación
     */
    private void createParcel() {
        ParcelUtils.validateInputsAsync(this, parcelNameInput, maxOccupantsInput, 
            pricePerPersonInput, errorMessage, parcelaViewModel, null, 
            isValid -> {
                if (isValid) {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(ParcelConstants.PARCEL_NAME, parcelNameInput.getText().toString());
                    replyIntent.putExtra(ParcelConstants.MAX_OCCUPANTS, maxOccupantsInput.getText().toString());
                    replyIntent.putExtra(ParcelConstants.PRICE_PER_PERSON, pricePerPersonInput.getText().toString());
                    replyIntent.putExtra(ParcelConstants.DESCRIPTION, descriptionInput.getText().toString());
                    replyIntent.putExtra(ParcelConstants.OPERATION_TYPE, ParcelConstants.OPERATION_INSERT);

                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            });
    }
}
