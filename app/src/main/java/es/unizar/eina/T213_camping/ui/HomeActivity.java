package es.unizar.eina.T213_camping.ui;

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
            "Pruebas Unitarias",
            "Prueba de Volumen",
            "Prueba de Sobrecarga"
        };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Seleccione tipo de prueba")
               .setItems(options, (dialog, which) -> {
                   switch (which) {
                       case 0:
                           // TODO: Implementar pruebas unitarias
                           DialogUtils.showErrorDialog(this, "Pruebas unitarias aún no implementadas");
                           break;
                       case 1:
                           DialogUtils.showConfirmationDialog(this,
                               "Prueba de Volumen",
                               "¿Desea ejecutar la prueba de volumen? Esta operación puede tardar varios minutos.",
                               R.drawable.ic_warning,
                               () -> TestUtils.volumeTest(this, parcelaViewModel, reservaViewModel));
                           break;
                       case 2:
                           DialogUtils.showConfirmationDialog(this,
                               "Prueba de Sobrecarga",
                               "¿Desea ejecutar la prueba de sobrecarga? Esta operación puede causar inestabilidad en la aplicación.",
                               R.drawable.ic_warning,
                               () -> TestUtils.stressTest(this, parcelaViewModel));
                           break;
                   }
               });
        builder.create().show();
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
