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
     * parcelas de forma incremental
     * 
     * @param context          Contexto de la aplicación
     * @param parcelaViewModel ViewModel para gestionar las parcelas
     * @return boolean indicando si la prueba fue exitosa
     */
    public static boolean stressTest(Application context, ParcelaViewModel parcelaViewModel) {
        try {
            cleanTestData(parcelaViewModel, null);
            final int MAX_DESCRIPCION_LENGTH = 300;
            final String BASE_NOMBRE = "STRESS_TEST_PARCELA_";

            // Check if there's space for at least one parcela
            int currentCount = parcelaViewModel.getParcelasCount();
            if (currentCount >= 100) { // Using MAX_PARCELAS constant from ParcelaRepository
                Log.e(TAG, "Test fallido: No hay espacio para insertar parcelas de prueba");
                return false;
            }

            String lastNombreParcela = null;

            // Test with incrementing description lengths
            for (int length = 0; length <= MAX_DESCRIPCION_LENGTH + 10; length++) {
                // Delete previous test parcela if it exists
                if (lastNombreParcela != null) {
                    parcelaViewModel.deleteByNombre(lastNombreParcela);
                }

                String nombreParcela = BASE_NOMBRE + length;
                lastNombreParcela = nombreParcela;
                String description = "a".repeat(length);

                Parcela parcela = new Parcela(
                        nombreParcela,
                        description,
                        4,
                        20.0);

                long result = parcelaViewModel.insert(parcela);

                // Check if the result matches expectations
                if (length <= MAX_DESCRIPCION_LENGTH) {
                    if (result == -1) {
                        Log.e(TAG, "Test fallido: No se pudo insertar parcela con descripción válida de longitud "
                                + length);
                        return false;
                    }
                    Log.d(TAG, "Insertada correctamente parcela con descripción de longitud " + length);
                } else {
                    if (result != -1) {
                        Log.e(TAG, "Test fallido: Se permitió insertar parcela con descripción inválida de longitud "
                                + length);
                        return false;
                    }
                    Log.d(TAG, "Rechazada correctamente parcela con descripción de longitud " + length);
                }
            }

            Log.d(TAG, "Test de sobrecarga incremental completado exitosamente");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error en prueba de sobrecarga: " + e.getMessage());
            return false;
        } finally {
            cleanTestData(parcelaViewModel, null);
        }
    }
}
