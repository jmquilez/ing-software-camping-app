package es.unizar.eina.T213_camping.utils.TestUtils;

import android.app.Application;
import android.util.Log;

import java.util.Date;

import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaReservada;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.database.repositories.ParcelaRepository;
import es.unizar.eina.T213_camping.database.repositories.ParcelaReservadaRepository;
import es.unizar.eina.T213_camping.database.repositories.ReservaRepository;

public class UnitTests {
    private static final String TAG = "UnitTests";

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
                60.0);
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
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
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
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con nombre muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con nombre muy largo");
        return true;
    }

    public static boolean testInsertarParcela4(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);

        // Test - nombre cadena vacía
        Parcela parcela = new Parcela(
                "",
                "costal",
                15,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear una parcela con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear una parcela con nombre null");
        return true;
    }

    public static boolean testInsertarParcela5(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada5";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - nombre duplicado
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcela);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con nombre duplicado");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con nombre duplicado");
        return true;
    }

    // TODO: move to valid class tests, modify valid classes and corresponding test
    // in doc tables
    public static boolean testInsertarParcela6(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada6";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - descripción null
        Parcela parcela = new Parcela(
                nombreParcela,
                null,
                15,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con descripción null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con descripción null");
        return true;
    }

    public static boolean testInsertarParcela7(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada7";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - descripción muy larga
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal".repeat(300),
                15,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con descripción muy larga");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con descripción muy larga");
        return true;
    }

    public static boolean testInsertarParcela8(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada8";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad null
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                null,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad null");
        return true;
    }

    public static boolean testInsertarParcela9(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada9";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad negativa
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                -1,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad negativa");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad negativa");
        return true;
    }

    public static boolean testInsertarParcela10(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada10";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad muy grande
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                100000,
                60.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con capacidad muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con capacidad muy grande");
        return true;
    }

    public static boolean testInsertarParcela11(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada11";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - precio null
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                null);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con precio null");
        return true;
    }

    public static boolean testInsertarParcela12(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada12";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - precio <= 0
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                0.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear parcela con precio por persona igual a 0");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear parcela con precio por persona igual a 0");
        return true;
    }

    public static boolean testInsertarParcela13(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada13";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - precio muy grande
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                100000.0);
        long result = parcelaRepository.insert(parcela);

        if (result != -1) {
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
                60.0);
        parcelaRepository.insert(parcela);

        Parcela parcelaM = new Parcela(
                nombreParcela,
                "montaña",
                21,
                42.0);
        long result = parcelaRepository.update(parcelaM);

        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo modificar una parcela válida y existente");
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
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                null,
                "costal",
                15,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != 0) {
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
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreNuevo,
                "costal",
                12,
                24.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con nombre muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con nombre muy largo");
        return true;
    }

    public static boolean testModificarParcela4(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreOriginal = "fuenlabrada4M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreOriginal);

        // Test - parcela con nombre null
        Parcela parcelaOrig = new Parcela(
                nombreOriginal,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                "",
                "costal",
                15,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela con nombre null");
        return true;
    }

    public static boolean testModificarParcela5(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada5M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - parcela inexistente
        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela inexistente");
        return true;
    }

    public static boolean testModificarParcela6(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada6M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - descripción null
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                null,
                15,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con descripción null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con descripción null");
        return true;
    }

    public static boolean testModificarParcela7(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada7M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - descripción muy larga
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal".repeat(300),
                15,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con descripción muy larga");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con descripción muy larga");
        return true;
    }

    public static boolean testModificarParcela8(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada8M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad null
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                null,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad null");
        return true;
    }

    public static boolean testModificarParcela9(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada9M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad negativa
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                -1,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad negativa");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad negativa");
        return true;
    }

    public static boolean testModificarParcela10(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada10M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - capacidad muy grande
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                100000,
                60.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con capacidad muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con capacidad muy grande");
        return true;
    }

    public static boolean testModificarParcela11(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada11M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - precio null
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                null);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con precio null");
        return true;
    }

    public static boolean testModificarParcela12(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada12M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - Precio por persona <= 0
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                0.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar parcela con precio por persona igual a 0");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar parcela con precio por persona igual a 0");
        return true;
    }

    public static boolean testModificarParcela13(Application context) {
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        String nombreParcela = "fuenlabrada13M";
        // Clean up first
        parcelaRepository.deleteByNombre(nombreParcela);

        // Test - Precio por persona muy grande
        Parcela parcelaOrig = new Parcela(
                nombreParcela,
                "costal",
                15,
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                nombreParcela,
                "costal",
                15,
                100000.0);
        long result = parcelaRepository.update(parcela);

        if (result != -1) {
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
                60.0);
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
                60.0);
        parcelaRepository.insert(parcelaOrig);

        Parcela parcela = new Parcela(
                null,
                "costal",
                15,
                60.0);
        long result = parcelaRepository.delete(parcela);

        if (result != 0) {
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
                60.0);
        long result = parcelaRepository.delete(parcela);

        if (result != 0) {
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
                50.0);
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
                50.0);
        reserva.setId(null);
        long result = reservaRepository.insert(reserva);

        // Dado que el id de Reserva se autogenera por cada inserción, si se intenta
        // insertar una reserva con id null, no se lanzará ninguna excepción (y se
        // insertará una reserva con un id válido != null)
        /*if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con ID null");
            return false;
        }*/
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con ID null");
        return true;
    }

    public static boolean testInsertarReserva3(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 10001L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - ID muy grande
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
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
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
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
                50.0);
        reserva1.setId(reservaId);
        reservaRepository.insert(reserva1);

        Reserva reserva2 = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                50.0);
        reserva2.setId(reservaId);
        long result = reservaRepository.insert(reserva2);

        if (result != -1) {
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
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
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

        // Test - Nombre cliente == cadena vacía
        Reserva reserva = new Reserva(
                "",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con cadena vacía como nombre de cliente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con cadena vacía como nombre de cliente");
        return true;
    }

    public static boolean testInsertarReserva8(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 8L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Nombre cliente muy largo
        Reserva reserva = new Reserva(
                "Pedro".repeat(40),
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con nombre cliente muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con nombre cliente muy largo");
        return true;
    }

    public static boolean testInsertarReserva9(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 9L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Fecha entrada null
        Reserva reserva = new Reserva(
                "Pedro",
                null,
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha entrada null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha entrada null");
        return true;
    }

    public static boolean testInsertarReserva10(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 10L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Fecha salida anterior a entrada
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729612800000L), // 20/10/2024
                new Date(1729353600000L), // 17/10/2024
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha salida anterior a entrada");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha salida anterior a entrada");
        return true;
    }

    public static boolean testInsertarReserva11(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 11L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Fecha salida null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                null,
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con fecha salida null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con fecha salida null");
        return true;
    }

    public static boolean testInsertarReserva12(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 12L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Teléfono null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                null,
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con teléfono null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con teléfono null");
        return true;
    }

    public static boolean testInsertarReserva13(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 13L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Teléfono inválido
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "a23456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con teléfono inválido");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con teléfono inválido");
        return true;
    }

    public static boolean testInsertarReserva14(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 14L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Precio null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                null);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con precio null");
        return true;
    }

    public static boolean testInsertarReserva15(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 15L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Precio cero
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                0.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: S permitió crear reserva con precio cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se permitió crear reserva con precio cero");
        return true;
    }

    public static boolean testInsertarReserva16(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 16L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Test - Precio muy grande
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                200000.0);
        reserva.setId(reservaId);
        long result = reservaRepository.insert(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió crear reserva con precio muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió crear reserva con precio muy grande");
        return true;
    }

    /* Tests de Modificación */
    public static boolean testModificarReserva1(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 1L;
        // Clean up first
        reservaRepository.deleteById(reservaId);

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Caso válido
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L), // 17/10/2024
                new Date(1729612800000L), // 20/10/2024
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo modificar una reserva válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se modificó correctamente la reserva");
        return true;
    }

    public static boolean testModificarReserva2(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 2L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - ID null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(null);
        long result = reservaRepository.update(reserva);

        // Dado que el id de Reserva se autogenera por cada inserción, si se intenta
        // insertar una reserva con id null, no se lanzará ninguna excepción (y se
        // insertará una reserva con un id válido != null)
        /*if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con ID null");
            return false;
        }*/
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con ID null");
        return true;
    }

    public static boolean testModificarReserva3(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 3L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - ID muy grande
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(10001L);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con ID muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con ID muy grande");
        return true;
    }

    public static boolean testModificarReserva4(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 4L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - ID cero
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(0L);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con ID cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con ID cero");
        return true;
    }

    public static boolean testModificarReserva5(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 5L;
        // Clean up first to ensure it doesn't exist
        reservaRepository.deleteById(reservaId);

        // Test - Modificar reserva inexistente
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva inexistente");
        return true;
    }

    public static boolean testModificarReserva6(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 6L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Nombre cliente null
        Reserva reserva = new Reserva(
                null,
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con nombre cliente null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con nombre cliente null");
        return true;
    }

    public static boolean testModificarReserva7(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 7L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Nombre cliente == cadena vacía
        Reserva reserva = new Reserva(
                "",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con cadena vacía como nombre de cliente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con cadena vacía como nombre de cliente");
        return true;
    }

    public static boolean testModificarReserva8(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 8L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Nombre cliente muy largo
        Reserva reserva = new Reserva(
                "Pedro".repeat(40),
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con nombre cliente muy largo");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con nombre cliente muy largo");
        return true;
    }

    public static boolean testModificarReserva9(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 9L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Fecha entrada null
        Reserva reserva = new Reserva(
                "Pedro",
                null,
                new Date(1729612800000L),
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con fecha entrada null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con fecha entrada null");
        return true;
    }

    public static boolean testModificarReserva10(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 10L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Fecha salida anterior a entrada
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729612800000L), // 20/10/2024
                new Date(1729353600000L), // 17/10/2024
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con fecha salida anterior a entrada");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con fecha salida anterior a entrada");
        return true;
    }

    public static boolean testModificarReserva11(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 11L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Fecha salida null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                null,
                "123456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con fecha salida null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con fecha salida null");
        return true;
    }

    public static boolean testModificarReserva12(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 12L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Teléfono null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                null,
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con teléfono null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con teléfono null");
        return true;
    }

    public static boolean testModificarReserva13(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 13L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Teléfono inválido
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "a23456789",
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con teléfono inválido");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con teléfono inválido");
        return true;
    }

    public static boolean testModificarReserva14(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 14L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Precio null
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                null);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con precio null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con precio null");
        return true;
    }

    public static boolean testModificarReserva15(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 15L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Precio cero
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                0.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con precio cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con precio cero");
        return true;
    }

    public static boolean testModificarReserva16(Application context) {
        ReservaRepository reservaRepository = new ReservaRepository(context);
        long reservaId = 16L;

        // Insert valid reserva first
        Reserva reservaOrig = new Reserva(
                "Juan",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "987654321",
                40.0);
        reservaOrig.setId(reservaId);
        reservaRepository.insert(reservaOrig);

        // Test - Precio muy grande
        Reserva reserva = new Reserva(
                "Pedro",
                new Date(1729353600000L),
                new Date(1729612800000L),
                "123456789",
                200000.0);
        reserva.setId(reservaId);
        long result = reservaRepository.update(reserva);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar reserva con precio muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar reserva con precio muy grande");
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
                50.0);
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
                50.0);
        reserva.setId(null);

        long result = reservaRepository.delete(reserva);
        if (result != 0) {
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
                50.0);
        reserva.setId(reservaId);
        long result = reservaRepository.delete(reserva);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una reserva inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una reserva inexistente");
        return true;
    }

    /**************************************************************************
     * *
     * PRUEBAS UNITARIAS DE PARCELAS RESERVADAS *
     * *
     **************************************************************************/

    /*
     * ========================================================================
     * Tests de Inserción - Casos Válidos
     * ========================================================================
     */
    public static boolean testInsertarParcelaReservada1(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);
        reservaRepository.deleteById(reservaId);

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo insertar una parcela reservada válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se insertó correctamente la parcela reservada");
        return true;
    }

    /*
     * ========================================================================
     * Tests de Inserción - Casos Inválidos
     * ========================================================================
     */
    public static boolean testInsertarParcelaReservada2(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);
        String nombreParcela = "moncayo";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        reservaRepository.deleteById(reservaId);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - parcela inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada con parcela inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada con parcela inexistente");
        return true;
    }

    public static boolean testInsertarParcelaReservada3(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 120L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);

        // Preparar parcela válida
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        // Test - reserva inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada con reserva inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada con reserva inexistente");
        return true;
    }

    public static boolean testInsertarParcelaReservada4(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);
        reservaRepository.deleteById(reservaId);

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Insertar primera vez
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservada);

        // Test - insertar duplicado
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada duplicada");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada duplicada");
        return true;
    }

    public static boolean testInsertarParcelaReservada5(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);
        reservaRepository.deleteById(reservaId);

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - número de ocupantes null
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, null);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada con número de ocupantes null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada con número de ocupantes null");
        return true;
    }

    public static boolean testInsertarParcelaReservada6(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);
        reservaRepository.deleteById(reservaId);

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - número de ocupantes cero
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 0);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada con número de ocupantes cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada con número de ocupantes cero");
        return true;
    }

    public static boolean testInsertarParcelaReservada7(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Clean up first
        ParcelaReservada cleanupParcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.delete(cleanupParcelaReservada);
        parcelaRepository.deleteByNombre(nombreParcela);
        reservaRepository.deleteById(reservaId);

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - número de ocupantes muy grande
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 1000);
        long result = parcelaReservadaRepository.insert(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió insertar una parcela reservada con número de ocupantes muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió insertar una parcela reservada con número de ocupantes muy grande");
        return true;
    }

    /*
     * ========================================================================
     * Tests de Modificación - Casos Válidos
     * ========================================================================
     */
    public static boolean testModificarParcelaReservada1(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1M";
        long reservaId = 1L;

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservada);

        // Test
        parcelaReservada.setNumOcupantes(12);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo modificar una parcela reservada válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se modificó correctamente la parcela reservada");
        return true;
    }

    /*
     * ========================================================================
     * Tests de Modificación - Casos Inválidos
     * ========================================================================
     */
    public static boolean testModificarParcelaReservada2(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "moncayo";
        long reservaId = 1L;

        // Preparar reserva válida
        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - parcela inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada con parcela inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada con parcela inexistente");
        return true;
    }

    public static boolean testModificarParcelaReservada3(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 120L;

        // Preparar parcela válida
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        // Test - reserva inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada con reserva inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada con reserva inexistente");
        return true;
    }

    public static boolean testModificarParcelaReservada4(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        // Test - modificar parcela reservada inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada inexistente");
        return true;
    }

    public static boolean testModificarParcelaReservada5(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Preparar datos de prueba y crear parcela reservada válida
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        ParcelaReservada parcelaReservadaOrig = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservadaOrig);

        // Test - número de ocupantes null
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, null);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada con número de ocupantes null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada con número de ocupantes null");
        return true;
    }

    public static boolean testModificarParcelaReservada6(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Preparar datos de prueba y crear parcela reservada válida
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        ParcelaReservada parcelaReservadaOrig = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservadaOrig);

        // Test - número de ocupantes cero
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 0);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada con número de ocupantes cero");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada con número de ocupantes cero");
        return true;
    }

    public static boolean testModificarParcelaReservada7(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Preparar datos de prueba y crear parcela reservada válida
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        ParcelaReservada parcelaReservadaOrig = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservadaOrig);

        // Test - número de ocupantes muy grande
        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 1000);
        long result = parcelaReservadaRepository.update(parcelaReservada);

        if (result != -1) {
            Log.e(TAG, "Test fallido: Se permitió modificar una parcela reservada con número de ocupantes muy grande");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió modificar una parcela reservada con número de ocupantes muy grande");
        return true;
    }

    /*
     * ========================================================================
     * Tests de Eliminación - Casos Válidos
     * ========================================================================
     */
    public static boolean testEliminarParcelaReservada1(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);
        ParcelaRepository parcelaRepository = new ParcelaRepository(context);
        ReservaRepository reservaRepository = new ReservaRepository(context);

        String nombreParcela = "fuenlabrada1";
        long reservaId = 1L;

        // Preparar datos de prueba
        Parcela parcela = new Parcela(nombreParcela, "descripcion", 15, 60.0);
        parcelaRepository.insert(parcela);

        Date fechaEntrada = new Date();
        Date fechaSalida = new Date(fechaEntrada.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva("Cliente", fechaEntrada, fechaSalida, "123456789", 50.0);
        reserva.setId(reservaId);
        reservaRepository.insert(reserva);

        ParcelaReservada parcelaReservada = new ParcelaReservada(nombreParcela, reservaId, 10);
        parcelaReservadaRepository.insert(parcelaReservada);

        // Test
        long result = parcelaReservadaRepository.delete(parcelaReservada);

        if (result <= 0) {
            Log.e(TAG, "Test fallido: No se pudo eliminar una parcela reservada válida");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se eliminó correctamente la parcela reservada");
        return true;
    }

    /*
     * ========================================================================
     * Tests de Eliminación - Casos Inválidos
     * ========================================================================
     */
    public static boolean testEliminarParcelaReservada2(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);

        // Test - nombre parcela null
        ParcelaReservada parcelaReservada = new ParcelaReservada(null, 1L, 10);
        long result = parcelaReservadaRepository.delete(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una parcela reservada con nombre null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una parcela reservada con nombre null");
        return true;
    }

    public static boolean testEliminarParcelaReservada3(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);

        // Test - ID reserva null
        ParcelaReservada parcelaReservada = new ParcelaReservada("fuenlabrada1", null, 10);
        long result = parcelaReservadaRepository.delete(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una parcela reservada con ID de reserva null");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una parcela reservada con ID de reserva null");
        return true;
    }

    public static boolean testEliminarParcelaReservada4(Application context) {
        ParcelaReservadaRepository parcelaReservadaRepository = new ParcelaReservadaRepository(context);

        // Test - parcela reservada inexistente
        ParcelaReservada parcelaReservada = new ParcelaReservada("moncayo", 120L, 10);
        long result = parcelaReservadaRepository.delete(parcelaReservada);

        if (result != 0) {
            Log.e(TAG, "Test fallido: Se permitió eliminar una parcela reservada inexistente");
            return false;
        }
        Log.d(TAG, "Test exitoso: Se impidió eliminar una parcela reservada inexistente");
        return true;
    }
}
