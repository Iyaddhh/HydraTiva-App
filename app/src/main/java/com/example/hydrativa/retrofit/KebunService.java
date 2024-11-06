package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.models.KebunResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface KebunService {
    @Multipart
    @POST("kebun")
    Call<Void> tambahKebun(
            @Part("nama_kebun") RequestBody namaKebun,
            @Part("luas_lahan") RequestBody luasLahan,
            @Part("lokasi_kebun")    RequestBody lokasiKebun,
            @Part("kode_alat") RequestBody kodeAlat,
            @Part MultipartBody.Part image
    );

    @GET("kebun/list") // Ganti dengan endpoint yang sesuai
    Call<List<Kebun>> getKebun();

    @GET("kebun/detail/{id}")
    Call<Kebun> getKebunDetail(@Path("id") int id);

    @DELETE("kebun/{id}")
    Call<Void> deleteKebun(@Path("id") int kebun_id);
}