package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hydrativa.retrofit.LogoutService;
import com.example.hydrativa.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutFunction extends AppCompatActivity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);  // Load the layout containing logoutButton

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        // Pass the current context (LogoutFunction) to the RetrofitClient
        LogoutService apiService = RetrofitClient.getRetrofitInstance(LogoutFunction.this).create(LogoutService.class);
        Call<Void> call = apiService.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LogoutFunction.this, "Logout successful", Toast.LENGTH_SHORT).show();
                    // Redirect to the login layout
                    Intent intent = new Intent(LogoutFunction.this, Login.class);
                    startActivity(intent);
                    finish(); // Close LogoutFunction
                } else {
                    Toast.makeText(LogoutFunction.this, "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LogoutFunction.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
