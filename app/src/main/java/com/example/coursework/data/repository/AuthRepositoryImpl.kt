package com.example.coursework.data.repository

import com.example.coursework.data.remote.ApiService
import com.example.coursework.domain.repository.AuthRepository

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): Result<Unit> = try {
        when (apiService.login(phoneNumber, password)) {
            "ok" -> Result.success(Unit)
            "notok" -> Result.failure(Exception("notok"))
            else -> Result.failure(Exception("no_connection"))
        }
    } catch (e: Exception) {
        Result.failure(Exception("no_connection", e))
    }

    override suspend fun register(name: String, phoneNumber: String, password: String): Result<Unit> = try {
        if (apiService.register(name, phoneNumber, password)) Result.success(Unit)
        else Result.failure(Exception("Ошибка регистрации"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
