package com.aivanchenko.nametag.data.provider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher

    companion object {
        val Default = object : DispatchersProvider {
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
        }
    }
}
