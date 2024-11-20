package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hydrativa.adapters.HistoryPenyiramanAdapter;
import com.example.hydrativa.models.HistoryPenyiraman;
import com.example.hydrativa.models.User;
import com.example.hydrativa.retrofit.KebunService;
import com.example.hydrativa.retrofit.ProfileService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryPenyiramanAdapter adapter;
    private List<HistoryPenyiraman> historyList;
    private List<HistoryPenyiraman> filteredHistoryList;
    private Spinner spinnerMonth;
    private List<String> monthsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int kebunId = getIntent().getIntExtra("kebun_id", 1); // -1 sebagai default jika tidak ada kebun_id

        // Inisialisasi data bulan
        monthsList = new ArrayList<>();

        // Mengatur Spinner dengan bulan
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerMonth.setAdapter(spinnerAdapter);

        // Menambahkan listener untuk spinner
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                // Mendapatkan bulan yang dipilih
                String selectedMonthYear = (String) parentView.getItemAtPosition(position);
                filterDataByMonth(selectedMonthYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Tidak melakukan apa-apa jika tidak ada yang dipilih
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Mengambil profil pengguna dari API
        ProfileService profileService = RetrofitClient.getRetrofitInstance(History.this).create(ProfileService.class);
        Call<User> profileCall = profileService.getProfile();
        profileCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userProfile = response.body();
                    String imageUrl = userProfile.getGambar();

                    if (imageUrl != null && imageUrl.startsWith("http://")) {
                        imageUrl = "https://" + imageUrl.substring(7); // Mengganti http:// menjadi https://
                    }

                    ImageView profileImageView = findViewById(R.id.circleImage);

                    // Menggunakan Glide untuk memuat gambar profil
                    Glide.with(History.this)
                            .load(imageUrl)
                            .circleCrop()
                            .error(R.drawable.tehdia)
                            .into(profileImageView);

                } else {
                    Toast.makeText(History.this, "Gagal memuat profil pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(History.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });

        KebunService apiService = RetrofitClient.getRetrofitInstance(History.this).create(KebunService.class);
        Call<List<HistoryPenyiraman>> call = apiService.getHistori(kebunId);
        call.enqueue(new Callback<List<HistoryPenyiraman>>() {
            @Override
            public void onResponse(Call<List<HistoryPenyiraman>> call, Response<List<HistoryPenyiraman>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    historyList = response.body();
                    filteredHistoryList = new ArrayList<>(historyList);
                    adapter = new HistoryPenyiramanAdapter(filteredHistoryList);
                    recyclerView.setAdapter(adapter);

                    // Dapatkan bulan dan tahun dari data history
                    addMonthsToSpinner(historyList);
                } else {
                    Toast.makeText(History.this, "Tidak ada data histori", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryPenyiraman>> call, Throwable t) {
                Toast.makeText(History.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fungsi untuk menambah bulan dan tahun yang ditemukan dalam data history ke dalam spinner
    private void addMonthsToSpinner(List<HistoryPenyiraman> historyList) {
        // Format tanggal untuk mendapatkan bulan dan tahun
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // Format sesuai data Anda
        for (HistoryPenyiraman history : historyList) {
            try {
                String dateStr = history.getDate();
                Log.d("History", "Date from history: " + dateStr);
                // Parsing waktu menjadi objek Date
                Date date = sdf.parse(history.getDate());
                // Mengubah string waktu menjadi Date
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy"); // Format bulan dan tahun
                String monthYear = monthYearFormat.format(date); // Mendapatkan bulan dan tahun dalam format "Maret 2023"

                // Pastikan bulan dan tahun unik sebelum ditambahkan ke list
                if (!monthsList.contains(monthYear)) {
                    monthsList.add(monthYear);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Jika parsing gagal, lewati item ini
            }
        }

        Log.d("History", "Months list: " + monthsList);

        // Update spinner setelah bulan dan tahun ditambahkan
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(spinnerAdapter);
    }

    // Fungsi untuk memfilter data berdasarkan bulan yang dipilih
    private void filterDataByMonth(String selectedMonthYear) {
        if (selectedMonthYear != null && !selectedMonthYear.isEmpty()) {
            // Format tanggal untuk memisahkan bulan dan tahun
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            Map<String, HistoryPenyiraman> dateHistoryMap = new HashMap<>();

            // Mengelompokkan data berdasarkan tanggal dan menyimpan data terbaru
            for (HistoryPenyiraman history : historyList) {
                try {
                    Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(history.getDate());
                    SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
                    String monthYear = monthYearFormat.format(date);

                    // Memastikan bulan yang dipilih cocok
                    if (monthYear.equals(selectedMonthYear)) {
                        // Mendapatkan tanggal dari history (format hanya tanggal)
                        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String day = dayFormat.format(date);

                        // Jika tanggal belum ada, atau jika data ini lebih terbaru, ganti data
                        if (!dateHistoryMap.containsKey(day) || dateHistoryMap.get(day).getDate().compareTo(history.getDate()) < 0) {
                            dateHistoryMap.put(day, history);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Jika parsing gagal, lewati item ini
                }
            }

            // Dapatkan data terbaru yang ditemukan untuk bulan yang dipilih
            filteredHistoryList.clear();
            filteredHistoryList.addAll(dateHistoryMap.values());
            adapter.notifyDataSetChanged();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.fab);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(History.this, Watering.class);
            startActivity(intent);
        });
    }
}
