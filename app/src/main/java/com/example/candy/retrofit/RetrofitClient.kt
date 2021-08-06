package com.example.candy.retrofit

import android.util.Log
import com.example.candy.utils.isJsonArray
import com.example.candy.utils.isJsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object RetrofitClient {
    private var Tag = "RetrofitClient"
    private var retrofitClient: Retrofit? = null

    fun getClient(url: String): Retrofit {
        Log.d(Tag,"getClient() called")
        if(retrofitClient == null){
            // okhttp 인스턴스 생성
            val client = OkHttpClient.Builder()

            // 레트로핏 로그를 찍기 위해 로깅 인터셉터 추가
            val logging = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    when {
                        message.isJsonObject() -> Log.d(Tag, JSONObject(message).toString(4))
                        message.isJsonArray() -> Log.d(Tag, JSONArray(message).toString(4))
                        else -> {
                            try {
                                Log.d(Tag, JSONObject(message).toString(4))
                            }catch (e: Exception){
                                Log.d(Tag, message)
                            }
                        }
                    }
                }
            })
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logging)

            // 레트로핏 인스턴스 생성
            retrofitClient = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build()) // 위에서 생성한 클라이언트로 레트로핏 클라이언트 설정
                .build()
        }

        return retrofitClient!!
    }
}