package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hydrativa.models.DetailKebun;
import com.example.hydrativa.models.DetailKebunResponse;
import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.models.KebunResponse;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detail_watering extends AppCompatActivity {

    private TextView tvMoisture, tvPH, tvStatus, tvTanggalPenyiraman;
    private KebunService kebunService;
    private int kebunId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_watering);

        // Inisialisasi TextView
        tvMoisture = findViewById(R.id.Moisture);
        tvPH = findViewById(R.id.PH);
        tvStatus = findViewById(R.id.Status);
        tvTanggalPenyiraman = findViewById(R.id.TanggalPenyiraman);

        // Mendapatkan kebunId dari Intent
        kebunId = getIntent().getIntExtra("KEBUN_ID", -1);

        // Inisialisasi KebunService
        kebunService = RetrofitClient.getRetrofitInstance().create(KebunService.class);

        // Memanggil API untuk mendapatkan detail alat
        getKebunDetail(kebunId);
    }

    private void getKebunDetail(int kebunId) {
        Call<List<Kebun>> call = kebunService.getKebunDetail();
        call.enqueue(new Callback<KebunResponse>() {
            @Override
            public void onResponse(Call<DetailKebunResponse> call, Response<DetailKebunResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailKebun kebunDetail = response.body().getKebunDetail();

                    // Menampilkan data alat di TextView
                    tvMoisture.setText("Moisture: " + kebunDetail.getMoisture());
                    tvPH.setText("pH: " + kebunDetail.getPh());
                    tvStatus.setText("Status: " + kebunDetail.getStatus());
                    tvTanggalPenyiraman.setText("Tanggal Penyiraman: " + kebunDetail.getTanggalPenyiraman());
                } else {
                    Toast.makeText(detail_watering.this, "Gagal memuat data alat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailKebunResponse> call, Throwable t) {
                Toast.makeText(detail_watering.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}