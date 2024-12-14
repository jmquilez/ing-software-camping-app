package es.unizar.eina.T213_camping.utils.TestUtils;

import static es.unizar.eina.T213_camping.utils.TestUtils.Common.cleanTestData;

import android.app.Application;
import android.util.Log;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;

public class StressTests {
    private static final String TAG = "StressTests";
    /**
     * Prueba de sobrecarga: Verifica el límite de caracteres en la descripción de
     * parcelas
     */
    public static boolean stressTest(Application context, ParcelaViewModel parcelaViewModel) {
        try {
            cleanTestData(parcelaViewModel, null);
            final int MAX_DESCRIPCION_LENGTH = 300;
            String nombreParcela = "STRESS_TEST_PARCELA";

            // Clean up first
            parcelaViewModel.deleteByNombre(nombreParcela);

            // Test with valid description (exactly MAX_DESCRIPCION_LENGTH)
            StringBuilder validDescription = new StringBuilder();
            validDescription.append("a".repeat(MAX_DESCRIPCION_LENGTH));

            Parcela validParcela = new Parcela(
                    nombreParcela,
                    validDescription.toString(),
                    4,
                    20.0);

            long validResult = parcelaViewModel.insert(validParcela);
            if (validResult == -1) {
                Log.e(TAG, "Test fallido: No se pudo insertar parcela con descripción de longitud máxima válida");
                return false;
            }

            // Clean up the valid test
            parcelaViewModel.deleteByNombre(nombreParcela);

            // Test with invalid description (MAX_DESCRIPCION_LENGTH + 1)
            StringBuilder invalidDescription = new StringBuilder();
            invalidDescription.append("a".repeat(MAX_DESCRIPCION_LENGTH + 1));

            Parcela invalidParcela = new Parcela(
                    nombreParcela,
                    invalidDescription.toString(),
                    4,
                    20.0);

            long invalidResult = parcelaViewModel.insert(invalidParcela);
            if (invalidResult != -1) {
                Log.e(TAG, "Test fallido: Se permitió insertar parcela con descripción demasiado larga");
                return false;
            }

            Log.d(TAG, "Test de sobrecarga completado exitosamente");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error en prueba de sobrecarga: " + e.getMessage());
            return false;
        }
    }
}
