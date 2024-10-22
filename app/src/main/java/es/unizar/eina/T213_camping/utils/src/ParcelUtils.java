package es.unizar.eina.T213_camping.utils.src;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ParcelUtils {

    public static boolean validateInputs(Context context, EditText parcelNameInput, EditText maxOccupantsInput, EditText pricePerPersonInput, TextView errorMessage) {
        String parcelName = parcelNameInput.getText().toString().trim();
        String maxOccupants = maxOccupantsInput.getText().toString().trim();
        String pricePerPerson = pricePerPersonInput.getText().toString().trim();

        if (parcelName.isEmpty() || maxOccupants.isEmpty() || pricePerPerson.isEmpty()) {
            errorMessage.setText("Por favor, complete todos los campos.");
            errorMessage.setVisibility(View.VISIBLE);
            return false;
        }

        // TODO: implement number picker
        try {
            int occupants = Integer.parseInt(maxOccupants);
            if (occupants <= 0) {
                errorMessage.setText("El número máximo de ocupantes debe ser mayor que cero.");
                errorMessage.setVisibility(View.VISIBLE);
                return false;
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Por favor, ingrese un número válido para el máximo de ocupantes.");
            errorMessage.setVisibility(View.VISIBLE);
            return false;
        }

        try {
            double price = Double.parseDouble(pricePerPerson);
            if (price <= 0) {
                errorMessage.setText("El precio por persona debe ser mayor que cero.");
                errorMessage.setVisibility(View.VISIBLE);
                return false;
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Por favor, ingrese un número válido para el precio por persona.");
            errorMessage.setVisibility(View.VISIBLE);
            return false;
        }

        errorMessage.setVisibility(View.GONE);
        return true;
    }
}
