package com.example.coursework.domain.repository

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Result<Unit>
    suspend fun register(name: String, phoneNumber: String, password: String): Result<Unit>
}
