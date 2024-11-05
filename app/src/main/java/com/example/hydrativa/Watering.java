package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.hydrativa.models.KebunResponse;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.LoginService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Watering extends AppCompatActivity {

    private KebunService kebunService;
    private RecyclerView recyclerView;
    private Button addButton;

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

        recyclerView = findViewById(R.id.recyclerViewKebun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<List<Kebun>> call = kebunService.getKebun();
        call.enqueue(new Callback<List<Kebun>>() {
            @Override
            public void onResponse(Call<List<Kebun>> call, Response<List<Kebun>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Kebun> kebunList = response.body(); // Respons langsung sebagai List<Kebun>
                    Log.d("Response", "onResponse: " + kebunList);
                    KebunAdapter kebunAdapter = new KebunAdapter(Watering.this, kebunList);
                    recyclerView.setAdapter(kebunAdapter);
                } else {
                    Log.d("Error", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Kebun>> call, Throwable throwable) {
                Toast.makeText(Watering.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "API Failure: " + throwable);
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

        addButton = findViewById(R.id.addKebunButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(Watering.this, tambah_kebun.class);
                startActivity(add);
            }
        });
    }
}