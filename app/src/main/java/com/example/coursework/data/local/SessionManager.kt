package com.example.coursework.data.local

import android.content.Context

class SessionManager(private val context: Context) {

    fun saveLogin(phoneNumber: String) {
        context.openFileOutput("config.txt", Context.MODE_PRIVATE).use {
            it.write(phoneNumber.toByteArray())
        }
    }

    fun getLogin(): String? = try {
        context.openFileInput("config.txt").use { String(it.readBytes()) }.ifBlank { null }
    } catch (e: Exception) {
        null
    }

    fun clearLogin() {
        context.deleteFile("config.txt")
    }
}
