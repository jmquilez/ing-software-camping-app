package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.unizar.eina.T213_camping.R;

/**
 * Actividad base que proporciona funcionalidad común para todas las actividades de la aplicación.
 * Implementa una toolbar personalizada con título y botones de navegación.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView toolbarTitle;
    protected ImageButton backButton;
    protected ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        // System.out.println("CREATING BASE ACTIVITY");

        initializeToolbar();
    }

    /**
     * Inicializa la toolbar y sus componentes.
     * Configura el título y los botones de navegación.
     */
    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbarTitle);
        backButton = findViewById(R.id.backButton);
        homeButton = findViewById(R.id.homeButton);

        // TODO: deprecated
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // TODO: revise home button
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        setToolbarTitle(getToolbarTitle());
    }

    /**
     * Obtiene el ID del layout a inflar para esta actividad.
     * @return ID del recurso de layout
     */
    protected abstract int getLayoutResourceId();

    /**
     * Obtiene el título a mostrar en la toolbar.
     * @return Título de la actividad
     */
    protected abstract String getToolbarTitle();

    /**
     * Establece el título en la toolbar.
     * @param title Título a mostrar
     */
    protected void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    /**
     * Controla la visibilidad de los botones de navegación.
     * @param buttonType Tipo de botón ("back" o "home")
     * @param show true para mostrar, false para ocultar
     */
    protected void setButtonVisibility(String buttonType, boolean show) {
        ImageButton button = null;
        if ("back".equals(buttonType)) {
            button = backButton;
        } else if ("home".equals(buttonType)) {
            button = homeButton;
        }

        if (button != null) {
            button.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
