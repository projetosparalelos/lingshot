package com.teachmeprint.common.helper

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
        runCatching {
            data()
        }.onSuccess { value ->
            onCopy(Status.Success(value))
        }.onFailure { e ->
            when (e) {
                is HttpException -> {
                    onCopy(Status.Error(e.code()))
                }
                is IOException -> {
                    onCopy(
                        Status.Error(STATUS_TEXT_ERROR_GENERIC)
                    )
                }
            }
        }
    }
}