package com.example.coursework

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request

class AuthWorkerGett(context: Context, params: WorkerParameters) : Worker(context, params) {

    fun getting(){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.1.80:8080/users")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw kotlinx.io.IOException(
                        "Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}"
                    )
                }
                // пример получения конкретного заголовка ответа
                println("Server: ${response.header("Server")}")
                // вывод тела ответа
                println(response.body!!.string())
                Log.d("yyy", "succses")
            }
        }
        catch (e: IOException) {
            println("Ошибка подключения: $e");
        }
    }
    override fun doWork(): Result {
        // Фоновая задача
        getting()
        return Result.success()
    }
}