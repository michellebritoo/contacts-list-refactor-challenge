package com.list.desafio.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.list.desafio.android.common.ext.entityToUIModel
import com.list.desafio.android.common.ext.responseToEntity
import com.list.desafio.android.common.ext.responseToUIModel
import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.remote.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _viewState = MutableSharedFlow<UsersUIEvent>()
    val viewState get() = _viewState.asSharedFlow()

    fun onStart() = getUserList()

    private fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUsers()
                .onStart { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = true)) }
                .onCompletion { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = false)) }
                .catch { getLocalData() }
                .collect { response ->
                    sendUIEvent(
                        UsersUIEvent.ShowUsersList(response.responseToUIModel())
                    )
                    saveLocalData(response)
                }
        }
    }

    private suspend fun saveLocalData(response: List<UserResponse>) {
        viewModelScope.launch(Dispatchers.IO) {
            sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = true))
            response.responseToEntity().forEach {
                repository.saveUser(it)
            }
            sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = false))
        }
    }

    private suspend fun getLocalData() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllUserFromLocalStorage()
            .onStart { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = true)) }
            .onCompletion { sendUIEvent(UsersUIEvent.Loader(shouldShowLoad = false)) }
            .catch { sendUIEvent(UsersUIEvent.ShowError) }
            .collect { users ->
                sendUIEvent(
                    UsersUIEvent.ShowUsersList(users.entityToUIModel())
                )
            }
    }

    private fun sendUIEvent(event: UsersUIEvent) = viewModelScope.launch(Dispatchers.Main) {
        _viewState.emit(event)
    }
}
