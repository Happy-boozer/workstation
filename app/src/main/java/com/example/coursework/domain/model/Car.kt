package com.example.coursework.domain.model

data class Car(
    val id: Int = 0,
    val userId: Int,
    val sign: String,
    val vin: String,
    val name: String,
    val status: String? = null
)
