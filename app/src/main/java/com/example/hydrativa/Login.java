package com.example.hydrativa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.example.hydrativa.models.LoginRequest;
import com.example.hydrativa.models.LoginResponse;
import com.example.hydrativa.retrofit.LoginService;
import com.example.hydrativa.retrofit.RetrofitClient;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private LoginService loginService;
    EditText login_user, pass_user;
    Button btn_login;
    TextView linkRegister;
    TextView linkForgot;

    private static final String TAG = "LoginActivity"; // Tag untuk Log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(LoginService.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_user = findViewById(R.id.userlogin);
        pass_user = findViewById(R.id.passlogin);
        btn_login = findViewById(R.id.loginButton);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = login_user.getText().toString().trim();
                String password = pass_user.getText().toString().trim();// Log username
                    loginUser(username, password);
            }
        });

        linkRegister = findViewById(R.id.registerLink);
        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

        linkForgot = findViewById(R.id.forgotLink);
        linkForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Forgot.class);
                startActivity(i);
            }
        });
    }

    private void loginUser(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<LoginResponse> call = loginService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();

                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_token", token);
                    editor.putString("username", loginResponse.getUser().getUsername());
                    editor.putString("name", loginResponse.getUser().getName());
                    editor.putString("email", loginResponse.getUser().getEmail());
                    editor.apply();

                    Toast.makeText(Login.this, "Selamat Datang", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Nama atau Kata Sandi Anda Salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
