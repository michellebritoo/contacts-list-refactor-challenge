package com.list.desafio.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.local.entity.UserEntity
import com.list.desafio.android.data.remote.UserResponse
import com.list.desafio.android.presentation.adapter.UserUIModel
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
                    sendUIEvent(UsersUIEvent.ShowUsersList(response.responseToUIModel()))
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

    private fun List<UserResponse>.responseToUIModel(): List<UserUIModel> {
        return map { user ->
            UserUIModel(
                name = user.name.orEmpty(),
                username = user.username.orEmpty(),
                image = user.image.orEmpty()
            )
        }
    }

    private fun List<UserEntity>.entityToUIModel(): List<UserUIModel> {
        return map { user ->
            UserUIModel(
                name = user.name.orEmpty(),
                username = user.username.orEmpty(),
                image = user.image.orEmpty()
            )
        }
    }

    private fun List<UserResponse>.responseToEntity(): List<UserEntity> {
        return map { user ->
            UserEntity(
                id = user.id ?: 0,
                name = user.name.orEmpty(),
                username = user.username.orEmpty(),
                image = user.image.orEmpty()
            )
        }
    }
}
