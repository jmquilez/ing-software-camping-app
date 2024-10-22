package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.ui.parcelas.listado.ParcelFeedActivity;
import es.unizar.eina.T213_camping.ui.reservas.listado.ReservationFeedActivity;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Set button visibility
        setButtonVisibility("back", false);
        setButtonVisibility("home", false);
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
