package com.example.coursework.domain.usecase

import com.example.coursework.domain.repository.CarRepository

class AddCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(login: String, name: String, plate: String, vin: String): Result<Unit> =
        repository.addCar(login, name, plate, vin)
}
