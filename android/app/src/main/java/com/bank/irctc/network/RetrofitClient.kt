package com.bank.irctc.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Note: Use your local IP address for physical device or 10.0.2.2 for emulator
    private const val BASE_URL = "http://10.0.2.2:8080/" 

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    
    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    val api: IRCTCApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(IRCTCApi::class.java)
    }
}
