package com.example.candy.myPage

import com.example.candy.utils.API
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object CandyAPI {
    private var instance: Retrofit? = null
    fun getClient() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(provideOkHttpClient(AppInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ) : OkHttpClient = OkHttpClient.Builder()
        .run{
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response =
            with(chain){
                val newRequest = request().newBuilder()
                    .addHeader("api_key","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1NUVURFTlQiXSwiaXNzIjoic29jaWFsX3NlcnZlciIsIm5hbWUiOiLtmY3quLjrj5kiLCJpYXQiOjE2MjkxMDY4MjQsInVzZXJLZXkiOjEsImVtYWlsIjoiaGlAbmF2ZXIuY29tIn0.O-RKD0qy5tL63oxEomF0iAeGfxd9hsM9hsmKNVgH3CiaE5PvB1KxzT8LAPCIX2_IcZ2B-GazBBqWbhlRatEwSQ")
                    .build()

                proceed(newRequest)
            }
    }
}