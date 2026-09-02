package com.bank.irctc.network

import com.bank.irctc.models.LoginRequest
import com.bank.irctc.models.LoginResponse
import com.bank.irctc.models.Station
import com.bank.irctc.models.Train
import com.bank.irctc.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IRCTCApi {

    @POST("api/auth/register")
    suspend fun register(@Body user: User): Response<User>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/stations")
    suspend fun getStations(): Response<List<Station>>

    @GET("api/trains/search")
    suspend fun searchTrains(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<List<Train>>
}
