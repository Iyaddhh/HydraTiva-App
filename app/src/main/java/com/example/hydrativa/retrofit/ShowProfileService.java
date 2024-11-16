package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.ShowProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShowProfileService {
    @GET("me")
    Call<ShowProfileResponse> getUser(@Path("id") int id);
}