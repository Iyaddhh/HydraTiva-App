package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.hydrativa.models.User;
import com.example.hydrativa.retrofit.ProfileService;
import com.example.hydrativa.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private TextView linkProfile;
    private TextView username, name, telp, jenis_kelamin;
    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView settingLink = findViewById(R.id.settingLink);

        settingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Setting.class);
                startActivity(intent);
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.input_username);
        name = findViewById(R.id.input_name);
        telp = findViewById(R.id.input_phone);
        jenis_kelamin = findViewById(R.id.input_jenis_kelamin);
        linkProfile = findViewById(R.id.suntingButton); // Inisialisasi linkProfile

        loadUserProfile();

        profile_image.setClickable(false);
        profile_image.setFocusable(false);

        username.setFocusable(false);
        username.setClickable(false);

        name.setFocusable(false);
        name.setClickable(false);

        telp.setFocusable(false);
        telp.setClickable(false);

        jenis_kelamin.setFocusable(false);
        jenis_kelamin.setClickable(false);

        linkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, EditProfile.class);
                startActivity(i);
            }
        });
    }

    private void loadUserProfile() {
        ProfileService userService = RetrofitClient.getRetrofitInstance(Profile.this).create(ProfileService.class);
        Call<User> call = userService.getProfile();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    username.setText(user.getUsername());
                    name.setText(user.getName());
                    telp.setText(user.getTelp());
                    jenis_kelamin.setText(user.getJenis_kelamin());

                    if (user.getGambar() != null && !user.getGambar().isEmpty()) {
                        User userProfile = response.body();
                        String imageUrl = userProfile.getGambar();

                        if (imageUrl != null && imageUrl.startsWith("http://")) {
                            imageUrl = "https://" + imageUrl.substring(7); // Mengganti http:// menjadi https://
                        }

                        Glide.with(Profile.this)
                                .load(imageUrl)
                                .into(profile_image);
                    }
                } else {
                    Toast.makeText(Profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        linkProfile = findViewById(R.id.suntingButton);
        linkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, EditProfile.class);
                startActivity(i);
            }
        });
    }
}