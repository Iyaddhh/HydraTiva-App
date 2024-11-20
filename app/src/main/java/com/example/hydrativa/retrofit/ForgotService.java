package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.LinkRequest;
import com.example.hydrativa.models.LinkResponse;
import com.example.hydrativa.models.VerificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ForgotService {

    @GET("verify-email")
    Call<VerificationResponse> sendVerificationEmail();

    @POST("reset-password-link")
    Call<LinkResponse> sendResetLink(@Body LinkRequest request);
}