package com.bank.irctc.models

data class TrainSchedule(
    val id: Long,
    val train: Train,
    val journeyDate: String,
    val departureTime: String?,
    val arrivalTime: String?,
    val availableSeats: Int
)

data class Train(
    val id: Long,
    val trainNumber: String,
    val trainName: String,
    val source: String,
    val destination: String,
    val sleeperFare: Double?,
    val ac3Fare: Double?,
    val ac2Fare: Double?,
    val ac1Fare: Double?
)
