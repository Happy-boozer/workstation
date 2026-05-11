package com.example.coursework.data.DTO

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: Int,
    val sighn: String,
    val vin: String,
    val name: String,
    val status: String
)