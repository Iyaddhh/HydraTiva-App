package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hydrativa.models.MateriResponse;
import com.example.hydrativa.retrofit.MateriService;
import com.example.hydrativa.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Materi extends AppCompatActivity {

    private TextView judulMateri, deskripsiMateri;
    private ImageView imageMateri;
    private MateriService materiService;
    private int materiId; // Menyimpan ID materi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        // Menangkap ID materi dari Intent
        materiId = getIntent().getIntExtra("materi_id", -1);  // Default -1 jika tidak ada ID

        // Inisialisasi tampilan
        judulMateri = findViewById(R.id.judulMateri);
        deskripsiMateri = findViewById(R.id.deskripsiMateri);
        imageMateri = findViewById(R.id.imageMateri);

        // Inisialisasi Retrofit Service
        materiService = RetrofitClient.getRetrofitInstance(Materi.this).create(MateriService.class);

        // Pastikan ID valid sebelum memanggil API
        if (materiId != -1) {
            fetchMateriDetail(materiId);
        } else {
            Toast.makeText(this, "ID Materi tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    // Memanggil API untuk mengambil detail materi berdasarkan ID
    private void fetchMateriDetail(int materiId) {
        Call<MateriResponse> call = materiService.getMateriDetail(materiId);

        call.enqueue(new Callback<MateriResponse>() {
            @Override
            public void onResponse(Call<MateriResponse> call, Response<MateriResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MateriResponse materi = response.body();

                    // Menampilkan detail materi
                    judulMateri.setText(materi.getJudul());
                    deskripsiMateri.setText(materi.getDeskripsi());
                    Glide.with(Materi.this)
                            .load(materi.getGambar())  // Menampilkan gambar jika ada
                            .into(imageMateri);
                } else {
                    Toast.makeText(Materi.this, "Materi tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MateriResponse> call, Throwable t) {
                Toast.makeText(Materi.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
