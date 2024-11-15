package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hydrativa.adapters.HistoryPenyiramanAdapter;
import com.example.hydrativa.models.HistoryPenyiraman;
import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detail_watering extends AppCompatActivity {

    private TextView tvMoisture, tvPH, tvStatus, tvKondisi, tvTitle, tvLocation;
    private KebunService kebunService;
    private ImageView gambarKebun;
    private RecyclerView recyclerView;
    private HistoryPenyiramanAdapter historyAdapter;
    private List<HistoryPenyiraman> historyList;
    private int kebunId;
    String imageUrl;
    TextView linkHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_watering);

        ImageView arrowRight3 = findViewById(R.id.arrowRight3);
        arrowRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_watering.this, edit_kebun.class);
                intent.putExtra("kebun_id", kebunId);
                startActivity(intent);
            }
        });

        kebunService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(KebunService.class);

        gambarKebun = findViewById(R.id.kebunImage1);
        tvTitle = findViewById(R.id.Title);
        tvLocation = findViewById(R.id.Location);
        tvMoisture = findViewById(R.id.Moisture);
        tvPH = findViewById(R.id.PH);
        tvKondisi = findViewById(R.id.Kondisi);
        tvStatus = findViewById(R.id.Status);

        kebunId = getIntent().getIntExtra("KEBUN_ID", -1);

        Call<Kebun> call = kebunService.getKebunDetail(kebunId);
        call.enqueue(new Callback<Kebun>() {
            @Override
            public void onResponse(Call<Kebun> call, Response<Kebun> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Kebun kebunDetail = response.body();
                    tvTitle.setText(kebunDetail.getNama_kebun());
                    tvLocation.setText(kebunDetail.getLokasi_kebun());
                    tvMoisture.setText("Moisture: " + kebunDetail.getMoisture());
                    tvPH.setText(kebunDetail.getpH() + " pH");
                    tvKondisi.setText("Status: " + kebunDetail.getStatus());

                    imageUrl = "http://10.0.2.2:8000/storage/" + kebunDetail.getGambar();
                    Glide.with(detail_watering.this)
                            .load(imageUrl)
                            .error(R.drawable.tehdia)
                            .into(gambarKebun);

                    getHistoryPenyiraman(kebunId);
                } else {
                    Toast.makeText(detail_watering.this, "Gagal memuat data kebun", Toast.LENGTH_SHORT).show();
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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getHistoryPenyiraman(int kebunId) {
        Call<List<HistoryPenyiraman>> callHistory = kebunService.getHistori(kebunId);
        callHistory.enqueue(new Callback<List<HistoryPenyiraman>>() {
            @Override
            public void onResponse(Call<List<HistoryPenyiraman>> call, Response<List<HistoryPenyiraman>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    historyList = response.body();
                    historyAdapter = new HistoryPenyiramanAdapter(historyList);
                    recyclerView.setAdapter(historyAdapter);

                    // Get the latest status from the history list
                    if (!historyList.isEmpty()) {
                        HistoryPenyiraman latestHistory = historyList.get(historyList.size() - 1); // Assuming the latest entry is the first in the list
                        String latestStatus = latestHistory.getSoilCondition();
                        Log.d("StatusCheck", "Latest Soil Condition: " + latestStatus);
                        // Set the status message based on the latest history status
                        if ("kering".equalsIgnoreCase(latestStatus)) {
                            tvStatus.setText("Sedang disiram");
                        } else if ("normal".equalsIgnoreCase(latestStatus) || "basah".equalsIgnoreCase(latestStatus)) {
                            tvStatus.setText("Sudah disiram");
                        } else {
                            tvStatus.setText("-");
                        }
                    }
                } else {
                    Toast.makeText(detail_watering.this, "Gagal memuat data history penyiraman", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryPenyiraman>> call, Throwable throwable) {
                Toast.makeText(detail_watering.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });

        linkHistory = findViewById(R.id.selengkapnya);
        linkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(detail_watering.this, History.class);
                startActivity(i);
            }
        });
    }
}
