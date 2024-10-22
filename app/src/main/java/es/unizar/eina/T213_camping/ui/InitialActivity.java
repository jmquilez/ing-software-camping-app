package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.HomeActivity; // Import HomeActivity

public class InitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Button entrarButton = findViewById(R.id.entrar_button);

        entrarButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the initial activity
        });

        // Hide back and home buttons on initial screen
        setButtonVisibility("back", false);
        setButtonVisibility("home", false);

        ImageView logoImage = findViewById(R.id.logo_image);
        logoImage.setImageResource(R.drawable.camping_logo);
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
