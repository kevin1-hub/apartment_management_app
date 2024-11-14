package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize logo and welcome text
        ImageView logo = findViewById(R.id.logo);
        TextView welcomeText = findViewById(R.id.welcome_text);

        // Load zoom animation and apply to both views
        Animation zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        logo.startAnimation(zoomInAnimation);
        welcomeText.startAnimation(zoomInAnimation);

        // Show splash screen for 4 seconds
        new Handler().postDelayed(() -> {
            // Redirect to login activity after 4 seconds
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close splash screen activity
        }, 4000);
    }
}
