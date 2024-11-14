package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.EditProfileRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProfileService {
    @Multipart
    @POST("me/update/mobile")
    Call<Void> updateProfile(
            @Part("username") RequestBody username,
            @Part("jenis_kelamin") RequestBody jenis_kelamin,
            @Part("name") RequestBody name,
            @Part("telp") RequestBody telp,
            @Part MultipartBody.Part image
    );
}
