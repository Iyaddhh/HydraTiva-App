package com.example.hydrativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydrativa.models.LinkResponse;
import com.example.hydrativa.models.LinkRequest;
import com.example.hydrativa.retrofit.ForgotService;
import com.example.hydrativa.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot extends AppCompatActivity {
    private EditText input_email;
    private Button send_email;
    ImageView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotemail);

        input_email = findViewById(R.id.emailresetpassword);
        send_email = findViewById(R.id.resetpasswordbutton);

        send_email.setOnClickListener(v -> sendResetLink());
    }

    private void sendResetLink() {
        String email = input_email.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgotService apiService = RetrofitClient.getRetrofitInstance(Forgot.this).create(ForgotService.class);
        LinkRequest request = new LinkRequest(email);

        apiService.sendResetLink(request).enqueue(new Callback<LinkResponse>() {
            @Override
            public void onResponse(Call<LinkResponse> call, Response<LinkResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Forgot.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Forgot.this, "Gagal mengirim link reset", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LinkResponse> call, Throwable t) {
                Toast.makeText(Forgot.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        backToLogin = findViewById(R.id.balikkelogin);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
