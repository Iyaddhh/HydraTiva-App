package com.example.hydrativa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hydrativa.models.UserProfile;
import com.example.hydrativa.retrofit.ProfileUpdateService;
import com.example.hydrativa.retrofit.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputUsername, inputGender, inputName, inputPhone;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure your layout name is correct

        inputUsername = findViewById(R.id.input_username);
        inputGender = findViewById(R.id.input_gender);
        inputName = findViewById(R.id.input_name);
        inputPhone = findViewById(R.id.input_phone);
        updateButton = findViewById(R.id.loginButton);

        // When the update button is clicked, perform the update
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String username = inputUsername.getText().toString();
        String gender = inputGender.getText().toString();
        String name = inputName.getText().toString();
        String phone = inputPhone.getText().toString();

        // Validate fields
        if (username.isEmpty() || gender.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the UserProfile object to send to the API
        UserProfile userProfile = new UserProfile(username, gender, name, phone);

        // Retrofit API call
        ProfileUpdateService apiService = RetrofitClient.getRetrofitInstance(this).create(ProfileUpdateService.class);
        Call<Void> call = apiService.updateProfile(userProfile);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another activity or back to the previous one
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
