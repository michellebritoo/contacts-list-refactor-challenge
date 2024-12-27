package com.list.desafio.android.presentation

import com.list.desafio.android.data.model.UserResponse

sealed class UsersUIEvent {
    data object ShowError : UsersUIEvent()
    data class Loader(val shouldShowLoad: Boolean) : UsersUIEvent()
    data class ShowUsersList(val list: List<UserResponse>) : UsersUIEvent()
}