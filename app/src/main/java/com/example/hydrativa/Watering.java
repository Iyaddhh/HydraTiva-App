package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydrativa.adapters.KebunAdapter;
import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Watering extends AppCompatActivity {

    private KebunService kebunService;
    private RecyclerView recyclerView;
    private List<Kebun> kebunList;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watering);

        kebunService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(KebunService.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addButton = findViewById(R.id.addKebunButton);
        addButton.setOnClickListener(view -> {
            Intent add = new Intent(Watering.this, tambah_kebun.class);
            startActivity(add);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<List<Kebun>> call = kebunService.getKebun();
        call.enqueue(new Callback<List<Kebun>>() {
            @Override
            public void onResponse(Call<List<Kebun>> call, Response<List<Kebun>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    kebunList = response.body();
                    Log.d("String", "onResponse: " + kebunList);
                    KebunAdapter kebunAdapter = new KebunAdapter(Watering.this, kebunList, kebunService);
                    recyclerView.setAdapter(kebunAdapter);
                } else {
                    Log.d("Response Error", "Response was not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Kebun>> call, Throwable throwable) {
                Toast.makeText(Watering.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setSelectedItemId(R.id.nav_watering);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_watering) {
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(getApplicationContext(), Setting.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);
    }
}
