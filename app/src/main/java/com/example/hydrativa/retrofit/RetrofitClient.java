    package com.example.hydrativa.retrofit;

    import android.content.Context;
    import android.content.SharedPreferences;

    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;

    import okhttp3.Dns;
    import okhttp3.Interceptor;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.Response;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    import okhttp3.logging.HttpLoggingInterceptor;


    import java.io.IOException;
    import java.net.InetAddress;
    import java.net.UnknownHostException;
    import java.util.Arrays;
    import java.util.List;
    import java.util.concurrent.TimeUnit;

    public class RetrofitClient {
        private static Retrofit retrofit;
        private static final String BASE_URL = "https://hydrativa-hufme6esdvd6acfp.eastasia-01.azurewebsites.net/api/";

        public static Retrofit getRetrofitInstance(Context context) {
            if (retrofit == null) {
                synchronized (RetrofitClient.class) {
                    if (retrofit == null) {
                        // Create a simple Gson instance
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        OkHttpClient client = new OkHttpClient.Builder()
                                .hostnameVerifier((hostname, session) -> true)
                                .connectTimeout(30, TimeUnit.SECONDS)  // Waktu timeout saat mencoba koneksi ke server
                                .readTimeout(30, TimeUnit.SECONDS)     // Waktu timeout saat menunggu respons dari server
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .addInterceptor(new AuthInterceptor(context))
                                .addInterceptor(new HttpLoggingInterceptor()
                                        .setLevel(HttpLoggingInterceptor.Level.BODY)) // Log seluruh request dan response
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
