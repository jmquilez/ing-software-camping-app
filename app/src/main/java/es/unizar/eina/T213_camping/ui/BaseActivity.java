package es.unizar.eina.T213_camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.unizar.eina.T213_camping.R;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbarTitle = findViewById(R.id.toolbarTitle);
        backButton = findViewById(R.id.backButton);
        homeButton = findViewById(R.id.homeButton);

        // TODO: deprecated
        backButton.setOnClickListener(v -> onBackPressed());

        // TODO: revise home button
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        setToolbarTitle(getToolbarTitle());
    }

    protected abstract int getLayoutResourceId();

    protected abstract String getToolbarTitle();

    protected void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED); // Set the result to canceled
        super.onBackPressed(); // Call the default implementation
    }
}
