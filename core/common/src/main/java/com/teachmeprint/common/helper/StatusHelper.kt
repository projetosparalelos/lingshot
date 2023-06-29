package com.teachmeprint.common.helper

import androidx.compose.runtime.Composable
import com.teachmeprint.domain.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

val <T> Status<T>.isLoadingStatus get() =
    this is Status.Loading

fun <T> CoroutineScope.launchWithStatus(
    data: suspend () -> T?,
    onCopy: (Status<T>) -> Unit
) {
    onCopy(Status.Loading())
    launch {
        try {
            val value = data()
            onCopy(Status.Success(value))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    onCopy(Status.Error(e.message()))
                }
                else -> {
                    onCopy(Status.Error(e.message))
                }
            }
        }
    }
}

@Composable
fun <T> Status<T>.onEmpty(content: @Composable () -> Unit): Status<T> {
    if (this is Status.Empty) {
        content()
    }
    return this
}

@Composable
fun <T> Status<T>.onLoading(content: @Composable () -> Unit): Status<T> {
    if (this is Status.Loading) {
        content()
    }
    return this
}

@Composable
fun <T> Status<T>.onSuccess(content: @Composable (T) -> Unit): Status<T> {
    if (this is Status.Success) {
        data?.let { content(it) }
    }
    return this
}

@Composable
fun <T> Status<T>.onError(content: @Composable (String) -> Unit): Status<T> {
    if (this is Status.Error) {
        statusMessage?.let { content(it) }
    }
    return this
}