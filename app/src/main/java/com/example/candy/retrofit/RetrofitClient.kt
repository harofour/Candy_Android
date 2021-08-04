package com.example.candy.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level

object RetrofitClient {
    private var retrofitClient: Retrofit? = null

    private const val baseUrl: String = "http://221.159.102.58"

    fun getClient(): Retrofit {
        if(retrofitClient == null){
            val logging = HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofitClient!!
    }
}