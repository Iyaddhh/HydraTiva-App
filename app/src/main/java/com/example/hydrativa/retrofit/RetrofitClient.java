package com.example.hydrativa.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.hydrativa.models.LoginResponseDeserializer;
import com.example.hydrativa.models.LoginResponse;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8000/api/";

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    // Configure Gson with the custom deserializer
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LoginResponse.class, new LoginResponseDeserializer())
                            .setLenient()
                            .create();

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new AuthInterceptor(context))
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static class AuthInterceptor implements Interceptor {
        private final SharedPreferences sharedPreferences;

        public AuthInterceptor(Context context) {
            this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = sharedPreferences.getString("auth_token", null);

            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();

            if (token != null) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            Request newRequest = requestBuilder.build();
            return chain.proceed(newRequest);
        }
    }
}

