package com.list.desafio.android

import retrofit2.Call
import retrofit2.http.GET

interface UsersClient {

    @GET("users")
    fun getUsers(): Call<List<User>>
}
