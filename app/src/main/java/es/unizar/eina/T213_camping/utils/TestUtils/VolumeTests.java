package es.unizar.eina.T213_camping.utils.TestUtils;

import static es.unizar.eina.T213_camping.utils.TestUtils.Common.cleanTestData;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.utils.DialogUtils;

public class VolumeTests {

    private static final String TAG = "VolumeTests";

    /**
     * Prueba de volumen: Verifica el funcionamiento con 100 parcelas y 10000
     * reservas
     */
    public static boolean volumeTest(Context context, ParcelaViewModel parcelaViewModel,
                                     ReservaViewModel reservaViewModel) {
        try {
            cleanTestData(parcelaViewModel, reservaViewModel);
            final int MAX_PARCELAS = 100;
            final int MAX_RESERVAS = 10000;

            // Get current counts and calculate remaining
            int currentParcelasCount = parcelaViewModel.getParcelasCount();
            int currentReservasCount = reservaViewModel.getReservasCount();
            int parcelasToAdd = MAX_PARCELAS - currentParcelasCount;
            int reservasToAdd = MAX_RESERVAS - currentReservasCount;

            // Crear parcelas hasta el límite
            for (int i = 1; i <= parcelasToAdd; i++) {
                Parcela parcela = new Parcela(
                        "TEST_PARCELA_" + i,
                        "Parcela de prueba " + i,
                        4,
                        20.0);
                long result = parcelaViewModel.insert(parcela);
                if (result == -1) {
                    Log.e(TAG, "Error al insertar parcela " + i);
                    return false;
                }
                Log.d(TAG, "Creada parcela " + i + " de " + parcelasToAdd);
            }

            // Crear reservas hasta el límite
            Calendar cal = Calendar.getInstance();
            for (int i = 1; i <= reservasToAdd; i++) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Date startDate = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, 3);
                Date endDate = cal.getTime();

                Reserva reserva = new Reserva(
                        "TEST_CLIENTE_" + i,
                        startDate,
                        endDate,
                        "123456789",
                        50.0);
                long result = reservaViewModel.insert(reserva);
                if (result == -1) {
                    Log.e(TAG, "Error al insertar reserva " + i);
                    return false;
                }
                Log.d(TAG, "Creada reserva " + i + " de " + reservasToAdd);
            }

            // Intentar insertar parcela 1001 (debería fallar)
            Parcela parcelaExtra = new Parcela(
                    "TEST_PARCELA_1001",
                    "Parcela de prueba 1001",
                    4,
                    20.0);
            if (parcelaViewModel.insert(parcelaExtra) != -1) {
                Log.e(TAG, "Error: Se permitió insertar más de 1000 parcelas");
                return false;
            }

            // Intentar insertar reserva 10001 (debería fallar)
            cal.add(Calendar.DAY_OF_YEAR, 1);
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, 3);
            Date endDate = cal.getTime();

            Reserva reservaExtra = new Reserva(
                    "TEST_CLIENTE_10001",
                    startDate,
                    endDate,
                    "123456789",
                    50.0);
            reservaExtra.setId(10001L);
            if (reservaViewModel.insert(reservaExtra) != -1) {
                Log.e(TAG, "Error: Se permitió insertar más de 10000 reservas");
                return false;
            }

            return true;

        } catch (Exception e) {
            DialogUtils.showErrorDialog(context, "Error en prueba de volumen: " + e.getMessage());
            return false;
        }
    }
}
