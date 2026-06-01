package com.example.coursework.presentation.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coursework.domain.model.Car
import com.example.coursework.domain.usecase.GetCarsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CarsState {
    object Idle : CarsState()
    object Loading : CarsState()
    data class Success(val cars: List<Car>) : CarsState()
    data class Error(val message: String) : CarsState()
}

class CarsViewModel(private val getCarsUseCase: GetCarsUseCase) : ViewModel() {

    private val _state = MutableStateFlow<CarsState>(CarsState.Idle)
    val state: StateFlow<CarsState> = _state

    fun loadCars(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = CarsState.Loading
            val result = getCarsUseCase(phoneNumber)
            _state.value = if (result.isSuccess)
                CarsState.Success(result.getOrDefault(emptyList()))
            else
                CarsState.Error("Не удалось загрузить автомобили")
        }
    }

    class Factory(private val getCarsUseCase: GetCarsUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CarsViewModel(getCarsUseCase) as T
    }
}
