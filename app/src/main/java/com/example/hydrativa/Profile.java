package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hydrativa.models.ShowProfileResponse;
import com.example.hydrativa.retrofit.ShowProfileService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextInputEditText usernameData, nameData, phoneData;
    private RadioGroup radioGroup;
    private ShowProfileService apiService;
    private TextView editbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profile_image);
        usernameData = findViewById(R.id.usernamedata);
        nameData = findViewById(R.id.namedata);
        phoneData = findViewById(R.id.phonedata);
        radioGroup = findViewById(R.id.radioGroup1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ShowProfileService.class);

        int userId = 1; // Replace with actual user ID
        fetchUserData(userId);


        editbutton = findViewById(R.id.suntingButton);
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this, EditProfile.class);
                startActivity(i);
            }
        });
    }

    private void fetchUserData(int userId) {
        apiService.getUser(userId).enqueue(new Callback<ShowProfileResponse>() {
            @Override
            public void onResponse(Call<ShowProfileResponse> call, Response<ShowProfileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ShowProfileResponse user = response.body();
                        populateUserData(user);
                    } else {
                        Log.e("Profile", "Response body is null");
                    }
                } else {
                    Log.e("Profile", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ShowProfileResponse> call, Throwable t) {
                Log.e("Profile", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void populateUserData(ShowProfileResponse user) {
        Glide.with(this).load(user.getGambar()).into(profileImage);
        usernameData.setText(user.getUsername());
        nameData.setText(user.getName());
        phoneData.setText(user.getTelp());

        if ("Laki-laki".equals(user.getJenis_kelamin())) {
            radioGroup.check(R.id.radio_laki);
        } else if ("Wanita".equals(user.getJenis_kelamin())) {
            radioGroup.check(R.id.radio_wanita);
        }
    }
}