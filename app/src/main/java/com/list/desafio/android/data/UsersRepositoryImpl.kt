package com.list.desafio.android.data

import com.list.desafio.android.data.local.dao.UserDao
import com.list.desafio.android.data.local.entity.UserEntity
import com.list.desafio.android.data.remote.UserResponse
import com.list.desafio.android.data.remote.UsersClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UsersRepository {
    suspend fun getAllUsers(): Flow<List<UserResponse>>
    suspend fun getAllUserFromLocalStorage(): Flow<List<UserEntity>>
    suspend fun saveUser(user: UserEntity)
}

class UsersRepositoryImpl(
    private val client: UsersClient,
    private val userDao: UserDao
): UsersRepository {
    override suspend fun getAllUsers(): Flow<List<UserResponse>> = flow {
        emit(client.getUsers())
    }

    override suspend fun getAllUserFromLocalStorage(): Flow<List<UserEntity>> = flow {
        emit(userDao.findAll())
    }

    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }
}
