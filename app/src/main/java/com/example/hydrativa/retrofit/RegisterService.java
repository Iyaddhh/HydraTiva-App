package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.RegisterResponse;
import com.example.hydrativa.models.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);
}
