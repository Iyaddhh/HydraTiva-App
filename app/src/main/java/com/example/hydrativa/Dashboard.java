package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hydrativa.adapters.ImageAdapter;
import com.example.hydrativa.adapters.MateriAdapter;
import com.example.hydrativa.models.ImageItem;
import com.example.hydrativa.models.MateriResponse;
import com.example.hydrativa.models.User;
import com.example.hydrativa.retrofit.MateriService;
import com.example.hydrativa.retrofit.ProfileService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private ViewPager2 viewPage;
    private ImageView[] dotsImage;
    private Handler handler;
    private int currentPage = 0;
    private RecyclerView recyclerView;
    private MateriAdapter materiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setup ViewPager2 untuk gambar slider
        setupImageSlider();

        // Setup RecyclerView untuk menampilkan materi
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        materiAdapter = new MateriAdapter(new ArrayList<>(), this, RetrofitClient.getRetrofitInstance(Dashboard.this).create(MateriService.class));
        recyclerView.setAdapter(materiAdapter);

        fetchMateriData();

        setupUserProfile();

        setupBottomNavigation();
    }

    private void setupImageSlider() {
        viewPage = findViewById(R.id.viewpager);
        LinearLayout slideDot = findViewById(R.id.slider);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);

        // Menambahkan gambar untuk slider
        ArrayList<ImageItem> imageList = new ArrayList<>();
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.stevia, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.tehdia, "https://hydrativa.vercel.app"));

        ImageAdapter imageAdapter = new ImageAdapter();
        viewPage.setAdapter(imageAdapter);
        imageAdapter.submitList(imageList);

        // Setup dot indicators
        dotsImage = new ImageView[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            dotsImage[i] = new ImageView(this);
            dotsImage[i].setImageResource(R.drawable.non_active_dot);
            slideDot.addView(dotsImage[i], params);
        }
        dotsImage[0].setImageResource(R.drawable.active_dot);

        // Mengatur callback untuk perubahan halaman
        viewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < dotsImage.length; i++) {
                    dotsImage[i].setImageResource(
                            i == position ? R.drawable.active_dot : R.drawable.non_active_dot
                    );
                }
            }
        });

        // Menambahkan auto-scroll untuk gambar slider
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imageList.size()) {
                    currentPage = 0;
                }
                viewPage.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void setupUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);

        ProfileService profileService = RetrofitClient.getRetrofitInstance(this).create(ProfileService.class);
        Call<User> call = profileService.getProfile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userProfile = response.body();
                    String imageUrl = userProfile.getGambar();
                    ImageView profileImageView = findViewById(R.id.circleImage);

                    // Memuat gambar profil pengguna menggunakan Glide
                    Glide.with(Dashboard.this)
                            .load(imageUrl)
                            .circleCrop()
                            .error(R.drawable.tehdia)
                            .into(profileImageView);
                } else {
                    Toast.makeText(Dashboard.this, "Gagal memuat profil pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(Dashboard.this, Watering.class));
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(Dashboard.this, Setting.class));
                return true;
            }
            return false;
        });
    }

    private void fetchMateriData() {
        MateriService materiService = RetrofitClient.getRetrofitInstance(this).create(MateriService.class);
        Call<List<MateriResponse>> call = materiService.getMateriShow();

        call.enqueue(new Callback<List<MateriResponse>>() {
            @Override
            public void onResponse(Call<List<MateriResponse>> call, Response<List<MateriResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    materiAdapter.updateList(response.body());
                } else {
                    Toast.makeText(Dashboard.this, "Gagal memuat data materi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MateriResponse>> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
