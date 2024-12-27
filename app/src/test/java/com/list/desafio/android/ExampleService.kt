package com.list.desafio.android

class ExampleService(
    private val service: UsersClient
) {

    fun example(): List<User> {
        val users = service.getUsers().execute()

        return users.body() ?: emptyList()
    }
}