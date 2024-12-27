package com.list.desafio.android.presentation

import com.list.desafio.android.data.model.UserResponse

sealed class UsersUIEvent {
    data object ShowLoading : UsersUIEvent()
    data object HideLoading : UsersUIEvent()
    data object ShowError : UsersUIEvent()
    data class ShowUsersList(val list: List<UserResponse>) : UsersUIEvent()
}