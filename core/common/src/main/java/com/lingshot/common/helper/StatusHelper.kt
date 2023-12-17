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
package com.lingshot.common.helper

import androidx.compose.runtime.Composable
import com.lingshot.domain.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

val <T> Status<T>.isLoadingStatus get() =
    this is Status.Loading

fun <T> CoroutineScope.launchWithStatus(
    data: suspend () -> T?,
    onCopy: (Status<T>) -> Unit,
): Job {
    onCopy(Status.Loading())
    return launch {
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
