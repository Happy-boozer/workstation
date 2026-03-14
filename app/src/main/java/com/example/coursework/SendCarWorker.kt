package com.example.coursework



import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.io.IOException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class SendCarWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    fun sending(name: String, plate: String, mark: String){
        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("name", name)
            .add("plate", plate)
            .add("mark", mark)// Данные поля
            .build()

        val request = Request.Builder()
            .url("http://192.168.1.80:8080/car")
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
                Log.d("SSSS", "succses")
            }
        }
        catch (e: IOException) {
            println("Ошибка подключения: $e");
        }
    }

    override fun doWork(): ListenableWorker.Result {
        // Фоновая задача
        val login = inputData.getString("login").toString()
        val plate = inputData.getString("plate").toString()
        val mark = inputData.getString("mark").toString()
        sending(login, plate, mark = mark)
        return ListenableWorker.Result.success()
    }
}