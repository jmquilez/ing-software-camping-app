package es.unizar.eina.T213_camping.utils.TestUtils;

import android.util.Log;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;

public class Common {
    private static final String TAG = "TestUtils.Common";

    /**
     * Limpieza de datos de prueba del sistema
     */
    public static void cleanTestData(ParcelaViewModel parcelaViewModel, ReservaViewModel reservaViewModel) {
        try {
            // Borrar parcelas de prueba
            if (parcelaViewModel != null) {
                parcelaViewModel.getAllParcelas().observeForever(parcelas -> {
                    for (Parcela parcela : parcelas) {
                        if (parcela.getNombre().startsWith("TEST_") ||
                                parcela.getNombre().startsWith("STRESS_")) {
                            parcelaViewModel.delete(parcela);
                        }
                    }
                });
            }

            // Borrar reservas de prueba
            if (reservaViewModel != null) {
                reservaViewModel.getAllReservas().observeForever(reservas -> {
                    for (Reserva reserva : reservas) {
                        if (reserva.getNombreCliente().startsWith("TEST_")) {
                            reservaViewModel.delete(reserva);
                        }
                    }
                });
            }

            Log.d(TAG, "Datos de prueba eliminados");
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar datos de prueba: " + e.getMessage());
        }
    }
}
