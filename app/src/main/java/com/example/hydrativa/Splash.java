package com.example.hydrativa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    Integer t = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String authToken = sharedPref.getString("auth_token", null);

                if (authToken != null) {
                    // Auth token exists, navigate to Dashboard
                    Intent i = new Intent(Splash.this, Dashboard.class);
                    startActivity(i);
                } else {
                    // No auth token, navigate to Get Started screen
                    Intent i = new Intent(Splash.this, getstarted.class);
                    startActivity(i);
                }
                finish();
            }
        }, t);
    }
}
