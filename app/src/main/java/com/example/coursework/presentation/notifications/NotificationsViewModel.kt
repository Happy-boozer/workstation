package com.example.coursework.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coursework.domain.model.Notification
import com.example.coursework.domain.usecase.GetNotificationsUseCase
import com.example.coursework.domain.usecase.MarkNotificationReadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NotificationsState {
    object Idle : NotificationsState()
    object Loading : NotificationsState()
    data class Success(val notifications: List<Notification>) : NotificationsState()
    data class Error(val message: String) : NotificationsState()
}

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markReadUseCase: MarkNotificationReadUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NotificationsState>(NotificationsState.Idle)
    val state: StateFlow<NotificationsState> = _state

    fun load(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = NotificationsState.Loading
            getNotificationsUseCase(phoneNumber)
                .onSuccess { _state.value = NotificationsState.Success(it) }
                .onFailure { _state.value = NotificationsState.Error(it.message ?: "Ошибка") }
        }
    }

    fun markRead(id: Int, phoneNumber: String) {
        viewModelScope.launch {
            markReadUseCase(id)
            load(phoneNumber)
        }
    }

    class Factory(
        private val getNotificationsUseCase: GetNotificationsUseCase,
        private val markReadUseCase: MarkNotificationReadUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NotificationsViewModel(getNotificationsUseCase, markReadUseCase) as T
    }
}
