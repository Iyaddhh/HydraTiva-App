package com.example.hydrativa.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ForgotService {

    @POST("reset-password-link")
    Call<ResponseBody> sendResetLink(@Body RequestBody email);
}