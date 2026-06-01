package com.example.coursework.domain.usecase

import com.example.coursework.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String, password: String): Result<Unit> =
        repository.login(phoneNumber, password)
}
