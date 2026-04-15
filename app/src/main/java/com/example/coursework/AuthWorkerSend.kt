package com.example.coursework

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.io.IOException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class AuthWorkerSend(context: Context, params: WorkerParameters) : Worker(context, params) {

    fun sending(phone_number: String, password: String): String? {
        Log.d("WORKER_TEST", "sending() started")
        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("phone_number", phone_number)
            .add("password", password)// Данные поля
            .build()

        val request = Request.Builder()
            .url("http://192.168.1.80:8080/gi")
            .post(formBody)
            .build()
        var body: String? = ""

        try {
            Log.d("WORKER_TEST", "before request")
            client.newCall(request).execute().use { response ->

                Log.d("WORKER_TEST", "after request")

                val code = response.code
                body = response.body?.string()

                Log.d("WORKER_CODE", code.toString())
                Log.d("WORKER_BODY", body ?: "null")

                if (!response.isSuccessful) {
                    Log.e("WORKER_ERROR", "HTTP error: $code")
                }
            }
                /*throw kotlinx.io.IOException(
                        "Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}"
                    )
                    Log.d("WORKER_CODE", response.code.toString())
                }
                // пример получения конкретного заголовка ответа
                println("Server: ${response.header("Server")}")
                // вывод тела ответа
                println(response.body!!.string())
                val responseBody = response.body?.string()

                Log.d("SERVER_RESPONSE", responseBody ?: "null")
            }*/
        }
        catch (e: IOException) {
            println("Ошибка подключения: $e");
            Log.e("WORKER_ERROR", "Ошибка: $e")
        }

        return body
    }

    override fun doWork(): ListenableWorker.Result {
        // Фоновая задача
        Log.d("rrr", "SUKA")
        val phone_number = inputData.getString("login").toString()
        val password = inputData.getString("password").toString()
        sending(phone_number, password)
        val answer = sending(phone_number, password)

        val outputData = workDataOf(
            "response" to "${answer}"
        )

        return ListenableWorker.Result.success(outputData)
    }
}