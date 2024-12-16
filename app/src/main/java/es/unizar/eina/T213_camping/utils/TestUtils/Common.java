package es.unizar.eina.T213_camping.utils.TestUtils;

import android.util.Log;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;

/**
 * Clase de utilidades para la limpieza de datos de prueba.
 * Proporciona métodos para eliminar datos de prueba del sistema,
 * incluyendo parcelas y reservas con prefijos específicos.
 */
public class Common {
    
    private static final String TAG = "TestUtils.Common";

    /**
     * Limpieza de datos de prueba del sistema
     * @param parcelaViewModel ViewModel para gestionar las parcelas
     * @param reservaViewModel ViewModel para gestionar las reservas
     */
    public static void cleanTestData(ParcelaViewModel parcelaViewModel, ReservaViewModel reservaViewModel) {
        try {
            // Borrar parcelas de prueba
            if (parcelaViewModel != null) {
                parcelaViewModel.deleteParcelasWithPrefix("TEST_");
                parcelaViewModel.deleteParcelasWithPrefix("STRESS_");
            }

            // Borrar reservas de prueba
            if (reservaViewModel != null) {
                reservaViewModel.deleteReservasWithClientNamePrefix("TEST_");
            }

            Log.d(TAG, "Datos de prueba eliminados");
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar datos de prueba: " + e.getMessage());
        }
    }
}
