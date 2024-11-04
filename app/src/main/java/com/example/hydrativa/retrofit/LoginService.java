package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.LoginRequest;
import com.example.hydrativa.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
