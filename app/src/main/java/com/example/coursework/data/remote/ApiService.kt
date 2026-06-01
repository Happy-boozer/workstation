package com.example.coursework.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

private const val BASE_URL = "http://192.168.1.77:3000"

class ApiService {
    private val client = OkHttpClient()

    suspend fun login(phoneNumber: String, password: String): String? = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("phone_number", phoneNumber)
            .add("password", password)
            .build()
        val request = Request.Builder().url("$BASE_URL/gi").post(body).build()
        client.newCall(request).execute().use { it.body?.string() }
    }

    suspend fun register(name: String, phoneNumber: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("username", name)
            .add("phone_number", phoneNumber)
            .add("password", password)
            .build()
        val request = Request.Builder().url("$BASE_URL/usver").post(body).build()
        client.newCall(request).execute().use { it.isSuccessful }
    }

    suspend fun getCars(phoneNumber: String): String? = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("phone_number", phoneNumber)
            .build()
        val request = Request.Builder().url("$BASE_URL/get_cars").post(body).build()
        client.newCall(request).execute().use { it.body?.string() }
    }

    suspend fun addCar(login: String, name: String, plate: String, vin: String): Pair<Int, String> = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("login", login)
            .add("name", name)
            .add("plate", plate)
            .add("VIN", vin)
            .build()
        val request = Request.Builder().url("$BASE_URL/insert_car").post(body).build()
        client.newCall(request).execute().use { Pair(it.code, it.body?.string() ?: "") }
    }

    suspend fun getNotifications(phoneNumber: String): String? = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("phone_number", phoneNumber)
            .build()
        val request = Request.Builder().url("$BASE_URL/get_notifications").post(body).build()
        client.newCall(request).execute().use { it.body?.string() }
    }

    suspend fun markRead(id: Int): Boolean = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("id", id.toString())
            .build()
        val request = Request.Builder().url("$BASE_URL/mark_read").post(body).build()
        client.newCall(request).execute().use { it.isSuccessful }
    }
}
