package com.example.coursework.domain.model

data class Notification(
    val id: Int,
    val userId: Int,
    val type: String,
    val title: String,
    val body: String,
    val time: String,
    val isRead: Boolean
)
