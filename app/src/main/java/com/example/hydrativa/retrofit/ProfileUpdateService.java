package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileUpdateService {
    @POST("me/update")
    Call<Void> updateProfile(@Body UserProfile userProfile);
}
