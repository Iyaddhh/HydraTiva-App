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

    private int materiId; // Menyimpan materi_id
    private TextView judulMateri, waktuMateri, deskripsiMateri, sumberMateri;
    private ImageView imageMateri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        // Inisialisasi UI
        judulMateri = findViewById(R.id.judulMateri);
        waktuMateri = findViewById(R.id.waktuMateri);
        deskripsiMateri = findViewById(R.id.deskripsiMateri);
        sumberMateri = findViewById(R.id.sumberMateri);
        imageMateri = findViewById(R.id.imageMateri);

        // Ambil ID materi yang dikirimkan melalui Intent
        materiId = getIntent().getIntExtra("materi_id", -1);  // Mengambil ID materi dari Intent
        if (materiId != -1) {
            // Ambil data materi dari API menggunakan ID yang didapat
            fetchMateriData(materiId);
        } else {
            Toast.makeText(Materi.this, "Materi ID tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMateriData(int materiId) {
        // Mengambil data materi berdasarkan materiId
        MateriService materiService = RetrofitClient.getRetrofitInstance(this).create(MateriService.class);
        Call<List<MateriResponse>> call = materiService.getMateriShow(); // Menggunakan List<MateriResponse>

        call.enqueue(new Callback<List<MateriResponse>>() {
            @Override
            public void onResponse(Call<List<MateriResponse>> call, Response<List<MateriResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MateriResponse> materiList = response.body();

                    // Misalkan kita ingin menampilkan data pertama dari list
                    MateriResponse materiResponse = materiList.get(0);  // Ambil item pertama dari list
                    // Jika ingin filter berdasarkan ID, kamu bisa menambahkan logika disini

                    // Mengikat data ke UI
                    judulMateri.setText(materiResponse.getJudul());
                    waktuMateri.setText(materiResponse.getWaktu());
                    deskripsiMateri.setText(materiResponse.getDeskripsi());
                    sumberMateri.setText(materiResponse.getSumber());

                    // Load gambar menggunakan Glide
                    Glide.with(Materi.this)
                            .load(materiResponse.getGambar())
                            .into(imageMateri);
                } else {
                    Toast.makeText(Materi.this, "Gagal memuat data materi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MateriResponse>> call, Throwable t) {
                Toast.makeText(Materi.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
