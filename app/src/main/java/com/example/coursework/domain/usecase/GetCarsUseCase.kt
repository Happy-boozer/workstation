package com.example.coursework.domain.usecase

import com.example.coursework.domain.model.Car
import com.example.coursework.domain.repository.CarRepository

class GetCarsUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(phoneNumber: String): Result<List<Car>> =
        repository.getCars(phoneNumber)
}
