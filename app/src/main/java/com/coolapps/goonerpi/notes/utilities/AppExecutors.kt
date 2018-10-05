package com.coolapps.goonerpi.notes.utilities

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun runOnBackgroundThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

fun <T> runOnBackgroundThread(f: () -> T, onGet: (T) -> Unit) {
    IO_EXECUTOR.execute { onGet(f()) }

}