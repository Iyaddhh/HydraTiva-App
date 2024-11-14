package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.EditProfileRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileUpdateService {
    @POST("/me/update")
    Call<ResponseBody> updateProfile(@Body EditProfileRequest request);
}
