package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.LinkRequest;
import com.example.hydrativa.models.LinkResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ForgotService {

    @POST("reset-password-link")
    Call<LinkResponse> sendResetLink(@Body LinkRequest request);
}