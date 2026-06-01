package com.example.coursework.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coursework.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(phoneNumber: String, password: String) {
        if (phoneNumber.isBlank() || password.isBlank()) {
            _state.value = LoginState.Error("Введите номер телефона и пароль")
            return
        }
        viewModelScope.launch {
            _state.value = LoginState.Loading
            val result = loginUseCase(phoneNumber, password)
            _state.value = when {
                result.isSuccess -> LoginState.Success
                result.exceptionOrNull()?.message == "notok" ->
                    LoginState.Error("Неверный номер телефона или пароль")
                else -> LoginState.Error("Ошибка соединения с сервером")
            }
        }
    }

    class Factory(private val loginUseCase: LoginUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LoginViewModel(loginUseCase) as T
    }
}
