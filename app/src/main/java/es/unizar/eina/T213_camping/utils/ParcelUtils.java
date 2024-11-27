package es.unizar.eina.T213_camping.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;

/**
 * Utilidades para la validación de datos de parcelas.
 * Proporciona métodos para validar los campos de entrada al crear o modificar parcelas.
 */
public class ParcelUtils {

    public interface ValidationCallback {
        void onValidationResult(boolean isValid);
    }

    /**
     * Valida los campos de entrada de una parcela.
     * Verifica que:
     * - Ningún campo esté vacío
     * - El nombre no esté duplicado
     * - El número de ocupantes sea válido y mayor que cero
     * - El precio por persona sea válido y mayor que cero
     * 
     * @param context Contexto de la aplicación
     * @param nameInput Campo de nombre de la parcela
     * @param maxOccupantsInput Campo de ocupantes máximos
     * @param priceInput Campo de precio por persona
     * @param errorMessage TextView para mostrar mensajes de error
     * @param viewModel ViewModel para verificar nombres duplicados
     * @param currentName Nombre actual de la parcela (null si es creación)
     * @param callback Callback para recibir el resultado de la validación
     */
    public static void validateInputsAsync(Context context, EditText nameInput, EditText maxOccupantsInput, EditText priceInput, TextView errorMessage, ParcelaViewModel viewModel, String currentName, ValidationCallback callback) {
        String name = nameInput.getText().toString().trim();
        
        if (name.isEmpty()) {
            showError(context, errorMessage, "El nombre no puede estar vacío");
            callback.onValidationResult(false);
            return;
        }

        // Run name check in background
        new Thread(() -> {
            boolean nameExists = !name.equals(currentName) && viewModel.exists(name);
            
            // Run UI updates on main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                if (nameExists) {
                    showError(context, errorMessage, "Ya existe una parcela con ese nombre");
                    nameInput.requestFocus();  // Focus on the name input
                    // Scroll to top if in a ScrollView
                    View rootView = nameInput.getRootView();
                    if (rootView != null) {
                        rootView.scrollTo(0, 0);
                    }
                    callback.onValidationResult(false);
                    return;
                }

                // Rest of validations
                try {
                    int occupants = Integer.parseInt(maxOccupantsInput.getText().toString().trim());
                    if (occupants <= 0) {
                        showError(context, errorMessage, "El número máximo de ocupantes debe ser mayor que cero.");
                        callback.onValidationResult(false);
                        return;
                    }
                } catch (NumberFormatException e) {
                    showError(context, errorMessage, "Por favor, ingrese un número válido para el máximo de ocupantes.");
                    callback.onValidationResult(false);
                    return;
                }

                try {
                    double price = Double.parseDouble(priceInput.getText().toString().trim());
                    if (price <= 0) {
                        showError(context, errorMessage, "El precio por persona debe ser mayor que cero.");
                        callback.onValidationResult(false);
                        return;
                    }
                } catch (NumberFormatException e) {
                    showError(context, errorMessage, "Por favor, ingrese un número válido para el precio por persona.");
                    callback.onValidationResult(false);
                    return;
                }

                errorMessage.setVisibility(View.GONE);
                callback.onValidationResult(true);
            });
        }).start();
    }

    private static void showError(Context context, TextView errorMessage, String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
        DialogUtils.showErrorDialog(context, message);
    }
}
