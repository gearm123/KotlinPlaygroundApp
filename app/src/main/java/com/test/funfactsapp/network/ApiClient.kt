package com.test.funfactsapp.network

import com.test.funfactsapp.db.FunFact
import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {
    @GET("/api/v2/facts/random")
    suspend fun getRandomFact(): Response<FunFact>
}