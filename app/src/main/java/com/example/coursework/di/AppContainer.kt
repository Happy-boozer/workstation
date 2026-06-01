package com.example.coursework.di

import android.content.Context
import com.example.coursework.data.local.SessionManager
import com.example.coursework.data.remote.ApiService
import com.example.coursework.data.repository.AuthRepositoryImpl
import com.example.coursework.data.repository.CarRepositoryImpl
import com.example.coursework.data.repository.NotificationRepositoryImpl
import com.example.coursework.domain.usecase.AddCarUseCase
import com.example.coursework.domain.usecase.GetCarsUseCase
import com.example.coursework.domain.usecase.GetNotificationsUseCase
import com.example.coursework.domain.usecase.LoginUseCase
import com.example.coursework.domain.usecase.MarkNotificationReadUseCase
import com.example.coursework.domain.usecase.RegisterUseCase

class AppContainer(context: Context) {
    val sessionManager = SessionManager(context)

    private val apiService = ApiService()
    private val authRepository = AuthRepositoryImpl(apiService)
    private val carRepository = CarRepositoryImpl(apiService)
    private val notificationRepository = NotificationRepositoryImpl(apiService)

    val loginUseCase = LoginUseCase(authRepository)
    val registerUseCase = RegisterUseCase(authRepository)
    val getCarsUseCase = GetCarsUseCase(carRepository)
    val addCarUseCase = AddCarUseCase(carRepository)
    val getNotificationsUseCase = GetNotificationsUseCase(notificationRepository)
    val markNotificationReadUseCase = MarkNotificationReadUseCase(notificationRepository)
}
