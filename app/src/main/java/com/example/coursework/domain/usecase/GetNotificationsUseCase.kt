package com.example.coursework.domain.usecase

import com.example.coursework.domain.repository.NotificationRepository

class GetNotificationsUseCase(private val repo: NotificationRepository) {
    suspend operator fun invoke(phoneNumber: String) = repo.getNotifications(phoneNumber)
}
