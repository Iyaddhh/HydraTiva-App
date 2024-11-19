package com.example.hydrativa;

import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.bumptech.glide.Glide;
import com.example.hydrativa.adapters.ImageAdapter;
import com.example.hydrativa.models.ImageItem;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.example.hydrativa.models.User;
import com.example.hydrativa.retrofit.ProfileService;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private ViewPager2 viewPage;
    private ImageView[] dotsImage;
    private Handler handler;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Menangani padding sistem untuk edge-to-edge view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hydrativa.vercel.app"));
                startActivity(intent);
            }
        });

        CardView cV1 = findViewById(R.id.grid1);
        CardView cV2 = findViewById(R.id.grid2);
        CardView cV3 = findViewById(R.id.grid3);
        CardView cV4 = findViewById(R.id.grid4);

        cV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Materi1.class);
                startActivity(intent);
            }
        });

        cV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, materi2.class);
                startActivity(intent);
            }
        });

        cV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, materi3.class);
                startActivity(intent);
            }
        });

        cV4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, materi4.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Menangani BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(Dashboard.this, Setting.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(Dashboard.this, Watering.class));
            overridePendingTransition(0, 0);
        });

        // Mengambil nama pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        // Menampilkan nama pengguna
        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);

        // Mengambil profil pengguna dari API
        ProfileService profileService = RetrofitClient.getRetrofitInstance(Dashboard.this).create(ProfileService.class);
        Call<User> call = profileService.getProfile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userProfile = response.body();
                    String imageUrl = userProfile.getGambar(); // Pastikan Anda memiliki path yang benar
                    ImageView profileImageView = findViewById(R.id.circleImage);

                    if (imageUrl != null && imageUrl.startsWith("http://")) {
                        imageUrl = "https://" + imageUrl.substring(7); // Mengganti http:// menjadi https://
                    }
                    // Menggunakan Glide untuk memuat gambar profil
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
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(Dashboard.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });

        // Setup ViewPager untuk gambar slider
        viewPage = findViewById(R.id.viewpager);
        LinearLayout slideDot = findViewById(R.id.slider);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);

        ArrayList<ImageItem> imageList = new ArrayList<>();
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.stevia, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.tehdia, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.daun_bg, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.stevia, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.tehdia, "https://hydrativa.vercel.app"));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.daun_bg, "https://hydrativa.vercel.app"));

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

        viewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < dotsImage.length; i++) {
                    if (i == position) {
                        dotsImage[i].setImageResource(R.drawable.active_dot);
                    } else {
                        dotsImage[i].setImageResource(R.drawable.non_active_dot);
                    }
                }
            }
        });

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

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPage != null) {
            viewPage.unregisterOnPageChangeCallback(null);
        }
    }
}
