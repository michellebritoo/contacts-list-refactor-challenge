package com.list.desafio.android.presentation

import app.cash.turbine.test
import com.list.desafio.android.TestCoroutineRule
import com.list.desafio.android.coVerifyNever
import com.list.desafio.android.common.ext.entityToUIModel
import com.list.desafio.android.common.ext.responseToUIModel
import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.local.entity.UserEntity
import com.list.desafio.android.data.remote.UserResponse
import com.list.desafio.android.relaxedMock
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository: UsersRepository = relaxedMock()
    private val viewModel = UsersViewModel(repository)

    @Before
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onStart with success api response should show list and save data on local storage`() = runTest {
        val usersList = usersResponseStub()
        coEvery { repository.getAllUsers() } returns flow { emit(usersList) }

        viewModel.onStart()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertEquals(UsersUIEvent.Loader(true), awaitItem())
            assertEquals(UsersUIEvent.ShowUsersList(usersList.responseToUIModel()), awaitItem())
            assertEquals(UsersUIEvent.Loader(false), awaitItem())
            assertEquals(UsersUIEvent.Loader(true), awaitItem())
            assertEquals(UsersUIEvent.Loader(false), awaitItem())
        }

        coVerify {
            repository.getAllUsers()
            repository.saveUser(any())
        }
        coVerifyNever { repository.getAllUserFromLocalStorage() }
    }

    @Test
    fun `onStart with failure api response should show list and find data on local storage`() = runTest {
        coEvery { repository.getAllUsers() } returns flow { throw Throwable("failure") }
        coEvery { repository.getAllUserFromLocalStorage() } returns flow { emit(usersEntityStub()) }

        viewModel.onStart()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertEquals(UsersUIEvent.Loader(true), awaitItem())
            assertEquals(UsersUIEvent.Loader(false), awaitItem())
            assertEquals(UsersUIEvent.Loader(true), awaitItem())
            assertEquals(UsersUIEvent.ShowUsersList(usersEntityStub().entityToUIModel()), awaitItem())
            assertEquals(UsersUIEvent.Loader(false), awaitItem())
        }

        coVerify {
            repository.getAllUsers()
            repository.getAllUserFromLocalStorage()
        }
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
