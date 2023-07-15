package com.lingshot.domain.model

sealed class Status<T> {
    class Default<T> : Status<T>()
    class Empty<T> : Status<T>()
    class Loading<T> : Status<T>()
    data class Success<T>(val data: T?) : Status<T>()
    data class Error<T>(val statusMessage: String?) : Status<T>()
}

fun <T> statusDefault(): Status<T> {
    return Status.Default()
}

fun <T> statusEmpty(): Status<T> {
    return Status.Empty()
}

fun <T> statusLoading(): Status<T> {
    return Status.Loading()
}

fun <T> statusSuccess(data: T?): Status<T> {
    return Status.Success(data)
}

fun <T> statusError(statusMessage: String?): Status<T> {
    return Status.Error(statusMessage)
}
