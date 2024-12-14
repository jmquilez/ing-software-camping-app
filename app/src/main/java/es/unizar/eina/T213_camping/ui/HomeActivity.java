package es.unizar.eina.T213_camping.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.parcelas.listado.ParcelFeedActivity;
import es.unizar.eina.T213_camping.ui.reservas.listado.ReservationFeedActivity;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.utils.TestUtils;

/**
 * Actividad principal que sirve como punto de entrada a las principales funcionalidades.
 * Proporciona botones para acceder a la gestión de parcelas y reservas.
 */
public class HomeActivity extends BaseActivity {

    private ParcelaViewModel parcelaViewModel;
    private ReservaViewModel reservaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewModels();
        setupNavigationButtons();
        setupTestButton();
        setButtonVisibility("back", false);
        setButtonVisibility("home", false);
    }

    private void setupViewModels() {
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);
        reservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
    }

    /**
     * Configura los botones de navegación a las principales funcionalidades.
     * - Gestión de parcelas
     * - Gestión de reservas
     */
    private void setupNavigationButtons() {
        Button parcelasButton = findViewById(R.id.parcelas_button);
        Button reservasButton = findViewById(R.id.reservas_button);

        parcelasButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ParcelFeedActivity.class);
            startActivity(intent);
        });

        reservasButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReservationFeedActivity.class);
            startActivity(intent);
        });
    }

    private void setupTestButton() {
        MaterialButton testButton = findViewById(R.id.test_button);
        
        testButton.setOnLongClickListener(v -> {
            showTestOptions();
            return true;
        });
    }

    private void showTestOptions() {
        String[] options = new String[]{
            "Pruebas Unitarias Individuales",
            "Ejecutar Todos los Tests de Parcelas",
            "Ejecutar Todos los Tests de Reservas",
            "Ejecutar Todos los Tests Unitarios",
            "Prueba de Volumen",
            "Prueba de Sobrecarga"
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Seleccione tipo de prueba")
               .setItems(options, (dialog, which) -> {
                   switch (which) {
                       case 0:
                           showUnitTestOptions();
                           break;
                       case 1:
                           DialogUtils.showConfirmationDialog(this,
                               "Tests de Parcelas",
                               "¿Desea ejecutar todos los tests de parcelas?",
                               R.drawable.ic_warning,
                               () -> executeAllParcelTests(getApplication()));
                           break;
                       case 2:
                           DialogUtils.showConfirmationDialog(this,
                               "Tests de Reservas",
                               "¿Desea ejecutar todos los tests de reservas?",
                               R.drawable.ic_warning,
                               () -> executeAllReservationTests(getApplication()));
                           break;
                       case 3:
                           DialogUtils.showConfirmationDialog(this,
                               "Todos los Tests",
                               "¿Desea ejecutar todos los tests unitarios?",
                               R.drawable.ic_warning,
                               () -> {
                                   executeAllParcelTests(getApplication());
                                   executeAllReservationTests(getApplication());
                               });
                           break;
                       case 4:
                           DialogUtils.showConfirmationDialog(this,
                               "Prueba de Volumen",
                               "¿Desea ejecutar la prueba de volumen? Esta operación puede tardar varios minutos.",
                               R.drawable.ic_warning,
                               () -> TestUtils.volumeTest(this, parcelaViewModel, reservaViewModel));
                           break;
                       case 5:
                           DialogUtils.showConfirmationDialog(this,
                               "Prueba de Sobrecarga",
                               "¿Desea ejecutar la prueba de sobrecarga? Esta operación puede causar inestabilidad en la aplicación.",
                               R.drawable.ic_warning,
                               () -> TestUtils.stressTest(getApplication(), parcelaViewModel));
                           break;
                   }
               });
        builder.create().show();
    }

    private void executeAllParcelTests(Application app) {
        try {
            // Tests de inserción
            for (int i = 1; i <= 12; i++) {
                TestUtils.class.getMethod("testInsertarParcela" + i, Application.class)
                    .invoke(null, app);
            }
            // Tests de modificación
            for (int i = 1; i <= 12; i++) {
                TestUtils.class.getMethod("testModificarParcela" + i, Application.class)
                    .invoke(null, app);
            }
            // Tests de eliminación
            for (int i = 1; i <= 3; i++) {
                TestUtils.class.getMethod("testEliminarParcela" + i, Application.class)
                    .invoke(null, app);
            }
            DialogUtils.showSuccessDialog(this, "Tests de parcelas completados", R.drawable.ic_create_success);
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, "Error en tests de parcelas: " + e.getMessage());
        }
    }

    private void executeAllReservationTests(Application app) {
        try {
            // Tests de inserción
            for (int i = 1; i <= 15; i++) {
                TestUtils.class.getMethod("testInsertarReserva" + i, Application.class)
                    .invoke(null, app);
            }
            // Tests de eliminación
            for (int i = 1; i <= 3; i++) {
                TestUtils.class.getMethod("testEliminarReserva" + i, Application.class)
                    .invoke(null, app);
            }
            DialogUtils.showSuccessDialog(this, "Tests de reservas completados", R.drawable.ic_create_success);
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, "Error en tests de reservas: " + e.getMessage());
        }
    }

    private void showUnitTestOptions() {
        String[] options = new String[]{
            // Tests de inserción de parcelas
            "Test Insertar Parcela 1 (Caso válido)",
            "Test Insertar Parcela 2 (Nombre null)",
            "Test Insertar Parcela 3 (Nombre muy largo)",
            "Test Insertar Parcela 4 (Nombre vacío)",
            "Test Insertar Parcela 5 (Descripción null)",
            "Test Insertar Parcela 6 (Descripción muy larga)",
            "Test Insertar Parcela 7 (Ocupantes null)",
            "Test Insertar Parcela 8 (Ocupantes negativos)",
            "Test Insertar Parcela 9 (Ocupantes muy grandes)",
            "Test Insertar Parcela 10 (Precio null)",
            "Test Insertar Parcela 11 (Precio negativo)",
            "Test Insertar Parcela 12 (Precio muy grande)",
            // Tests de modificación de parcelas
            "Test Modificar Parcela 1 (Caso válido)",
            "Test Modificar Parcela 2 (Nombre null)",
            "Test Modificar Parcela 3 (Nombre muy largo)",
            "Test Modificar Parcela 4 (Parcela inexistente)",
            "Test Modificar Parcela 5 (Descripción null)",
            "Test Modificar Parcela 6 (Descripción muy larga)",
            "Test Modificar Parcela 7 (Ocupantes null)",
            "Test Modificar Parcela 8 (Ocupantes negativos)",
            "Test Modificar Parcela 9 (Ocupantes muy grandes)",
            "Test Modificar Parcela 10 (Precio null)",
            "Test Modificar Parcela 11 (Precio negativo)",
            "Test Modificar Parcela 12 (Precio muy grande)",
            // Tests de eliminación de parcelas
            "Test Eliminar Parcela 1 (Caso válido)",
            "Test Eliminar Parcela 2 (Nombre null)",
            "Test Eliminar Parcela 3 (Parcela inexistente)",
            
            // Tests de inserción de reservas
            "Test Insertar Reserva 1 (Caso válido - Clases 1-18)",
            "Test Insertar Reserva 2 (ID null - Clase 19)",
            "Test Insertar Reserva 3 (ID muy grande - Clase 20)",
            "Test Insertar Reserva 4 (ID cero - Clase 21)",
            "Test Insertar Reserva 5 (ID duplicado - Clase 22)",
            "Test Insertar Reserva 6 (Nombre cliente null - Clase 23)",
            "Test Insertar Reserva 7 (Nombre cliente muy largo - Clase 24)",
            "Test Insertar Reserva 8 (Fecha entrada null - Clase 25)",
            "Test Insertar Reserva 9 (Fecha salida anterior - Clase 26)",
            "Test Insertar Reserva 10 (Fecha salida null - Clase 27)",
            "Test Insertar Reserva 11 (Teléfono null - Clase 28)",
            "Test Insertar Reserva 12 (Teléfono inválido - Clase 29)",
            "Test Insertar Reserva 13 (Precio null - Clase 30)",
            "Test Insertar Reserva 14 (Precio cero - Clase 31)",
            "Test Insertar Reserva 15 (Precio muy grande - Clase 32)",
            
            // Tests de eliminación de reservas
            "Test Eliminar Reserva 1 (Caso válido - Clases 1,2)",
            "Test Eliminar Reserva 2 (ID null - Clase 3)",
            "Test Eliminar Reserva 3 (Reserva inexistente - Clase 4)",

            // Tests de inserción de parcelas reservadas
            "Test Insertar Parcela Reservada 1 (Caso válido - Clases 1-5)",
            "Test Insertar Parcela Reservada 2 (Parcela inexistente - Clase 6)",
            "Test Insertar Parcela Reservada 3 (Reserva inexistente - Clase 7)",
            "Test Insertar Parcela Reservada 4 (Duplicada - Clase 8)",
            "Test Insertar Parcela Reservada 5 (Ocupantes null - Clase 9)",
            "Test Insertar Parcela Reservada 6 (Ocupantes cero - Clase 10)",
            "Test Insertar Parcela Reservada 7 (Ocupantes muy grandes - Clase 11)",

            // Tests de modificación de parcelas reservadas
            "Test Modificar Parcela Reservada 1 (Caso válido - Clases 1-5)",
            "Test Modificar Parcela Reservada 2 (Parcela inexistente - Clase 6)",
            "Test Modificar Parcela Reservada 3 (Reserva inexistente - Clase 7)",
            "Test Modificar Parcela Reservada 4 (Inexistente - Clase 8)",
            "Test Modificar Parcela Reservada 5 (Ocupantes null - Clase 9)",
            "Test Modificar Parcela Reservada 6 (Ocupantes cero - Clase 10)",
            "Test Modificar Parcela Reservada 7 (Ocupantes muy grandes - Clase 11)",

            // Tests de eliminación de parcelas reservadas
            "Test Eliminar Parcela Reservada 1 (Caso válido - Clases 1-3)",
            "Test Eliminar Parcela Reservada 2 (Nombre null - Clase 4)",
            "Test Eliminar Parcela Reservada 3 (ID Reserva null - Clase 5)",
            "Test Eliminar Parcela Reservada 4 (Inexistente - Clase 6)"
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Seleccione prueba unitaria")
               .setItems(options, (dialog, which) -> {
                   try {
                       Application app = getApplication();
                       boolean result = false;

                       // Tests de inserción de parcelas (0-11)
                       if (which <= 11) {
                           switch (which) {
                               case 0: result = TestUtils.testInsertarParcela1(app); break;
                               case 1: result = TestUtils.testInsertarParcela2(app); break;
                               case 2: result = TestUtils.testInsertarParcela3(app); break;
                               case 3: result = TestUtils.testInsertarParcela4(app); break;
                               case 4: result = TestUtils.testInsertarParcela5(app); break;
                               case 5: result = TestUtils.testInsertarParcela6(app); break;
                               case 6: result = TestUtils.testInsertarParcela7(app); break;
                               case 7: result = TestUtils.testInsertarParcela8(app); break;
                               case 8: result = TestUtils.testInsertarParcela9(app); break;
                               case 9: result = TestUtils.testInsertarParcela10(app); break;
                               case 10: result = TestUtils.testInsertarParcela11(app); break;
                               case 11: result = TestUtils.testInsertarParcela12(app); break;
                           }
                       }
                       // Tests de modificación de parcelas (12-23)
                       else if (which <= 23) {
                           switch (which - 12) {
                               case 0: result = TestUtils.testModificarParcela1(app); break;
                               case 1: result = TestUtils.testModificarParcela2(app); break;
                               case 2: result = TestUtils.testModificarParcela3(app); break;
                               case 3: result = TestUtils.testModificarParcela4(app); break;
                               case 4: result = TestUtils.testModificarParcela5(app); break;
                               case 5: result = TestUtils.testModificarParcela6(app); break;
                               case 6: result = TestUtils.testModificarParcela7(app); break;
                               case 7: result = TestUtils.testModificarParcela8(app); break;
                               case 8: result = TestUtils.testModificarParcela9(app); break;
                               case 9: result = TestUtils.testModificarParcela10(app); break;
                               case 10: result = TestUtils.testModificarParcela11(app); break;
                               case 11: result = TestUtils.testModificarParcela12(app); break;
                           }
                       }
                       // Tests de eliminación de parcelas (24-26)
                       else if (which <= 26) {
                           switch (which - 24) {
                               case 0: result = TestUtils.testEliminarParcela1(app); break;
                               case 1: result = TestUtils.testEliminarParcela2(app); break;
                               case 2: result = TestUtils.testEliminarParcela3(app); break;
                           }
                       }
                       // Tests de inserción de reservas (27-41)
                       else if (which <= 41) {
                           switch (which - 27) {
                               case 0: result = TestUtils.testInsertarReserva1(app); break;
                               case 1: result = TestUtils.testInsertarReserva2(app); break;
                               case 2: result = TestUtils.testInsertarReserva3(app); break;
                               case 3: result = TestUtils.testInsertarReserva4(app); break;
                               case 4: result = TestUtils.testInsertarReserva5(app); break;
                               case 5: result = TestUtils.testInsertarReserva6(app); break;
                               case 6: result = TestUtils.testInsertarReserva7(app); break;
                               case 7: result = TestUtils.testInsertarReserva8(app); break;
                               case 8: result = TestUtils.testInsertarReserva9(app); break;
                               case 9: result = TestUtils.testInsertarReserva10(app); break;
                               case 10: result = TestUtils.testInsertarReserva11(app); break;
                               case 11: result = TestUtils.testInsertarReserva12(app); break;
                               case 12: result = TestUtils.testInsertarReserva13(app); break;
                               case 13: result = TestUtils.testInsertarReserva14(app); break;
                               case 14: result = TestUtils.testInsertarReserva15(app); break;
                           }
                       }
                       // Tests de eliminación de reservas (42-44)
                       else if (which <= 44) {
                           switch (which - 42) {
                               case 0: result = TestUtils.testEliminarReserva1(app); break;
                               case 1: result = TestUtils.testEliminarReserva2(app); break;
                               case 2: result = TestUtils.testEliminarReserva3(app); break;
                           }
                       }
                       // Tests de inserción de parcelas reservadas (45-51)
                       else if (which <= 51) {
                           switch (which - 45) {
                               case 0: result = TestUtils.testInsertarParcelaReservada1(app); break;
                               case 1: result = TestUtils.testInsertarParcelaReservada2(app); break;
                               case 2: result = TestUtils.testInsertarParcelaReservada3(app); break;
                               case 3: result = TestUtils.testInsertarParcelaReservada4(app); break;
                               case 4: result = TestUtils.testInsertarParcelaReservada5(app); break;
                               case 5: result = TestUtils.testInsertarParcelaReservada6(app); break;
                               case 6: result = TestUtils.testInsertarParcelaReservada7(app); break;
                           }
                       }
                       // Tests de modificación de parcelas reservadas (52-58)
                       else if (which <= 58) {
                           switch (which - 52) {
                               case 0: result = TestUtils.testModificarParcelaReservada1(app); break;
                               case 1: result = TestUtils.testModificarParcelaReservada2(app); break;
                               case 2: result = TestUtils.testModificarParcelaReservada3(app); break;
                               case 3: result = TestUtils.testModificarParcelaReservada4(app); break;
                               case 4: result = TestUtils.testModificarParcelaReservada5(app); break;
                               case 5: result = TestUtils.testModificarParcelaReservada6(app); break;
                               case 6: result = TestUtils.testModificarParcelaReservada7(app); break;
                           }
                       }
                       // Tests de eliminación de parcelas reservadas (59-62)
                       else {
                           switch (which - 59) {
                               case 0: result = TestUtils.testEliminarParcelaReservada1(app); break;
                               case 1: result = TestUtils.testEliminarParcelaReservada2(app); break;
                               case 2: result = TestUtils.testEliminarParcelaReservada3(app); break;
                               case 3: result = TestUtils.testEliminarParcelaReservada4(app); break;
                           }
                       }

                       if (result) {
                           DialogUtils.showSuccessDialog(this, "Test ejecutado correctamente", R.drawable.ic_create_success);
                       } else {
                           DialogUtils.showErrorDialog(this, "El test ha fallado");
                       }
                   } catch (Exception e) {
                       DialogUtils.showErrorDialog(this, "Error al ejecutar el test: " + e.getMessage());
                   }
               });
        builder.create().show();
    }

    private void executeAllParcelaReservadaTests(Application app) {
        try {
            boolean success = true;
            // Tests de inserción
            for (int i = 1; i <= 7 && success; i++) {
                success = (boolean) TestUtils.class.getMethod("testInsertarParcelaReservada" + i, Application.class)
                    .invoke(null, app);
            }
            // Tests de modificación
            for (int i = 1; i <= 7 && success; i++) {
                success = (boolean) TestUtils.class.getMethod("testModificarParcelaReservada" + i, Application.class)
                    .invoke(null, app);
            }
            // Tests de eliminación
            for (int i = 1; i <= 4 && success; i++) {
                success = (boolean) TestUtils.class.getMethod("testEliminarParcelaReservada" + i, Application.class)
                    .invoke(null, app);
            }
            
            if (success) {
                DialogUtils.showSuccessDialog(this, "Tests de parcelas reservadas completados", R.drawable.ic_create_success);
            } else {
                DialogUtils.showErrorDialog(this, "Uno o más tests de parcelas reservadas han fallado");
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, "Error en tests de parcelas reservadas: " + e.getMessage());
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected String getToolbarTitle() {
        return "Home";
    }
}
