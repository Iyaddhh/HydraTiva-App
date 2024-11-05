package com.example.hydrativa.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

    public interface KebunService {
        @Multipart
        @POST("kebun")
        Call<Void> tambahKebun(
                @Part("nama_kebun") RequestBody namaKebun,
                @Part("luas_lahan") RequestBody luasLahan,
                @Part("lokasi_kebun") RequestBody lokasiKebun,
                @Part("kode_alat") RequestBody kodeAlat,
                @Part MultipartBody.Part image
        );
}
