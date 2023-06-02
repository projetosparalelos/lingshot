package com.teachmeprint.common.helper

import androidx.compose.runtime.Composable
import com.teachmeprint.common.helper.StatusMessage.STATUS_TEXT_ERROR_GENERIC
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class Status<T> {
    class Default<T> : Status<T>()
    class Loading<T> : Status<T>()
    data class Success<T>(val data: T?) : Status<T>()
    data class Error<T>(val statusCode: Int?) : Status<T>()
}

val <T> Status<T>.isLoadingStatus get() =
    this is Status.Loading

fun <T> statusDefault(): Status<T> {
    return Status.Default()
}

fun <T> statusLoading(): Status<T> {
    return Status.Loading()
}

fun <T> statusSuccess(data: T?): Status<T> {
    return Status.Success(data)
}

fun <T> statusError(statusCode: Int?): Status<T> {
    return Status.Error(statusCode)
}

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
                    onCopy(Status.Error(e.code()))
                }
                is IOException -> {
                    onCopy(Status.Error(STATUS_TEXT_ERROR_GENERIC))
                }
            }
        }
    }
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
fun <T> Status<T>.onError(content: @Composable (Int) -> Unit): Status<T> {
    if (this is Status.Error) {
        statusCode?.let { content(it) }
    }
    return this
}