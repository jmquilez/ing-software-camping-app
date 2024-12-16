package es.unizar.eina.T213_camping.ui.parcelas.creacion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.ParcelUtils;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;
import androidx.lifecycle.ViewModelProvider;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;

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

        parcelNameInput = findViewById(R.id.create_parcel_name_input);
        maxOccupantsInput = findViewById(R.id.create_parcel_max_occupants_input);
        pricePerPersonInput = findViewById(R.id.create_parcel_price_input);
        descriptionInput = findViewById(R.id.create_parcel_description_input);
        Button confirmParcelButton = findViewById(R.id.create_parcel_submit_button);
        errorMessage = findViewById(R.id.create_parcel_error_message);

        confirmParcelButton.setOnClickListener(v -> createParcel());

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
