package es.unizar.eina.T213_camping.utils;

import android.content.Context;
import android.util.Log;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;

public class TestUtils {
    private static final String TAG = "TestUtils";

    // Prueba de volumen
    public static void volumeTest(Context context, ParcelaViewModel parcelaViewModel, ReservaViewModel reservaViewModel) {
        // Test con límites válidos: 100 parcelas y 10000 reservas
        try {
            // Crear 100 parcelas
            for (int i = 1; i <= 100; i++) {
                Parcela parcela = new Parcela(
                    "TEST_PARCELA_" + i,
                    "Parcela de prueba " + i,
                    4,
                    20.0
                );
                parcelaViewModel.insert(parcela);
                Log.d(TAG, "Creada parcela " + i);
            }

            // Crear 10000 reservas
            Calendar cal = Calendar.getInstance();
            for (int i = 1; i <= 10000; i++) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Date entryDate = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, 7);
                Date departureDate = cal.getTime();

                Reserva reserva = new Reserva(
                    "TEST_CLIENTE_" + i,
                    entryDate,
                    departureDate,
                    "600" + String.format("%07d", i),
                    140.0
                );
                reservaViewModel.insert(reserva);
                Log.d(TAG, "Creada reserva " + i);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en prueba de volumen: " + e.getMessage());
        }
    }

    // Prueba de sobrecarga
    public static void stressTest(Context context, ParcelaViewModel parcelaViewModel) {
        final int STEP_SIZE = 10000; // Incremento de caracteres en cada iteración
        final StringBuilder description = new StringBuilder();
        final AtomicInteger currentSize = new AtomicInteger(0);

        try {
            while (true) {
                // Añadir STEP_SIZE caracteres de manera eficiente
                char[] chars = new char[STEP_SIZE];
                Arrays.fill(chars, 'a');
                description.append(chars);
                currentSize.addAndGet(STEP_SIZE);

                Parcela parcela = new Parcela(
                    "STRESS_TEST_" + currentSize.get(),
                    description.toString(),
                    4,
                    20.0
                );

                try {
                    // Insertar la parcela
                    parcelaViewModel.insert(parcela);
                    Log.d(TAG, "Insertada parcela con descripción de " + currentSize.get() + " caracteres");
                    
                    // Borrar la parcela después de la inserción exitosa
                    parcelaViewModel.delete(parcela);
                    Log.d(TAG, "Borrada parcela de prueba con descripción de " + currentSize.get() + " caracteres");
                    
                } catch (Exception e) {
                    Log.e(TAG, "Fallo al insertar parcela con " + currentSize.get() + " caracteres");
                    Log.e(TAG, "Mensaje de error: " + e.getMessage());
                    Log.e(TAG, "Longitud máxima soportada: " + (currentSize.get() - STEP_SIZE) + " caracteres");
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en prueba de sobrecarga: " + e.getMessage());
        }
    }

    // Limpieza de datos de prueba
    public static void cleanTestData(ParcelaViewModel parcelaViewModel, ReservaViewModel reservaViewModel) {
        try {
            // Borrar parcelas de prueba
            parcelaViewModel.getAllParcelas().observeForever(parcelas -> {
                for (Parcela parcela : parcelas) {
                    if (parcela.getNombre().startsWith("TEST_") || 
                        parcela.getNombre().startsWith("STRESS_")) {
                        parcelaViewModel.delete(parcela);
                    }
                }
            });

            // Borrar reservas de prueba
            reservaViewModel.getAllReservas().observeForever(reservas -> {
                for (Reserva reserva : reservas) {
                    if (reserva.getNombreCliente().startsWith("TEST_")) {
                        reservaViewModel.delete(reserva);
                    }
                }
            });

            Log.d(TAG, "Datos de prueba eliminados");
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar datos de prueba: " + e.getMessage());
        }
    }
} 