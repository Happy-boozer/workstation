package com.example.coursework.data.repository

import com.example.coursework.data.remote.ApiService
import com.example.coursework.domain.model.Car
import com.example.coursework.domain.repository.CarRepository
import org.json.JSONArray

class CarRepositoryImpl(private val apiService: ApiService) : CarRepository {

    override suspend fun getCars(phoneNumber: String): Result<List<Car>> = try {
        val json = apiService.getCars(phoneNumber) ?: return Result.success(emptyList())
        val array = JSONArray(json)
        val cars = (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            Car(
                userId = obj.optInt("userId"),
                sign = obj.optString("plate"),
                vin = obj.optString("vin"),
                name = obj.optString("name"),
                status = obj.optString("status").ifBlank { null }
            )
        }
        Result.success(cars)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addCar(login: String, name: String, plate: String, vin: String): Result<Unit> = try {
        val (code, body) = apiService.addCar(login, name, plate, vin)
        when {
            code in 200..299 -> Result.success(Unit)
            code == 404 -> Result.failure(Exception("Пользователь «$login» не найден на сервере"))
            code == 400 -> Result.failure(Exception("Неверные данные: $body"))
            code == 500 -> Result.failure(Exception("Ошибка сервера: $body"))
            else -> Result.failure(Exception("Сервер вернул код $code: $body"))
        }
    } catch (e: java.net.ConnectException) {
        Result.failure(Exception("Нет соединения с сервером. Убедитесь, что сервер запущен"))
    } catch (e: java.net.SocketTimeoutException) {
        Result.failure(Exception("Сервер не отвечает (таймаут)"))
    } catch (e: Exception) {
        Result.failure(Exception("Неизвестная ошибка: ${e.message}"))
    }
}
