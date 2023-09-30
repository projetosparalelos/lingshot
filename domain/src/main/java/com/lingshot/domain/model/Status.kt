/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
