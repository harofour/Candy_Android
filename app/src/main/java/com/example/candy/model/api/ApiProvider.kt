package com.example.candy.model.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider{

    private const val Base_Url = "http://221.159.102.58/"

    fun provideChallengeApi() : ChallengeApi = getRetrofitBuild().create(ChallengeApi::class.java)
    fun provideChallegneApi_Rx() : ChallengeApi = getRetrofitBuild_Rx().create(ChallengeApi::class.java)


    private fun getRetrofitBuild() = Retrofit.Builder()
        .baseUrl(Base_Url)
        .client(getOkhttpClient())
        .addConverterFactory(getGsonConverter())
        .build()

    private fun getRetrofitBuild_Rx() = Retrofit.Builder()
            .baseUrl(Base_Url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkhttpClient())
            .build()

    private fun getGsonConverter() = GsonConverterFactory.create()

    private fun getOkhttpClient() = OkHttpClient.Builder().apply {

        //TimeOut 시간을 지정합니다.
        readTimeout(20, TimeUnit.SECONDS)
        connectTimeout(20, TimeUnit.SECONDS)
        writeTimeout(5, TimeUnit.SECONDS)

        // 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
        addInterceptor(getLoggingInterceptor())
    }.build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }



}