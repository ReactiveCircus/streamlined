package io.github.reactivecircus.streamlined.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

@ExperimentalCoroutinesApi
val testCoroutineDispatcherProvider = CoroutineDispatcherProvider(
    io = TestCoroutineDispatcher(),
    computation = TestCoroutineDispatcher(),
    ui = TestCoroutineDispatcher()
)
