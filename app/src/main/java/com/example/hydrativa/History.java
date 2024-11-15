package com.example.hydrativa;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydrativa.adapters.HistoryPenyiramanAdapter;
import com.example.hydrativa.models.HistoryPenyiraman;
import com.example.hydrativa.retrofit.KebunService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryPenyiramanAdapter adapter;
    private List<HistoryPenyiraman> historyList;

    private static final String BASE_URL = "http://10.0.2.2:8000/api/";  // URL API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Membuat instance dari KebunService
        KebunService apiService = retrofit.create(KebunService.class);

        // Mendapatkan kebun_id dari sharedPreferences atau aktivitas lain
        int kebunId = getIntent().getIntExtra("kebun_id", 1); // Misalnya, kita mendapatkan kebunId dari intent

        // Memanggil API untuk mengambil histori penyiraman berdasarkan kebunId
        Call<List<HistoryPenyiraman>> call = apiService.getHistori(kebunId);

        call.enqueue(new Callback<List<HistoryPenyiraman>>() {
            @Override
            public void onResponse(Call<List<HistoryPenyiraman>> call, Response<List<HistoryPenyiraman>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Menambahkan data ke list dan mengupdate RecyclerView
                    historyList.addAll(response.body());
                    adapter = new HistoryPenyiramanAdapter(historyList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("History", "ini" + response.message());
                    Toast.makeText(History.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryPenyiraman>> call, Throwable t) {
                Toast.makeText(History.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
