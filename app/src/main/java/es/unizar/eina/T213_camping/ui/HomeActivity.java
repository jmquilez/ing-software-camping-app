package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.parcelas.listado.ParcelFeedActivity;
import es.unizar.eina.T213_camping.ui.reservas.listado.ReservationFeedActivity;

/**
 * Actividad principal que sirve como punto de entrada a las principales funcionalidades.
 * Proporciona botones para acceder a la gestión de parcelas y reservas.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupNavigationButtons();
        setButtonVisibility("back", false);
        setButtonVisibility("home", false);
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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected String getToolbarTitle() {
        return "Home";
    }
}
