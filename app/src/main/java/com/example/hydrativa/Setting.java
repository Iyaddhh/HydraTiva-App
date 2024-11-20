package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.hydrativa.models.User;
import com.example.hydrativa.models.VerificationResponse;
import com.example.hydrativa.retrofit.LogoutService;
import com.example.hydrativa.retrofit.ForgotService;
import com.example.hydrativa.retrofit.ProfileService;
import com.example.hydrativa.retrofit.RetrofitClient;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting extends AppCompatActivity {
    TextView linkForgot;
    TextView linkProfile;
    private Button btnVerifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVerifyEmail = findViewById(R.id.verifButton);
        btnVerifyEmail.setOnClickListener(v -> sendVerificationEmail());

        BottomAppBar bottomAppBar = findViewById(R.id.bottomView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        FloatingActionButton fab = findViewById(R.id.fab);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                return true;
            }
            return false;
        });
        fab.setOnClickListener(view -> {
            startActivity(new Intent(Setting.this, Watering.class));
            overridePendingTransition(0, 0);
        });
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "User");

        TextView nameText = findViewById(R.id.usernameText);
        nameText.setText(name);

        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });


        linkForgot = findViewById(R.id.forgotLink);
        linkForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Setting.this, Forgot.class);
                startActivity(i);
            }
        });

        linkProfile = findViewById(R.id.profileupdateLink);
        linkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Setting.this, Profile.class);
                startActivity(i);
            }
        });

        // Mengambil profil pengguna dari API
        ProfileService profileService = RetrofitClient.getRetrofitInstance(Setting.this).create(ProfileService.class);
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
                    Glide.with(Setting.this)
                            .load(imageUrl)
                            .circleCrop()
                            .error(R.drawable.tehdia)
                            .into(profileImageView);

                } else {
                    Toast.makeText(Setting.this, "Gagal memuat profil pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(Setting.this, "Error: " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", "Error occurred: " + throwable);
            }
        });
    }

    private void performLogout() {
        // Pass the current context (LogoutFunction) to the RetrofitClient
        LogoutService apiService = RetrofitClient.getRetrofitInstance(Setting.this).create(LogoutService.class);
        Call<Void> call = apiService.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Setting.this, "Logout berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Setting.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Setting.this, "Logout gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Setting.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationEmail() {
        ForgotService authService = RetrofitClient.getRetrofitInstance(this).create(ForgotService.class);

        authService.sendVerificationEmail().enqueue(new Callback<VerificationResponse>() {
            @Override
            public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    Toast.makeText(Setting.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Setting.this, "Gagal mengirim link verifikasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerificationResponse> call, Throwable t) {
                Toast.makeText(Setting.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}