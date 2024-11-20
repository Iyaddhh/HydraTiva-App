package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.MateriResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MateriService {
    @GET("materi")
    Call<List<MateriResponse>> getMateriShow();
}
