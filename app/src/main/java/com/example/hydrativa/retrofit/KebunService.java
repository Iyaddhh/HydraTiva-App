package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.models.RegisterRequest;
import com.example.hydrativa.models.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface KebunService {
    @GET("kebun/list")
    Call<List<Kebun>> getKebun();

}
