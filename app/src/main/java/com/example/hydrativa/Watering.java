package com.example.hydrativa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zerobranch.layout.SwipeLayout;

public class Watering extends AppCompatActivity {

    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watering);

        // Setup for Edge-to-Edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup Button
        addButton = findViewById(R.id.addKebunButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(Watering.this, tambah_kebun.class);
                startActivity(add);
            }
        });

        // Menambahkan OnClickListener ke ConstraintLayout di dalam SwipeLayout
        View contentLayout = findViewById(R.id.drag_item);
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Watering.this, detail_watering.class); // Ganti dengan Activity tujuan Anda
                startActivity(intent);
            }
        });

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setSelectedItemId(R.id.nav_watering);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_watering) {
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(getApplicationContext(), Setting.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
