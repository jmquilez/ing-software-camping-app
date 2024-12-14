package es.unizar.eina.T213_camping.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.parcelas.listado.ParcelFeedActivity;
import es.unizar.eina.T213_camping.ui.reservas.listado.ReservationFeedActivity;
import es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel;
import es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel;
import es.unizar.eina.T213_camping.utils.DialogUtils;
import es.unizar.eina.T213_camping.utils.TestUtils.StressTests;
import es.unizar.eina.T213_camping.utils.TestUtils.UnitTests;
import es.unizar.eina.T213_camping.utils.TestUtils.VolumeTests;

/**
 * Actividad principal que sirve como punto de entrada a las principales
 * funcionalidades.
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
        String[] options = new String[] {
            "// Tests Individuales",
            "Tests Individuales (Parcelas, Reservas y Parcelas Reservadas)",
            "// Tests por Lotes",
            "Tests de Parcelas (Inserción, Modificación y Eliminación)",
            "Tests de Reservas (Inserción, Modificación y Eliminación)",
            "Tests de Parcelas Reservadas (Inserción, Modificación y Eliminación)",
            "// Tests Completos",
            "Todos los Tests Unitarios",
            "// Pruebas de Sistema",
            "Prueba de Volumen (1000 parcelas, 10000 reservas)",
            "Prueba de Sobrecarga (Límite de caracteres)"
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Seleccione tipo de prueba");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.test_item, android.R.id.text1, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                if (options[position].startsWith("// ")) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getColor(R.color.green));
                }
                View divider = view.findViewById(R.id.test_item_divider);
                if (position == getCount() - 1) {
                    divider.setVisibility(View.GONE);
                }
                return view;
            }
        };

        builder.setAdapter(adapter, (dialog, which) -> {
            // Skip category headers
            if (!options[which].startsWith("// ")) {
                switch (which) {
                    case 1: // Tests Individuales
                        showUnitTestOptions();
                        break;
                    case 3: // Tests de Parcelas
                        DialogUtils.showConfirmationDialog(this,
                                "Tests de Parcelas",
                                "¿Desea ejecutar todos los tests de parcelas?",
                                R.drawable.ic_warning,
                                () -> executeAllParcelTests(getApplication()));
                        break;
                    case 4: // Tests de Reservas
                        DialogUtils.showConfirmationDialog(this,
                                "Tests de Reservas",
                                "¿Desea ejecutar todos los tests de reservas?",
                                R.drawable.ic_warning,
                                () -> executeAllReservationTests(getApplication()));
                        break;
                    case 5: // Tests de Parcelas Reservadas
                        DialogUtils.showConfirmationDialog(this,
                                "Tests de Parcelas Reservadas",
                                "¿Desea ejecutar todos los tests de parcelas reservadas?",
                                R.drawable.ic_warning,
                                () -> executeAllParcelaReservadaTests(getApplication()));
                        break;
                    case 7: // Todos los Tests
                        DialogUtils.showConfirmationDialog(this,
                                "Todos los Tests",
                                "¿Desea ejecutar todos los tests unitarios?",
                                R.drawable.ic_warning,
                                () -> {
                                    executeAllParcelTests(getApplication());
                                    executeAllReservationTests(getApplication());
                                    executeAllParcelaReservadaTests(getApplication());
                                });
                        break;
                    case 9: // Prueba de Volumen
                        DialogUtils.showConfirmationDialog(this,
                                "Prueba de Volumen",
                                "¿Desea ejecutar la prueba de volumen? Esta operación puede tardar varios minutos.",
                                R.drawable.ic_warning,
                                () -> {
                                    final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, "Ejecutando prueba de volumen...");
                                    new android.os.Handler().postDelayed(() -> {
                                        boolean result = VolumeTests.volumeTest(this, parcelaViewModel, reservaViewModel);
                                        loadingDialog.dismiss();
                                        if (result) {
                                            DialogUtils.showSuccessDialog(this, 
                                                "Prueba de volumen completada exitosamente", 
                                                R.drawable.ic_create_success);
                                        } else {
                                            DialogUtils.showErrorDialog(this, 
                                                "La prueba de volumen ha fallado");
                                        }
                                    }, 500);
                                });
                        break;
                    case 10: // Prueba de Sobrecarga
                        DialogUtils.showConfirmationDialog(this,
                                "Prueba de Sobrecarga",
                                "¿Desea ejecutar la prueba de sobrecarga? Esta operación puede causar inestabilidad en la aplicación.",
                                R.drawable.ic_warning,
                                () -> {
                                    final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, "Ejecutando prueba de sobrecarga...");
                                    new android.os.Handler().postDelayed(() -> {
                                        boolean result = StressTests.stressTest(getApplication(), parcelaViewModel);
                                        loadingDialog.dismiss();
                                        if (result) {
                                            DialogUtils.showSuccessDialog(this, 
                                                "Prueba de sobrecarga completada exitosamente", 
                                                R.drawable.ic_create_success);
                                        } else {
                                            DialogUtils.showErrorDialog(this, 
                                                "La prueba de sobrecarga ha fallado");
                                        }
                                    }, 500);
                                });
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void executeAllParcelTests(Application app) {
        try {
            boolean success = true;
            int failedTest = -1;
            
            // Tests de inserción
            for (int i = 1; i <= 13 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testInsertarParcela" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i;
            }
            
            // Tests de modificación
            for (int i = 1; i <= 13 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testModificarParcela" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 13; // Offset for modification tests
            }
            
            // Tests de eliminación
            for (int i = 1; i <= 3 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testEliminarParcela" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 26; // Offset for deletion tests
            }

            if (success) {
                DialogUtils.showSuccessDialog(this, "Tests de parcelas completados con éxito", R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 13 ? "Inserción" : 
                                failedTest <= 26 ? "Modificación" : 
                                "Eliminación";
                int testNumber = failedTest <= 13 ? failedTest : 
                               failedTest <= 26 ? failedTest - 13 : 
                               failedTest - 26;
                
                DialogUtils.showErrorDialog(this, 
                    "Error en test de " + testType + " de Parcela " + testNumber);
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, "Error en tests de parcelas: " + e.getMessage());
        }
    }

    private void executeAllReservationTests(Application app) {
        try {
            boolean success = true;
            int failedTest = -1;
            
            // Tests de inserción
            for (int i = 1; i <= 16 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testInsertarReserva" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i;
            }
            
            // Tests de modificación
            for (int i = 1; i <= 16 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testModificarReserva" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 16; // Offset for modification tests
            }
            
            // Tests de eliminación
            for (int i = 1; i <= 3 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testEliminarReserva" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 32; // Offset for deletion tests
            }

            if (success) {
                DialogUtils.showSuccessDialog(this, "Tests de reservas completados con éxito", R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 16 ? "Inserción" : 
                                failedTest <= 32 ? "Modificación" : 
                                "Eliminación";
                int testNumber = failedTest <= 16 ? failedTest : 
                               failedTest <= 32 ? failedTest - 16 : 
                               failedTest - 32;
                
                DialogUtils.showErrorDialog(this, 
                    "Error en test de " + testType + " de Reserva " + testNumber);
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, "Error en tests de reservas: " + e.getMessage());
        }
    }

    private void showUnitTestOptions() {
        String[] options = new String[] {
                // Tests de inserción de parcelas
                "Test Insertar Parcela 1 (Caso válido - Clases 1-9)",
                "Test Insertar Parcela 2 (Nombre null - Clase 10)",
                "Test Insertar Parcela 3 (Nombre muy largo - Clase 11)",
                "Test Insertar Parcela 4 (Nombre vacío - Clase 12)",
                "Test Insertar Parcela 5 (Parcela duplicada - Clase 13)",
                "Test Insertar Parcela 6 (Descripción null - Clase 14)",
                "Test Insertar Parcela 7 (Descripción muy larga - Clase 15)",
                "Test Insertar Parcela 8 (Ocupantes null - Clase 16)",
                "Test Insertar Parcela 9 (Ocupantes negativos - Clase 17)",
                "Test Insertar Parcela 10 (Ocupantes muy grandes - Clase 18)",
                "Test Insertar Parcela 11 (Precio null - Clase 19)",
                "Test Insertar Parcela 12 (Precio negativo - Clase 20)",
                "Test Insertar Parcela 13 (Precio muy grande - Clase 21)",

                // Tests de modificación de parcelas
                "Test Modificar Parcela 1 (Caso válido - Clases 1-9)",
                "Test Modificar Parcela 2 (Nombre null - Clase 10)",
                "Test Modificar Parcela 3 (Nombre muy largo - Clase 11)",
                "Test Modificar Parcela 4 (Nombre vacío - Clase 12)",
                "Test Modificar Parcela 5 (Parcela inexistente - Clase 13)",
                "Test Modificar Parcela 6 (Descripción null - Clase 14)",
                "Test Modificar Parcela 7 (Descripción muy larga - Clase 15)",
                "Test Modificar Parcela 8 (Ocupantes null - Clase 16)",
                "Test Modificar Parcela 9 (Ocupantes negativos - Clase 17)",
                "Test Modificar Parcela 10 (Ocupantes muy grandes - Clase 18)",
                "Test Modificar Parcela 11 (Precio null - Clase 19)",
                "Test Modificar Parcela 12 (Precio negativo - Clase 20)",
                "Test Modificar Parcela 13 (Precio muy grande - Clase 21)",

                // Tests de eliminación de parcelas
                "Test Eliminar Parcela 1 (Caso válido - Clases 1,2)",
                "Test Eliminar Parcela 2 (Nombre null - Clase 3)",
                "Test Eliminar Parcela 3 (Parcela inexistente - Clase 4)",

                // Tests de inserción de reservas
                "Test Insertar Reserva 1 (Caso válido - Clases 1-12)",
                "Test Insertar Reserva 2 (ID null - Clase 13)",
                "Test Insertar Reserva 3 (ID muy grande - Clase 14)",
                "Test Insertar Reserva 4 (ID cero - Clase 15)",
                "Test Insertar Reserva 5 (ID duplicado - Clase 16)",
                "Test Insertar Reserva 6 (Nombre cliente null - Clase 17)",
                "Test Insertar Reserva 7 (Nombre cliente cadena vacía - Clase 18)",
                "Test Insertar Reserva 8 (Nombre cliente muy largo - Clase 19)",
                "Test Insertar Reserva 9 (Fecha entrada null - Clase 20)",
                "Test Insertar Reserva 10 (Fecha salida anterior - Clase 21)",
                "Test Insertar Reserva 11 (Fecha salida null - Clase 22)",
                "Test Insertar Reserva 12 (Teléfono null - Clase 23)",
                "Test Insertar Reserva 13 (Teléfono inválido - Clase 24)",
                "Test Insertar Reserva 14 (Precio null - Clase 25)",
                "Test Insertar Reserva 15 (Precio cero - Clase 26)",
                "Test Insertar Reserva 16 (Precio muy grande - Clase 27)",

                // Tests de modificación de reservas
                "Test Modificar Reserva 1 (Caso válido - Clases 1-12)",
                "Test Modificar Reserva 2 (ID null - Clase 13)",
                "Test Modificar Reserva 3 (ID muy grande - Clase 14)",
                "Test Modificar Reserva 4 (ID cero - Clase 15)",
                "Test Modificar Reserva 5 (ID inexistente - Clase 16)",
                "Test Modificar Reserva 6 (Nombre cliente null - Clase 17)",
                "Test Modificar Reserva 7 (Nombre cliente cadena vacía - Clase 18)",
                "Test Modificar Reserva 8 (Nombre cliente muy largo - Clase 19)",
                "Test Modificar Reserva 9 (Fecha entrada null - Clase 20)",
                "Test Modificar Reserva 10 (Fecha salida anterior - Clase 21)",
                "Test Modificar Reserva 11 (Fecha salida null - Clase 22)",
                "Test Modificar Reserva 12 (Teléfono null - Clase 23)",
                "Test Modificar Reserva 13 (Teléfono inválido - Clase 24)",
                "Test Modificar Reserva 14 (Precio null - Clase 25)",
                "Test Modificar Reserva 15 (Precio cero - Clase 26)",
                "Test Modificar Reserva 16 (Precio muy grande - Clase 27)",

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
        builder.setTitle("Seleccione prueba unitaria");

        // Create adapter with custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.test_item, android.R.id.text1, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                
                // Make category headers bold
                TextView textView = view.findViewById(android.R.id.text1);
                if (options[position].startsWith("// Tests")) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getColor(R.color.green));
                }
                
                // Hide divider for last item
                View divider = view.findViewById(R.id.test_item_divider);
                if (position == getCount() - 1) {
                    divider.setVisibility(View.GONE);
                }
                
                return view;
            }
        };

        builder.setAdapter(adapter, (dialog, which) -> {
            try {
                Application app = getApplication();
                boolean result = false;

                // Tests de inserción de parcelas (0-12)
                if (which <= 12) {
                    switch (which) {
                        case 0:
                            result = UnitTests.testInsertarParcela1(app);
                            break;
                        case 1:
                            result = UnitTests.testInsertarParcela2(app);
                            break;
                        case 2:
                            result = UnitTests.testInsertarParcela3(app);
                            break;
                        case 3:
                            result = UnitTests.testInsertarParcela4(app);
                            break;
                        case 4:
                            result = UnitTests.testInsertarParcela5(app);
                            break;
                        case 5:
                            result = UnitTests.testInsertarParcela6(app);
                            break;
                        case 6:
                            result = UnitTests.testInsertarParcela7(app);
                            break;
                        case 7:
                            result = UnitTests.testInsertarParcela8(app);
                            break;
                        case 8:
                            result = UnitTests.testInsertarParcela9(app);
                            break;
                        case 9:
                            result = UnitTests.testInsertarParcela10(app);
                            break;
                        case 10:
                            result = UnitTests.testInsertarParcela11(app);
                            break;
                        case 11:
                            result = UnitTests.testInsertarParcela12(app);
                            break;
                        case 12:
                            result = UnitTests.testInsertarParcela13(app);
                            break;
                    }
                }
                // Tests de modificación de parcelas (13-25)
                else if (which <= 25) {
                    switch (which - 13) {
                        case 0:
                            result = UnitTests.testModificarParcela1(app);
                            break;
                        case 1:
                            result = UnitTests.testModificarParcela2(app);
                            break;
                        case 2:
                            result = UnitTests.testModificarParcela3(app);
                            break;
                        case 3:
                            result = UnitTests.testModificarParcela4(app);
                            break;
                        case 4:
                            result = UnitTests.testModificarParcela5(app);
                            break;
                        case 5:
                            result = UnitTests.testModificarParcela6(app);
                            break;
                        case 6:
                            result = UnitTests.testModificarParcela7(app);
                            break;
                        case 7:
                            result = UnitTests.testModificarParcela8(app);
                            break;
                        case 8:
                            result = UnitTests.testModificarParcela9(app);
                            break;
                        case 9:
                            result = UnitTests.testModificarParcela10(app);
                            break;
                        case 10:
                            result = UnitTests.testModificarParcela11(app);
                            break;
                        case 11:
                            result = UnitTests.testModificarParcela12(app);
                            break;
                        case 12:
                            result = UnitTests.testModificarParcela13(app);
                            break;
                    }
                }
                // Tests de eliminación de parcelas (26-28)
                else if (which <= 28) {
                    switch (which - 26) {
                        case 0:
                            result = UnitTests.testEliminarParcela1(app);
                            break;
                        case 1:
                            result = UnitTests.testEliminarParcela2(app);
                            break;
                        case 2:
                            result = UnitTests.testEliminarParcela3(app);
                            break;
                    }
                }
                // Tests de inserción de reservas (29-44)
                else if (which <= 44) {
                    switch (which - 29) {
                        case 0:
                            result = UnitTests.testInsertarReserva1(app);
                            break;
                        case 1:
                            result = UnitTests.testInsertarReserva2(app);
                            break;
                        case 2:
                            result = UnitTests.testInsertarReserva3(app);
                            break;
                        case 3:
                            result = UnitTests.testInsertarReserva4(app);
                            break;
                        case 4:
                            result = UnitTests.testInsertarReserva5(app);
                            break;
                        case 5:
                            result = UnitTests.testInsertarReserva6(app);
                            break;
                        case 6:
                            result = UnitTests.testInsertarReserva7(app);
                            break;
                        case 7:
                            result = UnitTests.testInsertarReserva8(app);
                            break;
                        case 8:
                            result = UnitTests.testInsertarReserva9(app);
                            break;
                        case 9:
                            result = UnitTests.testInsertarReserva10(app);
                            break;
                        case 10:
                            result = UnitTests.testInsertarReserva11(app);
                            break;
                        case 11:
                            result = UnitTests.testInsertarReserva12(app);
                            break;
                        case 12:
                            result = UnitTests.testInsertarReserva13(app);
                            break;
                        case 13:
                            result = UnitTests.testInsertarReserva14(app);
                            break;
                        case 14:
                            result = UnitTests.testInsertarReserva15(app);
                            break;
                        case 15:
                            result = UnitTests.testInsertarReserva16(app);
                            break;
                    }
                }
                // Tests de modificación de reservas (45-60)
                else if (which <= 60) {
                    switch (which - 45) {
                        case 0:
                            result = UnitTests.testModificarReserva1(app);
                            break;
                        case 1:
                            result = UnitTests.testModificarReserva2(app);
                            break;
                        case 2:
                            result = UnitTests.testModificarReserva3(app);
                            break;
                        case 3:
                            result = UnitTests.testModificarReserva4(app);
                            break;
                        case 4:
                            result = UnitTests.testModificarReserva5(app);
                            break;
                        case 5:
                            result = UnitTests.testModificarReserva6(app);
                            break;
                        case 6:
                            result = UnitTests.testModificarReserva7(app);
                            break;
                        case 7:
                            result = UnitTests.testModificarReserva8(app);
                            break;
                        case 8:
                            result = UnitTests.testModificarReserva9(app);
                            break;
                        case 9:
                            result = UnitTests.testModificarReserva10(app);
                            break;
                        case 10:
                            result = UnitTests.testModificarReserva11(app);
                            break;
                        case 11:
                            result = UnitTests.testModificarReserva12(app);
                            break;
                        case 12:
                            result = UnitTests.testModificarReserva13(app);
                            break;
                        case 13:
                            result = UnitTests.testModificarReserva14(app);
                            break;
                        case 14:
                            result = UnitTests.testModificarReserva15(app);
                            break;
                        case 15:
                            result = UnitTests.testModificarReserva16(app);
                            break;
                    }
                }
                // Tests de eliminación de reservas (61-63)
                else if (which <= 63) {
                    switch (which - 61) {
                        case 0:
                            result = UnitTests.testEliminarReserva1(app);
                            break;
                        case 1:
                            result = UnitTests.testEliminarReserva2(app);
                            break;
                        case 2:
                            result = UnitTests.testEliminarReserva3(app);
                            break;
                    }
                }
                // Tests de inserción de parcelas reservadas (64-70)
                else if (which <= 70) {
                    switch (which - 64) {
                        case 0:
                            result = UnitTests.testInsertarParcelaReservada1(app);
                            break;
                        case 1:
                            result = UnitTests.testInsertarParcelaReservada2(app);
                            break;
                        case 2:
                            result = UnitTests.testInsertarParcelaReservada3(app);
                            break;
                        case 3:
                            result = UnitTests.testInsertarParcelaReservada4(app);
                            break;
                        case 4:
                            result = UnitTests.testInsertarParcelaReservada5(app);
                            break;
                        case 5:
                            result = UnitTests.testInsertarParcelaReservada6(app);
                            break;
                        case 6:
                            result = UnitTests.testInsertarParcelaReservada7(app);
                            break;
                    }
                }
                // Tests de modificación de parcelas reservadas (71-77)
                else if (which <= 77) {
                    switch (which - 71) {
                        case 0:
                            result = UnitTests.testModificarParcelaReservada1(app);
                            break;
                        case 1:
                            result = UnitTests.testModificarParcelaReservada2(app);
                            break;
                        case 2:
                            result = UnitTests.testModificarParcelaReservada3(app);
                            break;
                        case 3:
                            result = UnitTests.testModificarParcelaReservada4(app);
                            break;
                        case 4:
                            result = UnitTests.testModificarParcelaReservada5(app);
                            break;
                        case 5:
                            result = UnitTests.testModificarParcelaReservada6(app);
                            break;
                        case 6:
                            result = UnitTests.testModificarParcelaReservada7(app);
                            break;
                    }
                }
                // Tests de eliminación de parcelas reservadas (78-81)
                else {
                    switch (which - 78) {
                        case 0:
                            result = UnitTests.testEliminarParcelaReservada1(app);
                            break;
                        case 1:
                            result = UnitTests.testEliminarParcelaReservada2(app);
                            break;
                        case 2:
                            result = UnitTests.testEliminarParcelaReservada3(app);
                            break;
                        case 3:
                            result = UnitTests.testEliminarParcelaReservada4(app);
                            break;
                    }
                }

                if (result) {
                    DialogUtils.showSuccessDialog(this, "Test ejecutado correctamente",
                            R.drawable.ic_create_success);
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
            int failedTest = -1;
            
            // Tests de inserción
            for (int i = 1; i <= 7 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testInsertarParcelaReservada" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i;
            }
            
            // Tests de modificación
            for (int i = 1; i <= 7 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testModificarParcelaReservada" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 7; // Offset for modification tests
            }
            
            // Tests de eliminación
            for (int i = 1; i <= 4 && success; i++) {
                success = (boolean) UnitTests.class.getMethod("testEliminarParcelaReservada" + i, Application.class)
                        .invoke(null, app);
                if (!success) failedTest = i + 14; // Offset for deletion tests
            }

            if (success) {
                DialogUtils.showSuccessDialog(this, "Tests de parcelas reservadas completados con éxito", 
                        R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 7 ? "Inserción" : 
                                failedTest <= 14 ? "Modificación" : 
                                "Eliminación";
                int testNumber = failedTest <= 7 ? failedTest : 
                               failedTest <= 14 ? failedTest - 7 : 
                               failedTest - 14;
                
                DialogUtils.showErrorDialog(this, 
                    "Error en test de " + testType + " de Parcela Reservada " + testNumber);
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
