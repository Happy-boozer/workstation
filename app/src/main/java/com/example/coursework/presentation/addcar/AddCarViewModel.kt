package com.example.coursework.presentation.addcar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coursework.domain.usecase.AddCarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AddCarState {
    object Idle : AddCarState()
    object Loading : AddCarState()
    object Success : AddCarState()
    data class Error(val message: String) : AddCarState()
}

class AddCarViewModel(private val addCarUseCase: AddCarUseCase) : ViewModel() {

    private val _state = MutableStateFlow<AddCarState>(AddCarState.Idle)
    val state: StateFlow<AddCarState> = _state

    fun addCar(login: String, name: String, plate: String, vin: String) {
        when {
            login.isBlank() -> { _state.value = AddCarState.Error("Сессия истекла — войдите заново"); return }
            name.isBlank() -> { _state.value = AddCarState.Error("Введите название автомобиля"); return }
            plate.isBlank() -> { _state.value = AddCarState.Error("Введите государственный номер"); return }
            plate.length > 9 -> { _state.value = AddCarState.Error("Госномер не может быть длиннее 9 символов"); return }
            vin.isBlank() -> { _state.value = AddCarState.Error("Введите VIN-номер"); return }
            vin.length > 16 -> { _state.value = AddCarState.Error("VIN не может быть длиннее 16 символов (сейчас ${vin.length})"); return }
        }
        viewModelScope.launch {
            _state.value = AddCarState.Loading
            val result = addCarUseCase(login, name, plate, vin)
            _state.value = if (result.isSuccess) AddCarState.Success
            else AddCarState.Error(result.exceptionOrNull()?.message ?: "Неизвестная ошибка")
        }
    }

    class Factory(private val addCarUseCase: AddCarUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddCarViewModel(addCarUseCase) as T
    }
}
