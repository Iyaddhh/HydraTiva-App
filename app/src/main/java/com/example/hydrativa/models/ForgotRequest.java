package com.example.hydrativa.models;
import android.content.Context;
import android.widget.Toast;

import com.example.hydrativa.retrofit.ForgotService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotRequest {
    private static final String BASE_URL = "http://127.0.0.1:8000/api"; // Update with your base URL
    private ForgotService forgotPasswordApi;

    public ForgotRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        forgotPasswordApi = retrofit.create(ForgotService.class);
    }

    public void sendResetPasswordLink(Context context, String email) {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        forgotPasswordApi.sendResetLink(request).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to send reset link", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
