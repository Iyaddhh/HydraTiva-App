package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hydrativa.retrofit.LoginService;
import com.example.hydrativa.retrofit.RegisterService;
import com.example.hydrativa.retrofit.RetrofitClient;

public class MainActivity extends AppCompatActivity {

    EditText userEditText, nameEditText, passEditText, EmailAdd, NoTelepon;
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

        registerService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(registerService.class);


        EditText register_user = findViewById(R.id.userlayout);
        EditText register_name = findViewById(R.id.nama);
        EditText pass_user = findViewById(R.id.password);
        EditText register_email = findViewById(R.id.email);
        EditText NoTelepon = findViewById(R.id.telepon);

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
}