package com.example.coursework.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coursework.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    fun register(name: String, phoneNumber: String, password: String) {
        if (name.isBlank() || phoneNumber.isBlank() || password.isBlank()) {
            _state.value = RegisterState.Error("Заполните все поля")
            return
        }
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            val result = registerUseCase(name, phoneNumber, password)
            _state.value = if (result.isSuccess) RegisterState.Success
            else RegisterState.Error("Ошибка регистрации. Проверьте соединение")
        }
    }

    class Factory(private val registerUseCase: RegisterUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterViewModel(registerUseCase) as T
    }
}
