package com.list.desafio.android.data

import com.list.desafio.android.coVerifyNever
import com.list.desafio.android.data.local.dao.UserDao
import com.list.desafio.android.data.local.entity.UserEntity
import com.list.desafio.android.data.remote.UserResponse
import com.list.desafio.android.data.remote.UsersClient
import com.list.desafio.android.relaxedMock
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UsersRepositoryImplTest {
    private val client: UsersClient = relaxedMock()
    private val userDao: UserDao = relaxedMock()
    private val repository = UsersRepositoryImpl(client, userDao)

    @Test
    fun `getAllUsers should return users from remote source`() = runTest {
        coEvery { client.getUsers() } returns usersResponseStub()

        val users = repository.getAllUsers().first()

        assertEquals(usersResponseStub(), users)
        coVerify { client.getUsers() }
        coVerifyNever { userDao.findAll() }
    }

    @Test
    fun `getAllUserFromLocalStorage should return users from local source`() = runTest {
        coEvery { userDao.findAll() } returns usersEntityStub()

        val users = repository.getAllUserFromLocalStorage().first()

        assertEquals(usersEntityStub(), users)
        coVerify { userDao.findAll() }
        coVerifyNever { client.getUsers() }
    }

    @Test
    fun `saveUser should insert users into local source`() = runTest {
        repository.saveUser(usersEntityStub().first())

        coVerify { userDao.insertUser(usersEntityStub().first()) }
    }

    private fun usersResponseStub() = listOf(
        UserResponse(id = 0, image = "http://image", name = "name", username = "username"),
        UserResponse(id = 1, image = "http://image2", name = "other name", username = "other username")
    )

    private fun usersEntityStub() = listOf(
        UserEntity(id = 0, image = "http://image", name = "name", username = "username"),
        UserEntity(id = 1, image = "http://image2", name = "other name", username = "other username")
    )
}