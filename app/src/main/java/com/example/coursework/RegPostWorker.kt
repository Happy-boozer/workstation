package com.example.coursework

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.io.IOException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class RegPostWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    fun sending(name: String, phone_number:String, password: String){
        val client = OkHttpClient()


        val formBody: RequestBody = FormBody.Builder()
            .add("username", name)
            .add("phone_number", phone_number)
            .add("password", password)
            .build();


        val request = Request.Builder()
            .url("http://10.192.208.1.80:8080/user")
            .post(formBody)
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
        val name = getInputData().getString("name").toString()
        val phone_number = getInputData().getString("phone_number").toString()
        val password = getInputData().getString("password").toString()
        Log.d("RRR","name = $name")
        sending(name = name, phone_number = phone_number, password = password)
        return Result.success()
    }
}