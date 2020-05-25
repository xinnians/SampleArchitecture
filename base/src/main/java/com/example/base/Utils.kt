package com.example.base

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import java.lang.Math.max
import kotlin.system.measureTimeMillis

inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner, Observer { it?.apply(observer) })
}

fun CoroutineScope.timer(interval: Long, fixedRate: Boolean = true, action: suspend TimerScope.() -> Unit): Job {
    return launch {
        val scope = TimerScope()

        while (true) {
            val time = measureTimeMillis {
                try {
                    action(scope)
                } catch (ex: Exception) {
                    Log.e("Ian", ex.toString())
                }
            }

            if (scope.isCanceled) {
                break
            }

            if (fixedRate) {
                delay(kotlin.math.max(0, interval - time))
            } else {
                delay(interval)
            }

            yield()
        }
    }
}

class TimerScope {
    var isCanceled: Boolean = false
        private set

    fun cancel() {
        isCanceled = true
    }
}