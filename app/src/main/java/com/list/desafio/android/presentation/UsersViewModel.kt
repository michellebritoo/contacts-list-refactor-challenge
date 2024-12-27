package com.list.desafio.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.list.desafio.android.data.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _viewState = MutableSharedFlow<UsersUIEvent>()
    val viewState: SharedFlow<UsersUIEvent> get() = _viewState

    fun onStart() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUsers()
                .onStart { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = true)) }
                .onCompletion { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = false)) }
                .catch { sendUIEvent(UsersUIEvent.ShowError) }
                .collect { response ->
                    sendUIEvent(UsersUIEvent.ShowUsersList(response))
                }
        }
    }

    private suspend fun sendUIEvent(event: UsersUIEvent) {
        _viewState.emit(event)
    }
}
