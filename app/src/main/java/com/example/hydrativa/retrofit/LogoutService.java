package com.example.hydrativa.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LogoutService {
    @GET("logout")
    Call<Void> logout();
}
