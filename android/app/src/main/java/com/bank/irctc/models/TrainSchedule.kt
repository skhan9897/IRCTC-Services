package com.bank.irctc.models

data class TrainSchedule(
    val id: Long,
    val train: Train,
    val journeyDate: String,
    val departureTime: String?,
    val arrivalTime: String?,
    val availableSeats: Int
)
