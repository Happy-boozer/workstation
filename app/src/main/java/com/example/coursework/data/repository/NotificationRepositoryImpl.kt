package com.example.coursework.data.repository

import com.example.coursework.data.remote.ApiService
import com.example.coursework.domain.model.Notification
import com.example.coursework.domain.repository.NotificationRepository
import org.json.JSONArray

class NotificationRepositoryImpl(private val apiService: ApiService) : NotificationRepository {

    override suspend fun getNotifications(phoneNumber: String): Result<List<Notification>> = try {
        val json = apiService.getNotifications(phoneNumber) ?: return Result.success(emptyList())
        val array = JSONArray(json)
        val list = (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            Notification(
                id     = obj.optInt("id"),
                userId = obj.optInt("userId"),
                type   = obj.optString("type"),
                title  = obj.optString("title"),
                body   = obj.optString("body"),
                time   = obj.optString("time"),
                isRead = obj.optBoolean("isRead")
            )
        }
        Result.success(list)
    } catch (e: java.net.ConnectException) {
        Result.failure(Exception("Нет соединения с сервером"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun markRead(id: Int): Result<Unit> = try {
        if (apiService.markRead(id)) Result.success(Unit)
        else Result.failure(Exception("Не удалось отметить уведомление"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
