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
import android.os.Looper;

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

        testButton.setOnClickListener(v -> showTestOptions());
    }

    private void showTestOptions() {
        String[] options = new String[] {
            getString(R.string.test_category_individual),
            getString(R.string.test_individual_title),
            getString(R.string.test_category_batch),
            getString(R.string.test_parcelas_title),
            getString(R.string.test_reservas_title),
            getString(R.string.test_parcelas_reservadas_title),
            getString(R.string.test_category_complete),
            getString(R.string.test_all_title),
            getString(R.string.test_category_system),
            getString(R.string.test_volume_title),
            getString(R.string.test_stress_title)
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(getString(R.string.dialog_test_select_type));

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
                                getString(R.string.test_parcelas_title),
                                getString(R.string.dialog_confirm_parcelas),
                                R.drawable.ic_warning,
                                () -> executeAllParcelTests(getApplication()));
                        break;
                    case 4: // Tests de Reservas
                        DialogUtils.showConfirmationDialog(this,
                                getString(R.string.test_reservas_title),
                                getString(R.string.dialog_confirm_reservas),
                                R.drawable.ic_warning,
                                () -> executeAllReservationTests(getApplication()));
                        break;
                    case 5: // Tests de Parcelas Reservadas
                        DialogUtils.showConfirmationDialog(this,
                                getString(R.string.test_parcelas_reservadas_title),
                                getString(R.string.dialog_confirm_parcelas_reservadas),
                                R.drawable.ic_warning,
                                () -> executeAllParcelaReservadaTests(getApplication()));
                        break;
                    case 7: // Todos los Tests
                        DialogUtils.showConfirmationDialog(this,
                                getString(R.string.test_all_title),
                                getString(R.string.dialog_confirm_all),
                                R.drawable.ic_warning,
                                () -> {
                                    executeAllParcelTests(getApplication());
                                    executeAllReservationTests(getApplication());
                                    executeAllParcelaReservadaTests(getApplication());
                                });
                        break;
                    case 9: // Prueba de Volumen
                        DialogUtils.showConfirmationDialog(this,
                                getString(R.string.test_volume_title),
                                getString(R.string.dialog_confirm_volume),
                                R.drawable.ic_warning,
                                () -> {
                                    final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, getString(R.string.dialog_loading_volume));
                                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                                        boolean result = VolumeTests.volumeTest(this, parcelaViewModel, reservaViewModel);
                                        loadingDialog.dismiss();
                                        if (result) {
                                            DialogUtils.showSuccessDialog(this, 
                                                getString(R.string.test_volume_success), 
                                                R.drawable.ic_create_success);
                                        } else {
                                            DialogUtils.showErrorDialog(this, 
                                                getString(R.string.test_volume_failed));
                                        }
                                    }, 500);
                                });
                        break;
                    case 10: // Prueba de Sobrecarga
                        DialogUtils.showConfirmationDialog(this,
                                getString(R.string.test_stress_title),
                                getString(R.string.dialog_confirm_stress),
                                R.drawable.ic_warning,
                                () -> {
                                    final Dialog loadingDialog = DialogUtils.showLoadingDialog(this, getString(R.string.dialog_loading_stress));
                                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                                        boolean result = StressTests.stressTest(getApplication(), parcelaViewModel);
                                        loadingDialog.dismiss();
                                        if (result) {
                                            DialogUtils.showSuccessDialog(this, 
                                                getString(R.string.test_stress_success), 
                                                R.drawable.ic_create_success);
                                        } else {
                                            DialogUtils.showErrorDialog(this, 
                                                getString(R.string.test_stress_failed));
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
                DialogUtils.showSuccessDialog(this, getString(R.string.test_parcelas_success), R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 13 ? getString(R.string.test_type_insercion) : 
                                failedTest <= 26 ? getString(R.string.test_type_modificacion) : 
                                getString(R.string.test_type_eliminacion);
                int testNumber = failedTest <= 13 ? failedTest : 
                               failedTest <= 26 ? failedTest - 13 : 
                               failedTest - 26;
                
                DialogUtils.showErrorDialog(this, 
                    getString(R.string.test_error_type, 
                        getString(getTestTypeStringResource(testType)), 
                        getTestEntityName("Parcela"), 
                        testNumber));
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, getString(R.string.test_parcelas_error, e.getMessage()));
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
                DialogUtils.showSuccessDialog(this, getString(R.string.test_reservas_success), R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 16 ? getString(R.string.test_type_insercion) : 
                                failedTest <= 32 ? getString(R.string.test_type_modificacion) : 
                                getString(R.string.test_type_eliminacion);
                int testNumber = failedTest <= 16 ? failedTest : 
                               failedTest <= 32 ? failedTest - 16 : 
                               failedTest - 32;
                
                DialogUtils.showErrorDialog(this, 
                    getString(R.string.test_error_type, 
                        getString(getTestTypeStringResource(testType)), 
                        getTestEntityName("Reserva"), 
                        testNumber));
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, getString(R.string.test_reservas_error, e.getMessage()));
        }
    }

    private void showUnitTestOptions() {
        String[] options = new String[] {
            getString(R.string.test_insert_parcela_1),
            getString(R.string.test_insert_parcela_2),
            getString(R.string.test_insert_parcela_3),
            getString(R.string.test_insert_parcela_4),
            getString(R.string.test_insert_parcela_5),
            getString(R.string.test_insert_parcela_6),
            getString(R.string.test_insert_parcela_7),
            getString(R.string.test_insert_parcela_8),
            getString(R.string.test_insert_parcela_9),
            getString(R.string.test_insert_parcela_10),
            getString(R.string.test_insert_parcela_11),
            getString(R.string.test_insert_parcela_12),
            getString(R.string.test_insert_parcela_13),
            getString(R.string.test_modify_parcela_1),
            getString(R.string.test_modify_parcela_2),
            getString(R.string.test_modify_parcela_3),
            getString(R.string.test_modify_parcela_4),
            getString(R.string.test_modify_parcela_5),
            getString(R.string.test_modify_parcela_6),
            getString(R.string.test_modify_parcela_7),
            getString(R.string.test_modify_parcela_8),
            getString(R.string.test_modify_parcela_9),
            getString(R.string.test_modify_parcela_10),
            getString(R.string.test_modify_parcela_11),
            getString(R.string.test_modify_parcela_12),
            getString(R.string.test_modify_parcela_13),
            getString(R.string.test_delete_parcela_1),
            getString(R.string.test_delete_parcela_2),
            getString(R.string.test_delete_parcela_3),
            getString(R.string.test_insert_reserva_1),
            getString(R.string.test_insert_reserva_2),
            getString(R.string.test_insert_reserva_3),
            getString(R.string.test_insert_reserva_4),
            getString(R.string.test_insert_reserva_5),
            getString(R.string.test_insert_reserva_6),
            getString(R.string.test_insert_reserva_7),
            getString(R.string.test_insert_reserva_8),
            getString(R.string.test_insert_reserva_9),
            getString(R.string.test_insert_reserva_10),
            getString(R.string.test_insert_reserva_11),
            getString(R.string.test_insert_reserva_12),
            getString(R.string.test_insert_reserva_13),
            getString(R.string.test_insert_reserva_14),
            getString(R.string.test_insert_reserva_15),
            getString(R.string.test_insert_reserva_16),
            getString(R.string.test_modify_reserva_1),
            getString(R.string.test_modify_reserva_2),
            getString(R.string.test_modify_reserva_3),
            getString(R.string.test_modify_reserva_4),
            getString(R.string.test_modify_reserva_5),
            getString(R.string.test_modify_reserva_6),
            getString(R.string.test_modify_reserva_7),
            getString(R.string.test_modify_reserva_8),
            getString(R.string.test_modify_reserva_9),
            getString(R.string.test_modify_reserva_10),
            getString(R.string.test_modify_reserva_11),
            getString(R.string.test_modify_reserva_12),
            getString(R.string.test_modify_reserva_13),
            getString(R.string.test_modify_reserva_14),
            getString(R.string.test_modify_reserva_15),
            getString(R.string.test_modify_reserva_16),
            getString(R.string.test_delete_reserva_1),
            getString(R.string.test_delete_reserva_2),
            getString(R.string.test_delete_reserva_3),
            getString(R.string.test_insert_parcela_reservada_1),
            getString(R.string.test_insert_parcela_reservada_2),
            getString(R.string.test_insert_parcela_reservada_3),
            getString(R.string.test_insert_parcela_reservada_4),
            getString(R.string.test_insert_parcela_reservada_5),
            getString(R.string.test_insert_parcela_reservada_6),
            getString(R.string.test_insert_parcela_reservada_7),
            getString(R.string.test_modify_parcela_reservada_1),
            getString(R.string.test_modify_parcela_reservada_2),
            getString(R.string.test_modify_parcela_reservada_3),
            getString(R.string.test_modify_parcela_reservada_4),
            getString(R.string.test_modify_parcela_reservada_5),
            getString(R.string.test_modify_parcela_reservada_6),
            getString(R.string.test_modify_parcela_reservada_7),
            getString(R.string.test_delete_parcela_reservada_1),
            getString(R.string.test_delete_parcela_reservada_2),
            getString(R.string.test_delete_parcela_reservada_3),
            getString(R.string.test_delete_parcela_reservada_4)
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(getString(R.string.dialog_test_select_unit));

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
                    DialogUtils.showSuccessDialog(this, getString(R.string.test_success),
                            R.drawable.ic_create_success);
                } else {
                    DialogUtils.showErrorDialog(this, getString(R.string.test_failed));
                }
            } catch (Exception e) {
                DialogUtils.showErrorDialog(this, getString(R.string.test_error, e.getMessage()));
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
                DialogUtils.showSuccessDialog(this, getString(R.string.test_parcelas_reservadas_success), 
                        R.drawable.ic_create_success);
            } else {
                String testType = failedTest <= 7 ? getString(R.string.test_type_insercion) : 
                                failedTest <= 14 ? getString(R.string.test_type_modificacion) : 
                                getString(R.string.test_type_eliminacion);
                int testNumber = failedTest <= 7 ? failedTest : 
                               failedTest <= 14 ? failedTest - 7 : 
                               failedTest - 14;
                
                DialogUtils.showErrorDialog(this, 
                    getString(R.string.test_error_type, 
                        getString(getTestTypeStringResource(testType)), 
                        getTestEntityName("ParcelaReservada"), 
                        testNumber));
            }
        } catch (Exception e) {
            DialogUtils.showErrorDialog(this, getString(R.string.test_parcelas_reservadas_error, e.getMessage()));
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.home_title);
    }

    private int getTestTypeStringResource(String type) {
        switch(type) {
            case "Inserción": return R.string.test_type_insercion;
            case "Modificación": return R.string.test_type_modificacion;
            case "Eliminación": return R.string.test_type_eliminacion;
            default: return R.string.test_type_insercion;
        }
    }

    private String getTestEntityName(String entity) {
        switch(entity) {
            case "Parcela": return getString(R.string.entity_parcela);
            case "Reserva": return getString(R.string.entity_reserva);
            case "ParcelaReservada": return getString(R.string.entity_parcela_reservada);
            default: return "";
        }
    }
}
