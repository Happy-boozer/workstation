package com.example.coursework.domain.usecase

import com.example.coursework.domain.repository.NotificationRepository

class MarkNotificationReadUseCase(private val repo: NotificationRepository) {
    suspend operator fun invoke(id: Int) = repo.markRead(id)
}
