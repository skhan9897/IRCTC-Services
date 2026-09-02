package com.bank.irctc.models

data class Station(
    val id: Long,
    val stationCode: String,
    val stationName: String,
    val city: String?,
    val state: String?
)
