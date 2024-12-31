package com.list.desafio.android

import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

inline fun <reified T> relaxedMock(): T = mockk(relaxed = true)

fun verifyNever(block: () -> Unit) {
    verify(exactly = 0) { block() }
}

fun coVerifyNever(block: suspend () -> Unit) {
    coVerify(exactly = 0) { block() }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}