package com.example.coursework.domain.repository

import com.example.coursework.domain.model.Car

interface CarRepository {
    suspend fun getCars(phoneNumber: String): Result<List<Car>>
    suspend fun addCar(login: String, name: String, plate: String, vin: String): Result<Unit>
}
