package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hydrativa.models.ForgotRequest;
import com.example.hydrativa.models.RegisterRequest;
import com.example.hydrativa.models.RegisterResponse;
import com.example.hydrativa.retrofit.RegisterService;
import com.example.hydrativa.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText register_user, register_name, pass_user, register_email, NoTelepon;
    RadioGroup radioGroup;
    String jenis_kelamin;
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

        radioGroup = findViewById(R.id.radioGroup1);
        registerService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(RegisterService.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Find the selected RadioButton by ID
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    // Get the text of the selected RadioButton and store it in jenis_kelamin
                    jenis_kelamin = selectedRadioButton.getText().toString();

                    // Show a toast (optional) to confirm the selected value
                    Toast.makeText(MainActivity.this, "Selected Gender: " + jenis_kelamin, Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                String kelamin = jenis_kelamin;
                registerUser(username, email, password, nama, telp, kelamin);
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

    private void registerUser(String username, String email, String password, String nama, String telp, String kelamin) {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email, nama, telp, kelamin);

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
                Intent intent = new Intent(MainActivity.this, Login.class);
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

