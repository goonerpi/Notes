package com.coolapps.goonerpi.notes.utilities

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun runOnIoThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

fun <T> runOnIoThread(f: () -> T, onGet: (T) -> Unit) {
    IO_EXECUTOR.execute { onGet(f()) }

}