package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.HomeActivity; // Import HomeActivity

/**
 * Actividad inicial que muestra la pantalla de bienvenida.
 * Sirve como punto de entrada a la aplicación y muestra el logo del camping.
 */
public class InitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setupEntryButton();
        setupLogo();

        // Hide back and home buttons on initial screen
        setButtonVisibility("back", false);
        setButtonVisibility("home", false);
    }

    /**
     * Configura el botón de entrada a la aplicación.
     * Al pulsarlo, navega a la pantalla principal (HomeActivity).
     */
    private void setupEntryButton() {
        Button entrarButton = findViewById(R.id.entrar_button);
        entrarButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the initial activity
        });
    }

    /**
     * Configura el logo de la aplicación.
     */
    private void setupLogo() {
        ImageView logoImage = findViewById(R.id.logo_image);
        logoImage.setImageResource(R.drawable.camping_logo_cropped_xml);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_initial;
    }

    @Override
    protected String getToolbarTitle() {
        return "Welcome";
    }
}
