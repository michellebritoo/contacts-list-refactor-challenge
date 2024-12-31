package com.list.desafio.android.data.remote

import retrofit2.http.GET

interface UsersClient {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}
