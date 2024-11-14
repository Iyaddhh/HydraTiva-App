package com.example.hydrativa.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProfileService {
    @Multipart
    @POST("me/update")
    Call<Void> updateProfile(
            @Part("name") RequestBody name,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("telp") RequestBody telp,
            @Part MultipartBody.Part image
    );
}
