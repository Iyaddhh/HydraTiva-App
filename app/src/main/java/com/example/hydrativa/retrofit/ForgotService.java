package com.example.hydrativa.retrofit;// ForgotPasswordApi.java
import com.example.hydrativa.models.ForgotPasswordResponse;
import com.example.hydrativa.models.ForgotPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ForgotService {
    @POST("reset-password-link")
    @Headers("Content-Type: application/json")
    Call<ForgotPasswordResponse> sendResetLink(@Body ForgotPasswordRequest request);
}