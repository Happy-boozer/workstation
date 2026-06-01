package com.example.coursework.domain.usecase

import com.example.coursework.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, phoneNumber: String, password: String): Result<Unit> =
        repository.register(name, phoneNumber, password)
}
