package com.list.desafio.android.data.remote

import com.list.desafio.android.data.model.UserResponse
import retrofit2.http.GET

interface UsersClient {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}
