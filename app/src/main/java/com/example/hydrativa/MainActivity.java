package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hydrativa.models.RegisterRequest;
import com.example.hydrativa.models.RegisterResponse;
import com.example.hydrativa.retrofit.RegisterService;
import com.example.hydrativa.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText register_user, register_name, pass_user, register_email, NoTelepon;
    Button registerButton;
    TextView linkLogin;
    private RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(RegisterService.class);

        register_user = findViewById(R.id.userlayout);
        register_name = findViewById(R.id.nama);
        pass_user = findViewById(R.id.password);
        register_email = findViewById(R.id.email);
        NoTelepon = findViewById(R.id.telepon);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = register_user.getText().toString().trim();
                String email = register_email.getText().toString().trim();
                String password = pass_user.getText().toString().trim();
                String nama = register_name.getText().toString().trim();
                String telp = NoTelepon.getText().toString().trim();
                registerUser(username, email, password, nama, telp);
            }
        });

        linkLogin = findViewById(R.id.loginLink);
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });
    }

    private void registerUser(String username, String email, String password, String nama, String telp) {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email, nama, telp);

        Call<RegisterResponse> call = registerService.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    // Navigate to another activity if needed
                } else {
                    Toast.makeText(MainActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
                // Navigate to the Dashboard
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
