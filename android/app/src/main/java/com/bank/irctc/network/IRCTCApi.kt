package com.bank.irctc.network

import com.bank.irctc.models.Booking
import com.bank.irctc.models.LoginRequest
import com.bank.irctc.models.LoginResponse
import com.bank.irctc.models.Station
import com.bank.irctc.models.Train
import com.bank.irctc.models.TrainSchedule
import com.bank.irctc.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("api/schedules/search")
    suspend fun searchSchedules(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("date") date: String
    ): Response<List<TrainSchedule>>

    @GET("api/users/{id}")
    suspend fun getUserProfile(@Path("id") id: Long): Response<User>

    @POST("api/bookings")
    suspend fun bookTicket(@Body booking: Booking): Response<Booking>

    @GET("api/bookings/{id}")
    suspend fun getBookingById(@Path("id") id: Long): Response<Booking>

    @GET("api/bookings/user/{userId}")
    suspend fun getMyBookings(@Path("userId") userId: Long): Response<List<Booking>>
}
