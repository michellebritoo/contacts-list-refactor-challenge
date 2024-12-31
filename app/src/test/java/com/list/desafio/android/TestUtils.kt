package com.list.desafio.android

import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

inline fun <reified T> relaxedMock(): T = mockk(relaxed = true)

fun verifyNever(block: () -> Unit) {
    verify(exactly = 0) { block() }
}

fun coVerifyNever(block: suspend () -> Unit) {
    coVerify(exactly = 0) { block() }
}

suspend inline fun <reified T> Flow<Any>.withEvent(crossinline validation: (T) -> Unit) {
    val event = first()
    if (event is T) {
        validation(event)
    }
}

class CoroutineTestRule : TestRule {
    val testDispatcher = TestCoroutineDispatcher()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                    testDispatcher.cleanupTestCoroutines()
                }
            }
        }
    }
}