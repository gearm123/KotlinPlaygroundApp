package com.test.funfactsapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiAdapter {
    var okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .build()

    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://uselessfacts.jsph.pl")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}