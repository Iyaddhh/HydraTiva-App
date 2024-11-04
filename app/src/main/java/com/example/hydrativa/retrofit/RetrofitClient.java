package com.example.hydrativa.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8000/api/";

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new AuthInterceptor(context))  // Pass context here
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static class AuthInterceptor implements Interceptor {
        private final SharedPreferences sharedPreferences;

        // Constructor that accepts a context
        public AuthInterceptor(Context context) {
            // Initialize SharedPreferences here
            this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            // Get the token from SharedPreferences
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
