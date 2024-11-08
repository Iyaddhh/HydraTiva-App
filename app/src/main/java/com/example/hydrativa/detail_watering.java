package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.hydrativa.models.DetailKebunResponse;
import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.models.KebunResponse;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detail_watering extends AppCompatActivity {

    private TextView tvMoisture, tvPH, tvStatus, tvTanggal, tvTitle, tvLocation;
    private KebunService kebunService;
    private Kebun kebunDetail;
    private int kebunId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_watering);

        kebunService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(KebunService.class);

        tvTitle = findViewById(R.id.Title);
        tvLocation = findViewById(R.id.Location);
        tvMoisture = findViewById(R.id.Moisture);
        tvPH = findViewById(R.id.PH);
        tvStatus = findViewById(R.id.Status);
        tvTanggal = findViewById(R.id.TanggalKebun);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        String currentDate = formatter.format(date);


        kebunId = getIntent().getIntExtra("KEBUN_ID", -1);
        Call<Kebun> call = kebunService.getKebunDetail(kebunId);
        call.enqueue(new Callback<Kebun>() {
            @Override
            public void onResponse(Call<Kebun> call, Response<Kebun> response) {
                if (response.isSuccessful() && response.body() != null) {
                    kebunDetail = response.body();

                    Log.d("WKWK", "bang: " + response);
                    tvTitle.setText(kebunDetail.getNama_kebun());
                    tvLocation.setText(kebunDetail.getLokasi_kebun());
                    tvMoisture.setText("Moisture: " + kebunDetail.getMoisture());
                    tvPH.setText(kebunDetail.getpH() + " pH");
                    tvStatus.setText("Status: " + kebunDetail.getStatus());
                    tvTanggal.setText(currentDate);
                } else {
                    Toast.makeText(detail_watering.this, "Gagal memuat data alat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Kebun> call, Throwable throwable) {
                Toast.makeText(detail_watering.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);

        ImageView arrowRight3 = findViewById(R.id.arrowRight3);
        arrowRight3.setOnClickListener(v -> {
            Intent intent = new Intent(detail_watering.this, edit_kebun.class);
            intent.putExtra("KEBUN_ID", kebunId);  // Pass kebunId if needed in the edit activity
            startActivity(intent);
        });
    }
}