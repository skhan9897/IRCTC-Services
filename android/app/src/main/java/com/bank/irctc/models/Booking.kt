package com.bank.irctc.models

data class Booking(
    val id: Long? = null,
    val userId: Long,
    val trainId: Long,
    val pnr: String? = null,
    val train: Train? = null,
    val journeyDate: String,
    val fromStation: String,
    val toStation: String,
    val classType: String,
    val totalFare: Double,
    val bookingStatus: String? = null,
    val passengers: List<Passenger>
)

data class Passenger(
    val name: String,
    val age: Int,
    val gender: String
)
