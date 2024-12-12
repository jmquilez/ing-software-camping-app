package es.unizar.eina.T213_camping.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.database.repositories.ParcelaRepository;
import es.unizar.eina.T213_camping.database.repositories.ReservaRepository;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;

public class TestUtils {
    private static final String TAG = "TestUtils";

    /**********************
     * PRUEBAS DE SISTEMA
     **********************/
    
    /**
     * Prueba de volumen: Verifica el funcionamiento con 100 parcelas y 10000 reservas
     */
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

    /**
     * Prueba de sobrecarga: Verifica el límite de caracteres en la descripción de parcelas
     */
    public static void stressTest(Application context, ParcelaViewModel parcelaViewModel) {
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

    /**
     * Limpieza de datos de prueba del sistema
     */
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

    /********************************
     * PRUEBAS UNITARIAS DE PARCELAS
     ********************************/

    /* Tests de Inserción */
    public static boolean testInsertarParcela1(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada1";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo insertar una parcela válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se insertó correctamente la parcela");
        return true;
    }

    public static boolean testInsertarParcela2(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        // No cleanup needed for null name
        
        // Test - nombre null
        Parcela parcela = new Parcela(
            null,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con nombre null");
        return true;
    }

    public static boolean testInsertarParcela3(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada3".repeat(60);
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - nombre muy largo
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con nombre muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con nombre muy largo");
        return true;
    }

    public static boolean testInsertarParcela4(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada4";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - nombre duplicado
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcela);
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con nombre duplicado");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con nombre duplicado");
        return true;
    }

    // TODO: move to valid class tests, modify valid classes and corresponding test in doc tables
    public static boolean testInsertarParcela5(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada5";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - descripción null
        Parcela parcela = new Parcela(
            nombreParcela,
            null,
            15,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con descripción null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con descripción null");
        return true;
    }

    public static boolean testInsertarParcela6(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada6";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - descripción muy larga
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal".repeat(300),
            15,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con descripción muy larga");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con descripción muy larga");
        return true;
    }

    public static boolean testInsertarParcela7(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada7";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad null
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            null,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad null");
        return true;
    }

    public static boolean testInsertarParcela8(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada8";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad negativa
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            -1,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad negativa");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad negativa");
        return true;
    }

    public static boolean testInsertarParcela9(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada9";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad muy grande
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            100000,
            60.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad muy grande");
        return true;
    }

    public static boolean testInsertarParcela10(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada10";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - precio null
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            null
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con precio null");
        return true;
    }

    public static boolean testInsertarParcela11(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada11";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - precio negativo
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            -1.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con precio negativo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con precio negativo");
        return true;
    }

    public static boolean testInsertarParcela12(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada12";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - precio muy grande
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            100000.0
        );
        long result = parcelaRepository.insert(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con precio muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con precio muy grande");
        return true;
    }

    /* Tests de Modificación */
    public static boolean testModificarParcela1(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada1M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - Caso válido
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcela);
        
        Parcela parcelaM = new Parcela(
            nombreParcela,
            "montaña",
            21,
            42.0
        );
        long result = parcelaRepository.update(parcelaM);
        
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo modificar una parcela válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se modificó correctamente la parcela");
        return true;
    }

    public static boolean testModificarParcela2(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada2M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - nombre null
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            null,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con nombre null");
        return true;
    }

    public static boolean testModificarParcela3(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreOriginal = "fuenlabrada3M";
        String nombreNuevo = nombreOriginal.repeat(60);
        // Clean up first
        parcelaRepository.deleteByNombre(nombreOriginal);
        parcelaRepository.deleteByNombre(nombreNuevo);
        
        // Test - nombre muy largo
        Parcela parcelaOrig = new Parcela(
            nombreOriginal,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreNuevo,
            "costal",
            12,
            24.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con nombre muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con nombre muy largo");
        return true;
    }

    public static boolean testModificarParcela4(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada4M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - parcela inexistente
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela inexistente");
        return true;
    }

    public static boolean testModificarParcela5(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada5M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - descripción null
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            null,
            15,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con descripción null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con descripción null");
        return true;
    }

    public static boolean testModificarParcela6(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada6M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - descripción muy larga
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal".repeat(300),
            15,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con descripción muy larga");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con descripción muy larga");
        return true;
    }

    public static boolean testModificarParcela7(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada7M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad null
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            null,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad null");
        return true;
    }

    public static boolean testModificarParcela8(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada8M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad negativa
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            -1,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad negativa");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad negativa");
        return true;
    }

    public static boolean testModificarParcela9(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada9M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - capacidad muy grande
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            100000,
            60.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad muy grande");
        return true;
    }

    public static boolean testModificarParcela10(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada10M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - precio null
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            null
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con precio null");
        return true;
    }

    public static boolean testModificarParcela11(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada11M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - Precio por persona negativo
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            -1.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con precio por persona negativo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con precio por persona negativo");
        return true;
    }

    public static boolean testModificarParcela12(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada12M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - Precio por persona muy grande
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            100000.0
        );
        long result = parcelaRepository.update(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con precio por persona demasiado grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con precio por persona demasiado grande");
        return true;
    }

    /* Tests de Eliminación */
    public static boolean testEliminarParcela1(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada1D";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcela);
        long result = parcelaRepository.delete(parcela);
        
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo eliminar una parcela válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se eliminó correctamente la parcela");
        return true;
    }

    public static boolean testEliminarParcela2(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada2D";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - nombre null
        Parcela parcelaOrig = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        parcelaRepository.insert(parcelaOrig);
        
        Parcela parcela = new Parcela(
            null,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.delete(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar parcela con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar parcela con nombre null");
        return true;
    }

    public static boolean testEliminarParcela3(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "hola";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);
        
        // Test - parcela inexistente
        Parcela parcela = new Parcela(
            nombreParcela,
            "costal",
            15,
            60.0
        );
        long result = parcelaRepository.delete(parcela);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una parcela inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una parcela inexistente");
        return true;
    }

    /*********************************
     * PRUEBAS UNITARIAS DE RESERVAS
     *********************************/

    /* Tests de Inserción */
    public static boolean testInsertarReserva1(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 1L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Caso válido
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L), // 17/10/2024
            new Date(1729612800000L), // 20/10/2024
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo insertar una reserva válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se insertó correctamente la reserva");
        return true;
    }

    public static boolean testInsertarReserva2(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        // No cleanup needed for null ID
        
        // Test - ID null
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(null);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con ID null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con ID null");
        return true;
    }

    public static boolean testInsertarReserva3(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 1002L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - ID muy grande
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con ID muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con ID muy grande");
        return true;
    }

    public static boolean testInsertarReserva4(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 0L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - ID cero
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con ID cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con ID cero");
        return true;
    }

    public static boolean testInsertarReserva5(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 5L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - ID duplicado
        Reserva reserva1 = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva1.setId(reservaId);
        reservaRepository.insert(reserva1);
        
        Reserva reserva2 = new Reserva(
            "Juan",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "987654321",
            50.0
        );
        reserva2.setId(reservaId);
        long result = reservaRepository.insert(reserva2);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con ID duplicado");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con ID duplicado");
        return true;
    }

    public static boolean testInsertarReserva6(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 6L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Nombre cliente null
        Reserva reserva = new Reserva(
            null,
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con nombre cliente null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con nombre cliente null");
        return true;
    }

    public static boolean testInsertarReserva7(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 7L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Nombre cliente muy largo
        Reserva reserva = new Reserva(
            "Pedro".repeat(40),
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con nombre cliente muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con nombre cliente muy largo");
        return true;
    }

    public static boolean testInsertarReserva8(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 8L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Fecha entrada null
        Reserva reserva = new Reserva(
            "Pedro",
            null,
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha entrada null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha entrada null");
        return true;
    }

    public static boolean testInsertarReserva9(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 9L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Fecha salida anterior a entrada
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729612800000L), // 20/10/2024
            new Date(1729353600000L), // 17/10/2024
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha salida anterior a entrada");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha salida anterior a entrada");
        return true;
    }

    public static boolean testInsertarReserva10(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 10L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Fecha salida null
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            null,
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha salida null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha salida null");
        return true;
    }

    public static boolean testInsertarReserva11(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 11L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Teléfono null
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            null,
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con teléfono null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con teléfono null");
        return true;
    }

    public static boolean testInsertarReserva12(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 12L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Teléfono inválido
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "a23456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con teléfono inválido");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con teléfono inválido");
        return true;
    }

    public static boolean testInsertarReserva13(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 13L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Precio null
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            null
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con precio null");
        return true;
    }

    public static boolean testInsertarReserva14(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 14L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Precio cero
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            0.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se permitió crear reserva con precio cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se permitió crear reserva con precio cero");
        return true;
    }

    public static boolean testInsertarReserva15(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 15L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Precio muy grande
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            200000.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con precio muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con precio muy grande");
        return true;
    }

    /* Tests de Eliminación */
    public static boolean testEliminarReserva1(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 1L;
        // Clean up first
        reservaRepository.deleteById(reservaId);
        
        // Test - Caso válido
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);
        
        long result = reservaRepository.delete(reserva);
        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo eliminar una reserva válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se eliminó correctamente la reserva");
        return true;
    }

    public static boolean testEliminarReserva2(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        // Test - ID null
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(null);
        
        long result = reservaRepository.delete(reserva);
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar reserva con ID null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar reserva con ID null");
        return true;
    }

    public static boolean testEliminarReserva3(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 999L;
        
        // Test - Reserva inexistente
        Reserva reserva = new Reserva(
            "Pedro",
            new Date(1729353600000L),
            new Date(1729612800000L),
            "123456789",
            50.0
        );
        reserva.setId(reservaId);
        long result = reservaRepository.delete(reserva);
        
        if (result > 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una reserva inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una reserva inexistente");
        return true;
    }
}
