package com.example.hydrativa.retrofit;

import com.example.hydrativa.models.Kebun;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
            @Part("lokasi_kebun") RequestBody lokasiKebun,
            @Part("alat_id") RequestBody idAlat,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("kebun/{id}")
    Call<Void> updateKebun(
            @Path("id") int kebun_id,
            @Part("nama_kebun") RequestBody namaKebun,
            @Part("luas_lahan") RequestBody luasLahan,
            @Part("lokasi_kebun") RequestBody lokasiKebun,
            @Part("alat_id") RequestBody idAlat,
            @Part MultipartBody.Part image
    );

    @GET("kebun/list")
    Call<List<Kebun>> getKebun();

    @GET("kebun/detail/{id}")
    Call<Kebun> getKebunDetail(@Path("id") int id);

    @DELETE("kebun/{id}")
    Call<Void> deleteKebun(@Path("id") int kebun_id);
}