package com.example.coursework.domain.repository

import com.example.coursework.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(phoneNumber: String): Result<List<Notification>>
    suspend fun markRead(id: Int): Result<Unit>
}
